package cn.ctoedu.miaosha.controller;

import cn.ctoedu.miaosha.domain.User;
import cn.ctoedu.miaosha.redis.GoodsKey;
import cn.ctoedu.miaosha.redis.RedisService;
import cn.ctoedu.miaosha.service.GoodsService;
import cn.ctoedu.miaosha.vo.GoodsDetailVo;
import cn.ctoedu.miaosha.vo.GoodsVo;
import com.lhd.common.util.AjaxJson;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xiaomi on 2019/07/23
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Resource
    RedisService redisService;

    @Resource
    GoodsService goodsService;

    @Resource
    ThymeleafViewResolver thymeleafViewResolver;

    @Resource
    ApplicationContext applicationContext;

    /**
     * QPS:1267 load:15 mysql
     * 5000 * 10
     * QPS:2884, load:5
     */
    @RequestMapping(value = "/to_list", produces = "text/html")
    @ResponseBody
    public String list(HttpServletRequest request, HttpServletResponse response, Model model, User user) {
        model.addAttribute("user", user);
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        model.addAttribute("goodsList", goodsList);
        WebContext ctx = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
        String html = thymeleafViewResolver.getTemplateEngine().process("goods_list", ctx);
        if (!StringUtils.isEmpty(html)) {
            redisService.set(GoodsKey.getGoodsList, "", html);
        }
        return html;
    }

    @RequestMapping(value = "/detail/{goodsId}")
    @ResponseBody
    public AjaxJson detail(Model model, User user, @PathVariable("goodsId") long goodsId) {
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);

        GoodsDetailVo vo = new GoodsDetailVo();
        vo.setGoods(goods);
        vo.setUser(user);
        vo.setRemainSeconds(this.getMiaoshaStatus(goods).get("miaoshaStatus"));
        vo.setMiaoshaStatus(this.getMiaoshaStatus(goods).get("remainSeconds"));
        return AjaxJson.success(vo);
    }

    /**
     *
     * @param goods
     * @return
     */
    private Map<String, Integer> getMiaoshaStatus(GoodsVo goods){
        Map<String, Integer> result = new HashMap<>();
        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();
        int miaoshaStatus, remainSeconds;
        if (now < startAt) {//秒杀还没开始，倒计时
            miaoshaStatus = 0;
            remainSeconds = (int) ((startAt - now) / 1000);
        } else if (now > endAt) {//秒杀已经结束
            miaoshaStatus = 2;
            remainSeconds = -1;
        } else {//秒杀进行中
            miaoshaStatus = 1;
            remainSeconds = 0;
        }

        result.put("miaoshaStatus", miaoshaStatus);
        result.put("remainSeconds", remainSeconds);
        return result;
    }
}
