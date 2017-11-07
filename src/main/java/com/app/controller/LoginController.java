package com.app.controller;

import com.app.pojo.DevUser;
import com.app.service.BackendUserService;
import com.app.service.DevUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
public class LoginController {

    @Autowired
    private DevUserService devUserService;
    @Autowired
    private BackendUserService backendUserService;

    @RequestMapping(value = "/back/login")
    public String sendManagerLogin(){
        return "backendlogin";
    }

    @RequestMapping(value = "/devlogin/login")
    public String sendDevLogin(){
        return "devlogin";
    }

    @RequestMapping(value = "/devLogin",method = RequestMethod.POST)
    public String devLogin(@RequestParam(value = "devCode") String devCode,
                           @RequestParam(value = "devPassword") String devPassword,
                           HttpSession session,
                           Model model){
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("devCode",devCode);
        if (devUserService.getDevUserCount(paramMap) != 1){
            model.addAttribute("error","请检查账户名");
            return "devlogin";
        } else {
            paramMap.put("devPassword",devPassword);
            if (devUserService.getDevUserCount(paramMap) != 1){
                model.addAttribute("error","请检查密码");
                return "devlogin";
            } else {
                DevUser devUser = devUserService.getDevUserByMap(paramMap);
                session.setAttribute("devUserSession",devUser);
                return "developer/main";
            }
        }
    }

    @RequestMapping(value = "/back/doLogin")
    public String backLogin(@RequestParam(value = "userCode") String userCode,
                            @RequestParam(value = "userPassword") String userPassword,
                            Model model,
                            HttpSession session){
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("userCode",userCode);
        if (backendUserService.getBackendUserCount(paramMap) != 1){
            model.addAttribute("error","请检查账户名");
            return "backendlogin";
        } else {
            paramMap.put("userPassword",userPassword);
            if (backendUserService.getBackendUserCount(paramMap) != 1){
                model.addAttribute("error","请检查密码");
                return "devlogin";
            } else {

            }
        }
        return "";
    }
}
