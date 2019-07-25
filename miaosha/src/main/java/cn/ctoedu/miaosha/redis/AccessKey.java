package cn.ctoedu.miaosha.redis;

/**
 * Created by xiaomi on 2019/07/23
 */
public class AccessKey extends BasePrefix {

    private AccessKey( int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static AccessKey withExpire(int expireSeconds) {
        return new AccessKey(expireSeconds, "access");
    }
}
