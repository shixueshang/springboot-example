package com.lhd.shiro.entity;


/**
 * Created by xiaomi on 2019/05/19
 */
public class User {

    private Long id;

    private String userName;

    private String password;

    private String permission;

    private boolean locked;

    public User(Long id, String userName, String password, String permission, boolean locked){
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.permission = permission;
        this.locked = locked;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }
}
