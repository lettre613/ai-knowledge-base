package com.lettre.knowledge.service;

import com.lettre.knowledge.entity.User;

import java.util.List;

public interface UserService {


    User getUser(Long id);


    List<User> getAll();


    void add(User user);


    void update(User user);


    void delete(Long id);

}