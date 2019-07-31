package cn.ctoedu.miaosha.controller;

import cn.ctoedu.miaosha.annotation.AccessLimit;
import cn.ctoedu.miaosha.domain.MiaoshaOrder;
import cn.ctoedu.miaosha.domain.User;
import cn.ctoedu.miaosha.rabbitmq.MiaoshaMessage;
import cn.ctoedu.miaosha.rabbitmq.Producer;
import cn.ctoedu.miaosha.redis.GoodsKey;
import cn.ctoedu.miaosha.redis.MiaoshaKey;
import cn.ctoedu.miaosha.redis.OrderKey;
import cn.ctoedu.miaosha.redis.RedisService;
import cn.ctoedu.miaosha.service.GoodsService;
import cn.ctoedu.miaosha.service.MiaoshaService;
import cn.ctoedu.miaosha.service.OrderService;
import cn.ctoedu.miaosha.service.UserService;
import cn.ctoedu.miaosha.vo.GoodsVo;
import com.lhd.common.util.AjaxJson;
import com.lhd.common.util.CodeMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;

/**
 * Created by xiaomi on 2019/07/23
 */
@Controller
@RequestMapping("/miaosha")
public class MiaoshaController implements InitializingBean {

    Logger logger = LogManager.getLogger(MiaoshaController.class);

    @Resource
    UserService userService;

    @Resource
    RedisService redisService;

    @Resource
    GoodsService goodsService;

    @Resource
    OrderService orderService;

    @Resource
    MiaoshaService miaoshaService;

    @Resource
    Producer sender;

    private HashMap<Long, Boolean> localOverMap = new HashMap<>();

    /**
     * bean初始化的时候调用
     */
    @Override
    public void afterPropertiesSet() {
        logger.info("load local data after bean inited......");
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        if (goodsList == null) {
            return;
        }
        for (GoodsVo goods : goodsList) {
            redisService.set(GoodsKey.getMiaoshaGoodsStock, "" + goods.getId(), goods.getStockCount());
            localOverMap.put(goods.getId(), false);
        }
    }

    /**
     * 秒杀验证码
     *
     * @param response
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping(value = "/verifyCode", method = RequestMethod.GET)
    @ResponseBody
    public AjaxJson getMiaoshaVerifyCod(HttpServletResponse response, User user, @RequestParam("goodsId") long goodsId) {
        if (user == null) {
            return AjaxJson.error(CodeMessage.SESSION_ERROR);
        }
        try {
            BufferedImage image = miaoshaService.createVerifyCode(user, goodsId);
            OutputStream out = response.getOutputStream();
            ImageIO.write(image, "JPEG", out);
            out.flush();
            out.close();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxJson.error(CodeMessage.MIAOSHA_FAIL);
        }
    }


    /**
     * 获取秒杀地址
     *
     * @param user
     * @param goodsId
     * @param verifyCode
     * @return
     */
    @AccessLimit(seconds = 5, maxCount = 5)
    @RequestMapping(value = "/path", method = RequestMethod.GET)
    @ResponseBody
    public AjaxJson getMiaoshaPath(User user, @RequestParam("goodsId") long goodsId,
                                   @RequestParam(value = "verifyCode", defaultValue = "0") int verifyCode) {
        if (user == null) {
            return AjaxJson.error(CodeMessage.SESSION_ERROR);
        }
        boolean check = miaoshaService.checkVerifyCode(user, goodsId, verifyCode);
        if (!check) {
            return AjaxJson.error(CodeMessage.REQUEST_ILLEGAL);
        }
        String path = miaoshaService.createMiaoshaPath(user, goodsId);
        return AjaxJson.success(path);
    }

    /**
     * 开始秒杀
     * QPS:1306
     * 5000 * 10
     * QPS: 2114
     */
    @RequestMapping(value = "/{path}/do_miaosha", method = RequestMethod.POST)
    @ResponseBody
    public AjaxJson miaosha(Model model, User user, @RequestParam("goodsId") long goodsId, @PathVariable("path") String path) {
        model.addAttribute("user", user);
        if (user == null) {
            return AjaxJson.error(CodeMessage.SESSION_ERROR);
        }
        //验证path
        boolean check = miaoshaService.checkPath(user, goodsId, path);
        if (!check) {
            return AjaxJson.error(CodeMessage.REQUEST_ILLEGAL);
        }
        //内存标记，减少redis访问
        boolean over = localOverMap.get(goodsId);
        if (over) {
            return AjaxJson.error(CodeMessage.MIAO_SHA_OVER);
        }
        //预减库存
        long stock = redisService.decr(GoodsKey.getMiaoshaGoodsStock, "" + goodsId);//10
        if (stock < 0) {
            localOverMap.put(goodsId, true);
            return AjaxJson.error(CodeMessage.MIAO_SHA_OVER);
        }
        //判断是否已经秒杀到了
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
        if (order != null) {
            return AjaxJson.error(CodeMessage.REPEATE_MIAOSHA);
        }
        //入队
        MiaoshaMessage mm = new MiaoshaMessage();
        mm.setUser(user);
        mm.setGoodsId(goodsId);
        sender.sendMiaoshaMessage(mm);
        return AjaxJson.success(0); //排队中
    }

    /**
     * orderId：成功
     * -1：秒杀失败
     * 0： 排队中
     */
    @RequestMapping(value = "/result", method = RequestMethod.GET)
    @ResponseBody
    public AjaxJson miaoshaResult(Model model, User user, @RequestParam("goodsId") long goodsId) {
        model.addAttribute("user", user);
        if (user == null) {
            return AjaxJson.error(CodeMessage.SESSION_ERROR);
        }
        long result = miaoshaService.getMiaoshaResult(user.getId(), goodsId);
        return AjaxJson.success(result);
    }

    /**
     *
     * 重置所有秒杀数据
     * @param model
     * @return
     */
    @RequestMapping(value = "/reset", method = RequestMethod.GET)
    @ResponseBody
    public AjaxJson reset(Model model) {
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        for (GoodsVo goods : goodsList) {
            goods.setStockCount(10);
            redisService.set(GoodsKey.getMiaoshaGoodsStock, "" + goods.getId(), 10);
            localOverMap.put(goods.getId(), false);
        }
        redisService.delete(OrderKey.getMiaoshaOrderByUidGid);
        redisService.delete(MiaoshaKey.isGoodsOver);
        miaoshaService.reset(goodsList);
        return AjaxJson.success(true);
    }

}
