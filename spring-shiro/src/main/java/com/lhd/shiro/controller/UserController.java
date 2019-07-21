package com.lhd.shiro.controller;

import com.lhd.common.util.CommonUtil;
import com.lhd.shiro.entity.User;
import com.lhd.shiro.service.UserService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Created by xiaomi on 2019/05/22
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * RequiresRoles 是所需角色 包含 AND 和 OR 两种
     * RequiresPermissions 是所需权限 包含 AND 和 OR 两种
     *
     * @return msg
     */
    @RequiresRoles(value = {"admin", "test"}, logical = Logical.OR)
    //@RequiresPermissions(value = {"user:list", "user:query"}, logical = Logical.OR)
    @GetMapping("/query")
    public String query() {
        return "query.....";
    }

    @GetMapping("/find")
    public String find() {
        return "find.....";
    }

    @PostMapping("/add")
    public ResponseEntity<String> addUser(@RequestBody User user){

        String salt = CommonUtil.createRandom(20, false);
        user.setPassword(CommonUtil.md5(salt + user.getPassword()));
        userService.saveUser(user);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
