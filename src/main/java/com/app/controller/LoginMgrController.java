package com.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/AppInfoSystem")
public class LoginMgrController {
    @RequestMapping(value = "/dev/login")
    public String sendIndex(){
        return "devlogin";
    }


}
