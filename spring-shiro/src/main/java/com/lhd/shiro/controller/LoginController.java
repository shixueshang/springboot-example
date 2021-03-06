package com.lhd.shiro.controller;

import com.lhd.shiro.entity.User;
import com.lhd.shiro.service.UserService;
import com.lhd.shiro.util.JwtUtil;
import org.apache.shiro.authc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Created by xiaomi on 2019/05/22
 */
@RestController
public class LoginController {

    private final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Resource
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user){

        if(StringUtils.isEmpty(user.getUserName())){
            return new ResponseEntity<>("用户名不能为空", HttpStatus.BAD_REQUEST);
        }

        if(StringUtils.isEmpty(user.getPassword())){
            return new ResponseEntity<>("密码不能为空", HttpStatus.BAD_REQUEST);
        }

        String token;
        try {
            //认证成功生成token
            token = JwtUtil.sign(user.getUserName(), user.getPassword());
        } catch (AuthenticationException e) {
            logger.error("对用户[{}]进行登录验证,验证未通过", user.getUserName(), e);
            return new ResponseEntity<>("AuthenticationException", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(token, HttpStatus.OK);

    }

    @GetMapping(value = "/hello")
    public String hello() {
        return "hello...";
    }

    @GetMapping(value = "/401")
    public String denied() {
        logger.info("小伙子权限不足,别无谓挣扎了...");
        return "denied...";
    }
}
