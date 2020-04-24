package com.lhd.mongodb.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Created by lihongde on 2018/06/15.
 */
@Data
@Document(collection = "user")
public class User {

    @Id
    private Long id;

    @Field("name")
    private String name;

    @Field("age")
    private Integer age;

    public User(Long id, String name, Integer age){
        this.id = id;
        this.name = name;
        this.age = age;
    }
}
