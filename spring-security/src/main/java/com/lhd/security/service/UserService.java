package com.lhd.security.service;

import com.lhd.security.domain.User;

/**
 * Created by lihongde on 2018/06/15.
 */
public interface UserService {

    User getCurrentUser(String account);

    void register(User user);

    String login(String username, String password);

    String refresh(String oldToken, String tokenHead);
}
