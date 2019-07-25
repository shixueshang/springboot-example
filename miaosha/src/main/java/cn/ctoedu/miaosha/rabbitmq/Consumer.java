package cn.ctoedu.miaosha.rabbitmq;

import cn.ctoedu.miaosha.config.MQConfig;
import cn.ctoedu.miaosha.domain.MiaoshaOrder;
import cn.ctoedu.miaosha.domain.User;
import cn.ctoedu.miaosha.redis.RedisService;
import cn.ctoedu.miaosha.service.GoodsService;
import cn.ctoedu.miaosha.service.MiaoshaService;
import cn.ctoedu.miaosha.service.OrderService;
import cn.ctoedu.miaosha.vo.GoodsVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by xiaomi on 2019/07/23
 */
@Component
public class Consumer {

    private static Logger log = LogManager.getLogger(Consumer.class);

    @Autowired
    RedisService redisService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    MiaoshaService miaoshaService;

    @RabbitListener(queues = MQConfig.MIAOSHA_QUEUE)
    public void receive(String message) {
        log.info("receive message:" + message);
        MiaoshaMessage mm = RedisService.stringToBean(message, MiaoshaMessage.class);
        User user = mm.getUser();
        long goodsId = mm.getGoodsId();

        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goods.getStockCount();
        if (stock <= 0) {
            return;
        }
        //判断是否已经秒杀到了
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
        if (order != null) {
            return;
        }
        //减库存 下订单 写入秒杀订单
        miaoshaService.miaosha(user, goods);
    }

}
