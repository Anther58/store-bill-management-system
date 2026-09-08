package com.example.billdemo.controller;


import com.example.billdemo.entity.User;
import com.example.billdemo.mapper.UserMapper;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")

public class UserController {


    private final UserMapper userMapper;


    public UserController(UserMapper userMapper){

        this.userMapper=userMapper;

    }



    // 注册
    @PostMapping("/register")
    public String register(@RequestBody User user){


        User old =
                userMapper.findByUsername(
                        user.getUsername()
                );


        if(old!=null){

            return "用户名已经存在";

        }


        userMapper.register(user);


        return "注册成功";


    }




    // 登录
    @PostMapping("/login")
    public Object login(
            @RequestBody User user,
            HttpSession session
    ){


        User dbUser =
                userMapper.findByUsername(
                        user.getUsername()
                );



        if(dbUser==null){

            return "用户不存在";

        }



        if(!dbUser.getPassword()
                .equals(user.getPassword())){


            return "密码错误";

        }



        // 登录成功，保存用户ID
        session.setAttribute("userId", dbUser.getId());

        System.out.println("login sessionId = " + session.getId());

        System.out.println("登录成功 userId = " + session.getAttribute("userId"));

        return dbUser;


    }



    //退出登录
    @GetMapping("/logout")
    public String logout(
            HttpSession session
    ){

        session.invalidate();

        return "退出成功";

    }

    @GetMapping("/check")
    public Object check(HttpSession session) {

        System.out.println("check sessionId = " + session.getId());

        Integer userId =
                (Integer) session.getAttribute("userId");

        System.out.println("check userId = " + userId);






        if(userId==null){

            return false;

        }




        return true;

    }
    // 获取当前登录用户
    @GetMapping("/info")
    public Object info(HttpSession session){

        Integer userId =
                (Integer) session.getAttribute("userId");

        if(userId == null){

            return "未登录";

        }

        User user =
                userMapper.findById(userId);

        return user;

    }

}
