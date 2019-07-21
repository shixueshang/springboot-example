package com.lhd.shiro.service.impl;

import com.lhd.shiro.entity.User;
import com.lhd.shiro.repository.UserRepository;
import com.lhd.shiro.service.UserService;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by xiaomi on 2019/05/19
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserRepository userRepository;

    public static final Map<String, Collection<String>> PERMISSIONS_CACHE = new HashMap<>();

    static {
        PERMISSIONS_CACHE.put("admin", Arrays.asList("user:list", "user:add", "user:edit"));
        PERMISSIONS_CACHE.put("test", Collections.singletonList("user:list"));

    }

    @Override
    public User findUserByName(String userName) {
        User user = new User(userName);
        Example<User> example = Example.of(user);
        Optional<User> optional = userRepository.findOne(example);

        return optional.isPresent() ? optional.get() : null;
    }

    @Override
    @Transactional
    public void saveUser(User user) {

        userRepository.save(user);
    }
}
