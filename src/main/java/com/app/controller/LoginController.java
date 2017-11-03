package com.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    @RequestMapping(value = "/manager/login")
    public String sendManagerLogin(){
        return "backendlogin";
    }

    @RequestMapping(value = "/dev/login")
    public String sendDevLogin(){
        return "devlogin";
    }


}
