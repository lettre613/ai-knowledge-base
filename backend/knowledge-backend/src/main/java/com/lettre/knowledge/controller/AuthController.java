package com.lettre.knowledge.controller;


import com.lettre.knowledge.common.Result;
import com.lettre.knowledge.dto.CurrentUserResponse;
import com.lettre.knowledge.dto.LoginResponse;
import com.lettre.knowledge.dto.UserLoginRequest;
import com.lettre.knowledge.dto.UserRegisterRequest;
import com.lettre.knowledge.security.LoginUser;
import com.lettre.knowledge.service.AuthService;
import com.lettre.knowledge.util.SecurityUtil;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/auth")
public class AuthController {


    private final AuthService authService;


    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    @PostMapping("/register")
    public Result<String> register(
            @Valid
            @RequestBody UserRegisterRequest request
    ){

        authService.register(
                request.getUsername(),
                request.getPassword(),
                request.getEmail()
        );

        return Result.success("注册成功");

    }



    @PostMapping("/login")
    public Result<LoginResponse> login(
            @Valid
            @RequestBody UserLoginRequest request
    ){

        String token = authService.login(
                request.getUsername(),
                request.getPassword()
        );

        return Result.success(new LoginResponse(token));

    }


    @GetMapping("/me")
    public Result<CurrentUserResponse> me() {

        LoginUser loginUser = SecurityUtil.requireLoginUser();

        return Result.success(
                new CurrentUserResponse(
                        loginUser.getUserId(),
                        loginUser.getUsername()
                )
        );

    }


}
