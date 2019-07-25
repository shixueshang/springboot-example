package cn.ctoedu.miaosha.rabbitmq;

import cn.ctoedu.miaosha.domain.User;
import lombok.Data;

/**
 * Created by xiaomi on 2019/07/23
 */
@Data
public class MiaoshaMessage {

    private User user;
    private long goodsId;
}
