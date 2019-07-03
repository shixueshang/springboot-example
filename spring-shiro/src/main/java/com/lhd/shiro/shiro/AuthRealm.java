package com.lhd.shiro.shiro;

import com.lhd.shiro.entity.User;
import com.lhd.shiro.service.UserService;
import com.lhd.shiro.service.impl.UserServiceImpl;
import com.lhd.shiro.util.JwtUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by xiaomi on 2019/05/19
 */

@Component
public class AuthRealm extends AuthorizingRealm {

    @Resource
    private UserService userService;

    /**
     * 必须重写此方法，不然Shiro会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /**
     * 调用subject.login(token)之后会调用该方法
     * 认证回调函数,登录时调用
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        String token = (String) authenticationToken.getCredentials();
        String userName = JwtUtil.getUserName(token);
        if (userName == null) {
            throw new AuthenticationException("用户名无效");
        }

        User user = userService.findUserByName(userName);

        if(user == null){
            throw new AuthenticationException("用户不存在!");
        }

        if (!JwtUtil.verify(token, userName, user.getPassword())) {
            throw new AuthenticationException("用户名或密码错误");
        }

        return new SimpleAuthenticationInfo(token, token, getName());
    }

    /**
     * 只有需要验证权限时才会调用, 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.在配有缓存的情况下，只加载一次.
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {


        Session session = SecurityUtils.getSubject().getSession();
        String username = JwtUtil.getUserName(principalCollection.toString());
        User user = userService.findUserByName(username);

        // 权限信息对象info,用来存放查出的用户的所有的角色（role）及权限（permission）
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        // 用户的角色集合
        Set<String> roles = new HashSet<>();
        roles.add(user.getPermission());
        info.setRoles(roles);
        // 用户的角色对应的所有权限，如果只使用角色定义访问权限，下面可以不要
        // 只有角色并没有颗粒度到每一个按钮 或 是操作选项  PERMISSIONS 是可选项
        final Map<String, Collection<String>> permissionsCache = UserServiceImpl.PERMISSIONS_CACHE;
        final Collection<String> permissions = permissionsCache.get(user.getPermission());
        info.addStringPermissions(permissions);
        return info;
    }


}
