package com.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/devSystem")
public class SkipController {
    @RequestMapping("/devHome")
    public String sendDevHome(){
        return "/developer/devhome";
    }
}
