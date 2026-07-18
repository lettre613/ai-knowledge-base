package com.lettre.knowledge.service.impl;


import com.lettre.knowledge.entity.User;
import com.lettre.knowledge.mapper.UserMapper;
import com.lettre.knowledge.service.UserService;

import jakarta.annotation.Resource;

import org.springframework.stereotype.Service;

import java.util.List;

import com.lettre.knowledge.exception.BusinessException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;



@Service
public class UserServiceImpl implements UserService {


    @Resource
    private UserMapper userMapper;



    @Override
   public User getById(Long id){

    User user = userMapper.selectById(id);


    if(user == null){

        throw new BusinessException(
                40001,
                "用户不存在"
        );

    }


    return user;

    }


    @Override
public List<User> list(){

    return userMapper.selectList(null);

}



@Override
public Page<User> page(
        long current,
        long size
){

    Page<User> page =
            new Page<>(current,size);


    return userMapper.selectPage(
            page,
            null
    );

}


    @Override
    public void add(User user){

        userMapper.insert(user);

    }

    @Override
    public void update(User user){

    userMapper.updateById(user);

    }

    @Override
    public void delete(Long id){

    userMapper.deleteById(id);

    }

}