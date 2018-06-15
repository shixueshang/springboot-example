package com.lhd.security.controller;

import com.lhd.security.config.JwtTokenUtil;
import com.lhd.security.domain.User;
import com.lhd.security.repository.UserRepository;
import com.lhd.security.service.UserService;
import com.lhd.security.util.AjaxJson;
import com.lhd.security.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by lihongde on 2018/06/15.
 */
@RestController
@RequestMapping("/users")
public class UserController extends BaseController {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Autowired
    private UserRepository repository;

    @Autowired
    private UserService userService;

    private JwtTokenUtil jwtTokenUtil;

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(method = RequestMethod.GET)
    public List<User> getUsers() {
        return repository.findAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(method = RequestMethod.POST)
    User addUser(@RequestBody User addedUser) {
        return repository.save(addedUser);
    }

    @PostAuthorize("returnObject.username == principal.username or hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public User getUser(@PathVariable String id) {
        return repository.findById(id).get();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    User updateUser(@PathVariable String id, @RequestBody User updatedUser) {
        updatedUser.setId(id);
        return repository.save(updatedUser);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    User removeUser(@PathVariable String id) {
        User deletedUser = repository.findById(id).get();
        repository.delete(deletedUser);
        return deletedUser;
    }

    @PostAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public AjaxJson getUserByUsername(@RequestParam(value = "username") String username) {
        return new AjaxJson(HttpStatus.OK.value(), repository.findByUsername(username));
    }

    @PostAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/current", method = RequestMethod.GET)
    public AjaxJson getCurrentUser() {
        String authHeader = request.getHeader(this.tokenHeader);
        if (authHeader != null && authHeader.startsWith(tokenHead)) {
            final String authToken = authHeader.substring(tokenHead.length()); // The part after "Bearer "
            User user = userService.getCurrentUser(jwtTokenUtil.getUsernameFromToken(authToken));
            if (user == null) {
                return new AjaxJson(HttpStatus.INTERNAL_SERVER_ERROR.value(), "token错误");
            }
            user.setAccount(CommonUtil.md5Hex(user.getAccount()));
            return new AjaxJson(HttpStatus.OK.value(), user);
        }

        return new AjaxJson(HttpStatus.OK.value());
    }
}
