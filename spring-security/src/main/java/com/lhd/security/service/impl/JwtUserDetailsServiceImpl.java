package com.lhd.security.service.impl;

import com.lhd.security.config.JwtUser;
import com.lhd.security.domain.Role;
import com.lhd.security.domain.User;
import com.lhd.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by lihongde on 2018/06/15.
 */
@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    /**
     * 提供一种从用户名可以查到用户并返回的方法
     * @param account 帐号
     * @return UserDetails
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String account) throws UsernameNotFoundException {
        User user = userRepository.findByAccount(account);

        if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", account));
        } else {
            return new JwtUser(
                    user.getId(),
                    user.getAccount(),
                    user.getPassword(),
                    mapToGrantedAuthorities(user.getRoles().stream().map(Role::getName).collect(Collectors.toList())),
                    user.getLastPasswordResetDate()
            );
        }
    }

    private List<GrantedAuthority> mapToGrantedAuthorities(List<String> authorities) {
        return authorities.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
