package cn.ctoedu.miaosha.vo;

import cn.ctoedu.miaosha.domain.User;
import lombok.Data;

/**
 * Created by xiaomi on 2019/07/23
 */
@Data
public class GoodsDetailVo {

        private int miaoshaStatus = 0;
        private int remainSeconds = 0;
        private GoodsVo goods ;
        private User user;
}
