package com.lettre.knowledge.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public class UserRegisterRequest {


    @NotBlank(message = "用户名不能为空")
    private String username;


    @NotBlank(message = "密码不能为空")
    @Size(
        min = 6,
        message = "密码长度不能少于6位"
    )
    private String password;


    private String email;



    public String getUsername() {
        return username;
    }


    public void setUsername(String username) {
        this.username = username;
    }


    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }


    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }

}