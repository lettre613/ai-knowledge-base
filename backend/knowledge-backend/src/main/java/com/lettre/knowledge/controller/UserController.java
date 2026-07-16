package com.lettre.knowledge.controller;


import java.util.List;
import com.lettre.knowledge.entity.User;
import com.lettre.knowledge.service.UserService;

import jakarta.annotation.Resource;

import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
public class UserController {


    @Resource
    private UserService userService;


    @GetMapping("/{id}")
    public User getUser(
            @PathVariable Long id
    ){

        return userService.getUser(id);

    }


    @GetMapping
    public List<User> list(){

        return userService.getAll();

    }


    @PostMapping
    public String add(
            @RequestBody User user
    ){

        userService.add(user);

        return "success";

    }

    @PutMapping
    public String update(
        @RequestBody User user
    ){

    userService.update(user);

    return "success";

    }

    @DeleteMapping("/{id}")
   public String delete(
        @PathVariable Long id
    ){

    userService.delete(id);

    return "success";

    }

}