package com.lhd.security.repository;

import com.lhd.security.domain.Role;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Created by lihongde on 2018/06/15.
 */
@RepositoryRestResource
@CacheConfig(cacheNames = "roles")
public interface RoleRepository extends JpaRepository<Role,String> {

    @Cacheable()
    Role findByName(final String name);
}
