package cn.ctoedu.miaosha.domain;

import lombok.Data;

/**
 * Created by xiaomi on 2019/07/22
 */
@Data
public class MiaoshaOrder {

    private Long id;
    private Long userId;
    private Long orderId;
    private Long goodsId;
}
