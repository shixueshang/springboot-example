package cn.ctoedu.miaosha.domain;

import lombok.Data;

import java.util.Date;

/**
 * Created by xiaomi on 2019/07/22
 */
@Data
public class MiaoshaGoods {

    private Long id;
    private Long goodsId;
    private Double miaoshaPrice;
    private Integer stockCount;
    private Date startDate;
    private Date endDate;
}
