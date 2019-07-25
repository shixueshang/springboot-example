package cn.ctoedu.miaosha.vo;

import cn.ctoedu.miaosha.domain.OrderInfo;
import lombok.Data;

/**
 * Created by xiaomi on 2019/07/23
 */
@Data
public class OrderDetailVo {

    private GoodsVo goods;
    private OrderInfo order;
}
