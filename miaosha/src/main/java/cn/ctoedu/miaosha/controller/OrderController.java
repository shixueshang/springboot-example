package cn.ctoedu.miaosha.controller;

import cn.ctoedu.miaosha.domain.User;
import cn.ctoedu.miaosha.domain.OrderInfo;
import cn.ctoedu.miaosha.service.GoodsService;
import cn.ctoedu.miaosha.service.OrderService;
import cn.ctoedu.miaosha.vo.GoodsVo;
import cn.ctoedu.miaosha.vo.OrderDetailVo;
import com.lhd.common.util.AjaxJson;
import com.lhd.common.util.CodeMessage;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by xiaomi on 2019/07/23
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Resource
    OrderService orderService;

    @Resource
    GoodsService goodsService;

    @RequestMapping("/detail")
    public AjaxJson info(Model model, User user, @RequestParam("orderId") long orderId) {
        if (user == null) {
            return AjaxJson.error(CodeMessage.SESSION_ERROR);
        }
        OrderInfo order = orderService.getOrderById(orderId);
        if (order == null) {
            return AjaxJson.error(CodeMessage.ORDER_NOT_EXIST);
        }
        long goodsId = order.getGoodsId();
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        OrderDetailVo vo = new OrderDetailVo();
        vo.setOrder(order);
        vo.setGoods(goods);
        return AjaxJson.success(vo);
    }
}
