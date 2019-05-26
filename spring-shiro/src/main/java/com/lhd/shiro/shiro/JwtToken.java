package com.lhd.shiro.shiro;

import lombok.Getter;
import org.apache.shiro.authc.AuthenticationToken;

/**
 * Created by xiaomi on 2019/05/22
 */
@Getter
public class JwtToken implements AuthenticationToken {

    private String token;

    public JwtToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
