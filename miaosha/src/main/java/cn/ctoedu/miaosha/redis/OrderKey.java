package cn.ctoedu.miaosha.redis;

/**
 * Created by xiaomi on 2019/07/23
 */
public class OrderKey extends BasePrefix {

    public OrderKey(String prefix) {
        super(prefix);
    }

    public static OrderKey getMiaoshaOrderByUidGid = new OrderKey("moug");
}
