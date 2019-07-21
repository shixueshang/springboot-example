package com.lhd.shiro.entity;


import lombok.Data;

import javax.persistence.*;

/**
 * Created by xiaomi on 2019/05/19
 */
@Data
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name", nullable = false, unique = true)
    private String userName;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String salt;

    @Column(nullable = false)
    private String permission;

    @Column(nullable = false)
    private boolean locked;

    public User(String userName){
        this.userName = userName;
    }

    public User(String userName, String password, String permission, boolean locked){
        this.userName = userName;
        this.password = password;
        this.permission = permission;
        this.locked = locked;
    }

}
