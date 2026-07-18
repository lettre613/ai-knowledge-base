package com.lettre.knowledge.service;


import com.lettre.knowledge.entity.User;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;


public interface UserService {


    User getById(Long id);


    List<User> list();


    Page<User> page(
            long current,
            long size
    );


    void add(User user);


    void update(User user);


    void delete(Long id);

}