package com.lhd.shiro.controller;

import com.lhd.shiro.util.JwtUtil;
import org.apache.shiro.authc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by xiaomi on 2019/05/22
 */
@RestController
public class LoginController {

    private final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @GetMapping("/login")
    public ResponseEntity<String> login(String username, String password){

        if(StringUtils.isEmpty(username)){
            return new ResponseEntity<>("用户名不能为空", HttpStatus.BAD_REQUEST);
        }

        if(StringUtils.isEmpty(password)){
            return new ResponseEntity<>("密码不能为空", HttpStatus.BAD_REQUEST);
        }

        String jwtToken;
        try {
            //认证成功生成token
            jwtToken = JwtUtil.sign(username, password);
        } catch (AuthenticationException e) {
            logger.error("对用户[{}]进行登录验证,验证未通过", username, e);
            return new ResponseEntity<>("AuthenticationException", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(jwtToken, HttpStatus.OK);

    }

    @GetMapping(value = "/hello")
    public String hello() {
        logger.info("不登录也可以访问...");
        return "hello...";
    }

    @GetMapping(value = "/index")
    public String index() {
        logger.info("登陆成功了...");
        return "index";
    }

    @GetMapping(value = "/denied")
    public String denied() {
        logger.info("小伙子权限不足,别无谓挣扎了...");
        return "denied...";
    }
}
