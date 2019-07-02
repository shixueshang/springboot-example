package com.lhd.shiro.shiro;

import com.lhd.shiro.filter.JWTFilter;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by xiaomi on 2019/05/19
 */
@Configuration
public class ShiroConfig {

    private Logger logger = LoggerFactory.getLogger(ShiroConfig.class);

    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        // 必须设置 SecurityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        // 添加自己的过滤器并且取名为jwt
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("jwt", new JWTFilter());
        shiroFilterFactoryBean.setFilters(filterMap);

        shiroFilterFactoryBean.setLoginUrl("/login");
        // 登录成功后要跳转的连接
        shiroFilterFactoryBean.setSuccessUrl("/index");
        shiroFilterFactoryBean.setUnauthorizedUrl("/denied");
        loadShiroFilterChain(shiroFilterFactoryBean);
        return shiroFilterFactoryBean;
    }

    @Bean("securityManager")
    public SecurityManager securityManager(AuthRealm realm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(realm);

        // 关闭shiro自带的session,详情见文档
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator evaluator = new DefaultSessionStorageEvaluator();
        evaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(evaluator);
        securityManager.setSubjectDAO(subjectDAO);
        return securityManager;
    }


    private void loadShiroFilterChain(ShiroFilterFactoryBean shiroFilterFactoryBean) {
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();

        //登陆操作走过滤器 进行shiro认证
        filterChainDefinitionMap.put("/login", "anon");

        filterChainDefinitionMap.put("/401", "anon");
        filterChainDefinitionMap.put("/404", "anon");

        filterChainDefinitionMap.put("/resource/**", "anon");
        filterChainDefinitionMap.put("/install", "anon");
        filterChainDefinitionMap.put("/hello", "anon");
        filterChainDefinitionMap.put("/**", "jwt");

        Map<String, String> permissions = new LinkedHashMap<>();
        permissions.put("/user/find", "perms[user:find]");
        filterChainDefinitionMap.putAll(permissions);
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
    }
}
