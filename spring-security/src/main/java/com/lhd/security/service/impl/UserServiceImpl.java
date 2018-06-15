package com.lhd.security.service.impl;

import com.lhd.security.config.JwtTokenUtil;
import com.lhd.security.config.JwtUser;
import com.lhd.security.domain.Role;
import com.lhd.security.domain.User;
import com.lhd.security.repository.RoleRepository;
import com.lhd.security.repository.UserRepository;
import com.lhd.security.service.UserService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;

/**
 * Created by lihongde on 2018/06/15.
 */
@Service
public class UserServiceImpl  implements UserService{

    private UserRepository userRepository;

    private RoleRepository roleRepository;

    private JwtTokenUtil jwtTokenUtil;

    private AuthenticationManager authenticationManager;

    private UserDetailsService userDetailsService;


    @Override
    public User getCurrentUser(String account) {

        return userRepository.findByAccount(account);
    }

    @Override
    public void register(User user) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        final String rawPassword = user.getPassword();
        user.setPassword(encoder.encode(rawPassword));
        user.setLastPasswordResetDate(new Date());
        Role userRole = roleRepository.findByName("ROLE_USER");
        if (userRole == null){
            userRole = roleRepository.save(new Role("ROLE_USER"));
        }
        user.setRoles(Collections.singletonList(userRole));
        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
           e.printStackTrace();
        }
    }

    @Override
    public String login(String username, String password) {
        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(username, password);
        try {
            final Authentication authentication = authenticationManager.authenticate(upToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            return jwtTokenUtil.generateToken(userDetails);
        } catch (BadCredentialsException e) {

        }
        return null;
    }

    @Override
    public String refresh(String oldToken, String tokenHead) {
        final String token = oldToken.substring(tokenHead.length());
        String username = jwtTokenUtil.getUsernameFromToken(token);
        JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);
        if (jwtTokenUtil.canTokenBeRefreshed(token, user.getLastPasswordResetDate())){
            return jwtTokenUtil.refreshToken(token);
        }
        return null;
    }
}
