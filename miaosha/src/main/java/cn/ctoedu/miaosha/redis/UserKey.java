package cn.ctoedu.miaosha.redis;

/**
 * Created by xiaomi on 2019/07/23
 */
public class UserKey extends BasePrefix {

    private UserKey(String prefix) {
        super(prefix);
    }

    public static UserKey getById = new UserKey("id");
    public static UserKey getByName = new UserKey("name");
}
