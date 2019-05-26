package com.lhd.shiro.service.impl;

import com.lhd.shiro.entity.User;
import com.lhd.shiro.service.UserService;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by xiaomi on 2019/05/19
 */
@Service
public class UserServiceImpl implements UserService {


    public static final Map<String, User> USERS_CACHE = new HashMap<>();

    public static final Map<String, Collection<String>> PERMISSIONS_CACHE = new HashMap<>();

    static {
        USERS_CACHE.put("u1", new User(1L, "u1", "p1", "admin", true));
        USERS_CACHE.put("u2", new User(2L, "u2", "p2", "admin", false));
        USERS_CACHE.put("u3", new User(3L, "u3", "p3", "test", true));

        PERMISSIONS_CACHE.put("admin", Arrays.asList("user:list", "user:add", "user:edit"));
        PERMISSIONS_CACHE.put("test", Collections.singletonList("user:list"));

    }

    @Override
    public User findUserByName(String userName) {
        return USERS_CACHE.get(userName);
    }
}
