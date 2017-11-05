package com.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/appVersion")
public class AppVersionController {

    @RequestMapping("/appversionmodify")
    public String appVersionModify(){
        return "developer/appversionmodify";
    }
}
