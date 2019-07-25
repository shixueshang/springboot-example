package cn.ctoedu.miaosha.redis;

/**
 * Created by xiaomi on 2019/07/23
 */
public class MiaoshaUserKey extends BasePrefix {

    public static final int TOKEN_EXPIRE = 3600;

    private MiaoshaUserKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static MiaoshaUserKey token = new MiaoshaUserKey(TOKEN_EXPIRE, "tk");
    public static MiaoshaUserKey getById = new MiaoshaUserKey(TOKEN_EXPIRE, "id");
}
