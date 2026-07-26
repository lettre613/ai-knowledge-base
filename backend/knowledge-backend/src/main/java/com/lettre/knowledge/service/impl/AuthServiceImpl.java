package com.lettre.knowledge.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lettre.knowledge.entity.User;
import com.lettre.knowledge.exception.BusinessException;
import com.lettre.knowledge.mapper.UserMapper;
import com.lettre.knowledge.service.AuthService;
import com.lettre.knowledge.util.JwtUtil;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;



@Service
public class AuthServiceImpl implements AuthService {


    private final UserMapper userMapper;


    private final BCryptPasswordEncoder passwordEncoder;


    private final JwtUtil jwtUtil;



    public AuthServiceImpl(
            UserMapper userMapper,
            BCryptPasswordEncoder passwordEncoder,
            JwtUtil jwtUtil
    ){

        this.userMapper = userMapper;

        this.passwordEncoder = passwordEncoder;

        this.jwtUtil = jwtUtil;

    }




    @Override
    public void register(
            String username,
            String password,
            String email
    ){


        // 1. 查询用户名是否存在

        User existUser = userMapper.selectOne(
                new LambdaQueryWrapper<User>()
                        .eq(User::getUsername, username)
        );


        if(existUser != null){

            throw new BusinessException(
                    40001,
                    "用户名已经存在"
            );
        
        }



        // 2. 创建用户对象

        User user = new User();


        user.setUsername(username);



        // 3. BCrypt密码加密

        user.setPassword(
                passwordEncoder.encode(password)
        );


        user.setEmail(email);



        // 4. 保存数据库

        userMapper.insert(user);


    }


    @Override
    public String login(String username, String password) {

        User user = userMapper.selectOne(
                new LambdaQueryWrapper<User>()
                        .eq(User::getUsername, username)
        );

        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {

            throw new BusinessException(
                    40002,
                    "用户名或密码错误"
            );

        }

        return jwtUtil.generateToken(user.getId(), user.getUsername());

    }


}
