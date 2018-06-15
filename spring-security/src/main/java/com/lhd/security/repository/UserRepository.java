package com.lhd.security.repository;

import com.lhd.security.domain.User;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Created by lihongde on 2018/06/15.
 */
@RepositoryRestResource
@CacheConfig(cacheNames = "users")
public interface  UserRepository extends JpaRepository<User,String> {

    @Cacheable()
    User findByUsername(final String username);

    @Cacheable()
    User findByAccount(final String account);
}
