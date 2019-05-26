package com.lhd.security.controller;

import com.lhd.common.util.AjaxJson;
import com.lhd.security.domain.User;
import com.lhd.security.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by lihongde on 2018/06/15.
 */
@RestController
@RequestMapping("/auth")
public class LoginController extends BaseController {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Resource
    private UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public AjaxJson login(String account, String password){
        return new AjaxJson(HttpStatus.OK.value(), userService.login(account, account));
    }

    @RequestMapping(value = "/refresh", method = RequestMethod.GET)
    public ResponseEntity<?> refreshToken(){
        String token = request.getHeader(tokenHeader);
        String refreshedToken = userService.refresh(token, tokenHead);
        if (refreshedToken == null) {
            return ResponseEntity.badRequest().body(null);
        } else {
            return ResponseEntity.ok(refreshedToken);
        }
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public AjaxJson register(@RequestBody User addedUser) {
        userService.register(addedUser);

        return new AjaxJson(HttpStatus.OK.value());
    }

}
