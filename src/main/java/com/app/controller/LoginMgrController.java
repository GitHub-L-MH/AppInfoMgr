package com.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginMgrController {
    @RequestMapping(value = "/AppInfoSystem")
    public String sendIndex(){
        return "index";
    }
}
