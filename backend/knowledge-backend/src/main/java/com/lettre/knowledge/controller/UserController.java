package com.lettre.knowledge.controller;


import java.util.List;
import com.lettre.knowledge.entity.User;
import com.lettre.knowledge.service.UserService;

import jakarta.annotation.Resource;

import org.springframework.web.bind.annotation.*;
import com.lettre.knowledge.common.Result;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;


@RestController
@RequestMapping("/user")
public class UserController {


    @Resource
    private UserService userService;


    @GetMapping("/{id}")
public Result<User> getById(
        @PathVariable Long id
){

    return Result.success(
            userService.getById(id)
    );

}


    @GetMapping
    public Result<List<User>> list(){
    
        return Result.success(
                userService.list()
        );
    
    }

    @GetMapping("/page")
public Result<Page<User>> page(
        @RequestParam(defaultValue = "1") long page,
        @RequestParam(defaultValue = "10") long size
){

    return Result.success(
            userService.page(page,size)
    );

}


    @PostMapping
public Result<String> add(
        @RequestBody User user
){

    userService.add(user);

    return Result.success(
            "新增成功"
    );

}

@PutMapping
public Result<String> update(
        @RequestBody User user
){

    userService.update(user);

    return Result.success(
            "修改成功"
    );

}

@DeleteMapping("/{id}")
public Result<String> delete(
        @PathVariable Long id
){

    userService.delete(id);

    return Result.success(
            "删除成功"
    );

}

}