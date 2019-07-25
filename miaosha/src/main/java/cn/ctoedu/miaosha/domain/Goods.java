package cn.ctoedu.miaosha.domain;

import lombok.Data;

import java.util.Date;

/**
 * Created by xiaomi on 2019/07/22
 */
@Data
public class Goods {

    private Long id;
    private String goodsName;
    private String goodsTitle;
    private String goodsImg;
    private String goodsDetail;
    private Double goodsPrice;
    private Integer goodsStock;
    private Date createDate;
    private Date updateDate;
}
