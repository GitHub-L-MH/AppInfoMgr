package com.app.controller;

import com.app.service.DevUserService;
import com.app.vo.BaseResult;
import com.app.vo.CurStatus;
import com.app.vo.HTTPStatus;
import com.app.vo.JSONData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * 开发者平台登陆
 */
@Controller
@RequestMapping("/devLogin")
public class DevLoginController extends BaseController {

    @Autowired(required = false)
    private DevUserService devUserService;

    private JSONData jsonData = new JSONData();

    @RequestMapping(value = "/login.action")
    public BaseResult login(@RequestParam(value = "devCode") String devCode,
                            @RequestParam(value = "devPwd") String devPwd,
                            HttpSession session){
        Map<String,Object> mapParams = new HashMap<>();
        mapParams.put("devCode",devCode);
        if (devUserService.getDevUserCount(mapParams) != 1){
            jsonData.setStatus(String.valueOf(CurStatus.USERNAME_ERROR));
            jsonData.setMessage("请检查用户名");
            return buildSuccessResultInfo(jsonData);
        }
        mapParams.put("devPassword",devPwd);
        if (devUserService.getDevUserCount(mapParams) != 1){
            return buildFailedResultInfo(HTTPStatus.ACCEPTED,"请检查密码",String.valueOf(CurStatus.USERPWD_ERROR));
        } else {
            session.setAttribute("devUser",devUserService.getDevUserByMap(mapParams));
            return buildSuccessResultInfo(null);
        }
    }
}
