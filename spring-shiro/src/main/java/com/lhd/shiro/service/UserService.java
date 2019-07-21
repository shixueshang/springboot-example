package com.lhd.shiro.service;


import com.lhd.shiro.entity.User;

/**
 * Created by xiaomi on 2019/05/19
 */
public interface UserService {

    User findUserByName(String userName);

    void saveUser(User user);
}
