package com.example.billdemo.entity;


import java.util.Date;


public class User {


    private Integer id;

    private String username;

    private String password;

    private String nickname;

    private Date createTime;



    public Integer getId() {
        return id;
    }


    public void setId(Integer id) {
        this.id = id;
    }


    public String getUsername() {
        return username;
    }


    public void setUsername(String username) {
        this.username=username;
    }


    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password=password;
    }


    public String getNickname() {
        return nickname;
    }


    public void setNickname(String nickname) {
        this.nickname=nickname;
    }


    public Date getCreateTime() {
        return createTime;
    }


    public void setCreateTime(Date createTime) {
        this.createTime=createTime;
    }

}
