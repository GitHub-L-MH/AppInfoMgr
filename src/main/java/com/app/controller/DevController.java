package com.app.controller;

import com.app.pojo.AppCategory;
import com.app.pojo.DataDictionary;
import com.app.pojo.DevUser;
import com.app.service.AppCategoryService;
import com.app.service.DataDictionaryService;
import com.app.service.DevUserService;
import com.sun.org.apache.xpath.internal.operations.Mod;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/dev")
public class DevController {

    @Autowired
    private DevUserService devUserService;
    @Autowired
    private DataDictionaryService dataDictionaryService;
    @Autowired
    private AppCategoryService appCategoryService;

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

    @RequestMapping(value = "/flatform/app/list")
    public String sendAppInfoList(Model model,
                                  @RequestParam(value = "querySoftwareName",required = false) String querySoftwareName,
                                  @RequestParam(value = "queryStatus",required = false) Integer queryStatus,
                                  @RequestParam(value = "queryFlatformId",required = false) Integer queryFlatformId,
                                  @RequestParam(value = "queryCategoryLevel1",required = false) Integer queryCategoryLevel1,
                                  @RequestParam(value = "queryCategoryLevel2",required = false) Integer queryCategoryLevel2,
                                  @RequestParam(value = "queryCategoryLevel3",required = false) Integer queryCategoryLevel3,
                                  @RequestParam(value = "currPageNo",required = false) Integer currPageNo){
        if (currPageNo == null || currPageNo <= 0){
            currPageNo = 1;
        }
        DataDictionary dataDictionary = new DataDictionary();
        dataDictionary.setTypeCode("APP_STATUS");
        List<DataDictionary>  statusList = dataDictionaryService.getDataDictionaryListByObj(dataDictionary);
        dataDictionary.setTypeCode("APP_FLATFORM");
        List<DataDictionary>  flatFormList = dataDictionaryService.getDataDictionaryListByObj(dataDictionary);
        List<AppCategory> categoryLevel1List = appCategoryService.getCategoryLevel1List();
        model.addAttribute("statusList",statusList);
        model.addAttribute("flatFormList",flatFormList);
        model.addAttribute("categoryLevel1List",categoryLevel1List);
        return "developer/appinfolist";
    }

    @RequestMapping(value = "categorylevellist.json")
    @ResponseBody
    public List<AppCategory> categorylevellist(@RequestParam(value = "pid") Integer id){
        AppCategory appCategory = new AppCategory();
        appCategory.setParentId(id);
        List<AppCategory> appCategoryList = appCategoryService.getAppCategoryListByObj(appCategory);
        return appCategoryList;
    }
}
