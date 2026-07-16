package com.lettre.knowledge.service.impl;


import com.lettre.knowledge.entity.User;
import com.lettre.knowledge.mapper.UserMapper;
import com.lettre.knowledge.service.UserService;

import jakarta.annotation.Resource;

import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserServiceImpl implements UserService {


    @Resource
    private UserMapper userMapper;


    @Override
    public User getUser(Long id){

        return userMapper.selectById(id);

    }


    @Override
    public List<User> getAll(){

        return userMapper.selectList(null);

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