package com.lhd.shiro.repository;

import com.lhd.shiro.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by xiaomi on 2019/07/21
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
