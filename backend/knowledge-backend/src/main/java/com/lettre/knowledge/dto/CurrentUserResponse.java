package com.lettre.knowledge.dto;


public class CurrentUserResponse {


    private Long userId;

    private String username;


    public CurrentUserResponse(Long userId, String username) {
        this.userId = userId;
        this.username = username;
    }


    public Long getUserId() {
        return userId;
    }


    public void setUserId(Long userId) {
        this.userId = userId;
    }


    public String getUsername() {
        return username;
    }


    public void setUsername(String username) {
        this.username = username;
    }


}
