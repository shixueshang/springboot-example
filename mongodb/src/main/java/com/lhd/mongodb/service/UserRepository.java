package com.lhd.mongodb.service;

import com.lhd.mongodb.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by lihongde on 2018/06/15.
 */
public interface UserRepository extends MongoRepository<User, Long> {

    User findByName(String name);
}
