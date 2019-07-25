package cn.ctoedu.miaosha.config;

import cn.ctoedu.miaosha.domain.User;

/**
 * Created by xiaomi on 2019/07/22
 */
public class UserContext {

    private static ThreadLocal<User> userHolder = new ThreadLocal<>();

    public static void setUser(User user) {
        userHolder.set(user);
    }

    public static User getUser() {
        return userHolder.get();
    }
}
