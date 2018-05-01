package com.jnshu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

/* 认证Controller */
@Controller
public class LoginController {
    //登陆
    @RequestMapping("/login.action")
    public String login(HttpSession session,String username,String password){

        //调用service进行用户身份认证


        //在session中保存用户身份信息
        session.setAttribute("username", username);

        //重定向到用户信息页面
        return "redirect:/userList.action";
    }

    //退出登陆
    @RequestMapping("logout")
    public String logout(HttpSession session){
        //删除session
        session.invalidate();

        return "redirect:/userList.action";
    }
}
