package cn.ctoedu.miaosha.rabbitmq;

import cn.ctoedu.miaosha.config.MQConfig;
import cn.ctoedu.miaosha.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by xiaomi on 2019/07/23
 */
@Component
public class Producer {

    private static Logger log = LoggerFactory.getLogger(Producer.class);

    @Autowired
    AmqpTemplate amqpTemplate ;

    public void sendMiaoshaMessage(MiaoshaMessage mm) {
        String msg = RedisService.beanToString(mm);
        log.info("send message:"+msg);
        amqpTemplate.convertAndSend(MQConfig.MIAOSHA_QUEUE, msg);
    }
}
