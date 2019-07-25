package cn.ctoedu.miaosha.redis;

/**
 * Created by xiaomi on 2019/07/23
 */
public interface KeyPrefix {

     int expireSeconds();

     String getPrefix();
}
