package com.app.controller;

import com.app.pojo.AppCategory;
import com.app.pojo.AppInfo;
import com.app.pojo.DataDictionary;
import com.app.pojo.DevUser;
import com.app.service.AppCategoryService;
import com.app.service.AppInfoService;
import com.app.service.DataDictionaryService;
import com.app.service.DevUserService;
import com.app.vo.AppInfoEx;
import com.app.vo.Page;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.Appinfo;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Date;
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
    @Autowired
    private AppInfoService appInfoService;

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
                                  @RequestParam(value = "pageIndex",required = false) Integer currPageNo){
        Page page = new Page();
        page.setPageSize(4);
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("softwareName",querySoftwareName);
        paramMap.put("status",queryStatus);
        paramMap.put("flatformId",queryFlatformId);
        paramMap.put("categoryLevel1",queryCategoryLevel1);
        paramMap.put("categoryLevel2",queryCategoryLevel2);
        paramMap.put("categoryLevel3",queryCategoryLevel3);
        page.setTotalCount(appInfoService.getAppInfoCount(paramMap));
        if (currPageNo == null || currPageNo <= 0){
            currPageNo = 1;
        }
        page.setCurrentPageNo(currPageNo);
        if (page.getCurrentPageNo() <= 0){
            page.setCurrentPageNo(1);
        }
        paramMap.put("offSet",(page.getCurrentPageNo()-1)*page.getPageSize());
        paramMap.put("pageSize",page.getPageSize());
        List<AppInfoEx> appInfoExList = appInfoService.getAppInfoExPageByMap(paramMap);

        DataDictionary dataDictionary = new DataDictionary();
        dataDictionary.setTypeCode("APP_STATUS");
        List<DataDictionary>  statusList = dataDictionaryService.getDataDictionaryListByObj(dataDictionary);
        dataDictionary.setTypeCode("APP_FLATFORM");
        List<DataDictionary>  flatFormList = dataDictionaryService.getDataDictionaryListByObj(dataDictionary);
        List<AppCategory> categoryLevel1List = appCategoryService.getCategoryLevel1List();
        model.addAttribute("statusList",statusList);
        model.addAttribute("flatFormList",flatFormList);
        model.addAttribute("categoryLevel1List",categoryLevel1List);
        model.addAttribute("appInfoList",appInfoExList);
        model.addAttribute("querySoftwareName",querySoftwareName);
        model.addAttribute("queryStatus",queryStatus);
        model.addAttribute("queryFlatformId",queryFlatformId);
        model.addAttribute("queryCategoryLevel1",queryCategoryLevel1);
        model.addAttribute("queryCategoryLevel2",queryCategoryLevel2);
        model.addAttribute("queryCategoryLevel3",queryCategoryLevel3);
        System.out.println(page.toString());
        model.addAttribute("pages",page);

                return "developer/appinfolist";
    }

    @RequestMapping(value = "/categorylevellist.json")
    @ResponseBody
    public List<AppCategory> categorylevellist(@RequestParam(value = "pid",required = false) Integer id){
        if (id == null){
            return appCategoryService.getCategoryLevel1List();
        }
        AppCategory appCategory = new AppCategory();
        appCategory.setParentId(id);
        List<AppCategory> appCategoryList = appCategoryService.getAppCategoryListByObj(appCategory);
        return appCategoryList;
    }

    @RequestMapping("/flatform/app/appinfoadd")
    public String sendAppInfoAdd(){
        return "developer/appinfoadd";
    }

    @RequestMapping(value = "datadictionarylist.json")
    @ResponseBody
    public List<DataDictionary> DataDictionarylist(@RequestParam(value = "tcode") String tcode){
        DataDictionary dataDictionary = new DataDictionary();
        dataDictionary.setTypeCode(tcode);
        List<DataDictionary>  flatFormList = dataDictionaryService.getDataDictionaryListByObj(dataDictionary);
        return flatFormList;
    }

    @RequestMapping(value = "/apkexist.json")
    @ResponseBody
    public Map<String,Object> apkExist(@RequestParam(value = "APKName") String APKName){
        Map<String,Object> resultMap = new HashMap<>();
        Map<String,Object> paramMap = new HashMap<>();
        if (APKName == null || "".equals(APKName)){
            resultMap.put("APKName","empty");
        } else {
            paramMap.put("aPKName",APKName);
            int result = appInfoService.getAppInfoCount(paramMap);
            if (result >= 1){
                resultMap.put("APKName","exist");
            } else {
                resultMap.put("APKName","noexist");
            }
        }
        return resultMap;
    }


    @RequestMapping(value = "/appinfoaddsave")
    public String addAppInfo(@RequestParam(value = "a_logoPicPath") MultipartFile a_logoPicPath,
                             @RequestParam(value = "softwareName",required = false) String softwareName,
                             @RequestParam(value = "APKName") String APKName,
                             @RequestParam(value = "supportROM") String supportROM,
                             @RequestParam(value = "interfaceLanguage") String interfaceLanguage,
                             @RequestParam(value = "softwareSize") double softwareSize,
                             @RequestParam(value = "downloads") Integer downloads,
                             @RequestParam(value = "flatformId") Integer flatformId,
                             @RequestParam(value = "categoryLevel1") Integer categoryLevel1,
                             @RequestParam(value = "categoryLevel2") Integer categoryLevel2,
                             @RequestParam(value = "categoryLevel3") Integer categoryLevel3,
                             @RequestParam(value = "status") Integer status,
                             @RequestParam(value = "appInfo") String appInfo,

                             Model model,
                             HttpServletRequest request,
                             HttpServletResponse response){
        AppInfo appInfo1 = new AppInfo();
        appInfo1.setId(0);
        appInfo1.setSoftwareName(softwareName);
        appInfo1.setAPKName(APKName);
        appInfo1.setSupportROM(supportROM);
        appInfo1.setInterfaceLanguage(interfaceLanguage);
        appInfo1.setSoftwareName(softwareName);
        appInfo1.setSoftwareSize(softwareSize);
        appInfo1.setFlatformId(flatformId);
        appInfo1.setDownloads(downloads);
        appInfo1.setCategoryLevel1(categoryLevel1);
        appInfo1.setCategoryLevel2(categoryLevel2);
        appInfo1.setCategoryLevel3(categoryLevel3);
        appInfo1.setStatus(status);
        appInfo1.setAppInfo(appInfo);
        Map<String,String> resultMap = fileUpload(a_logoPicPath,request,model);
        if (resultMap.get("status") == "200"){
            appInfo1.setLogoPicPath(resultMap.get("fileName"));
            appInfo1.setLogoLocPath(resultMap.get("filePath"));
        } else {
            return "appinfoadd";
        }
        if (appInfoService.add(appInfo1) == 1){

            return "redirect:/dev/flatform/app/list";
        } else {
            return "appinfoadd";
        }

    }

    /**
     * 文件上传方法
     * @param attach
     * @param request
     * @param model
     * @return
     */
    private Map<String,String> fileUpload(MultipartFile attach, HttpServletRequest request,Model model){
        Map<String,String> resultMap = new HashMap<>();
        String idPicPath = null;
        if (!attach.isEmpty()){
            String uri = "H:\\Workspaces\\SSM\\AppInfoMgr\\src\\main\\webapp\\statics\\uploadfiles";
            String path = request.getSession().getServletContext().getRealPath("statics"+ File.separator + "uploadfiles");
            String oldFileName = attach.getOriginalFilename();//原文件名
            //源文件后缀名
            String prefix = FilenameUtils.getExtension(oldFileName);
            //限制文件大小
            int filesize = 51200;
            //判断文件大小是否超过限制
            if (attach.getSize() > filesize){
                model.addAttribute("fileUploadError","文件不能超过50kb");
                resultMap.put("status","201");
            }else if (prefix.equalsIgnoreCase("jpg")
                    || prefix.equalsIgnoreCase("png")
                    || prefix.equalsIgnoreCase("jpeg")){//判断文件格式是否正确
                //随机生成文件名
                String fileName = System.currentTimeMillis()+ RandomUtils.nextInt(1000000)+"_Personal.jpg";
                resultMap.put("fileName","/statics/uploadfiles/"+fileName);
                //创建目标文件
                File targetFile = new File(uri,fileName);
                File targetFile2 = new File(path,fileName);
                //检查目标路径是否存在
                if (!targetFile.exists()){
                    targetFile.mkdirs();
                }

                if (!targetFile2.exists()){
                    targetFile2.mkdirs();
                }
                try {
                    //保存文件
                    attach.transferTo(targetFile);
                    attach.transferTo(targetFile2);
                    resultMap.put("status","200");
                } catch (IOException e) {
                    e.printStackTrace();
                    model.addAttribute("fileUploadError","上传失败");
                    resultMap.put("status","202");
                }
                idPicPath = uri+File.separator+fileName;
                resultMap.put("filePath",idPicPath);

            }else{
                model.addAttribute("fileUploadError","上传文件格式不正确");
                resultMap.put("status","203");
            }
        }
        return resultMap;
    }

    @RequestMapping("/appinfomodify")
    public String appInfoModify(@RequestParam("id") Integer id,
                                Model model){
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("id",id);
        paramMap.put("offSet",0);
        paramMap.put("pageSize",5);
        AppInfoEx appInfo = appInfoService.getAppInfoExPageByMap(paramMap).get(0);
        model.addAttribute("appInfo",appInfo);
        return "developer/appinfomodify";
    }

    @RequestMapping("/appinfomodifysave")
    public String appInfoModifySave(@RequestParam(value = "id") Integer id,
                                    @RequestParam(value = "softwareName") String softwareName,
                                    @RequestParam(value = "APKName") String APKName,
                                    @RequestParam(value = "supportROM") String supportROM,
                                    @RequestParam(value = "interfaceLanguage") String interfaceLanguage,
                                    @RequestParam(value = "softwareSize") double softwareSize,
                                    @RequestParam(value = "downloads") Integer downloads,
                                    @RequestParam(value = "flatformId") Integer flatformId,
                                    @RequestParam(value = "categoryLevel1") Integer categoryLevel1,
                                    @RequestParam(value = "categoryLevel2") Integer categoryLevel2,
                                    @RequestParam(value = "categoryLevel3") Integer categoryLevel3,
                                    @RequestParam(value = "appInfo") String appInfo,
                                    @RequestParam(value = "logoLocPath") String logoLocPath,
                                    @RequestParam(value = "logoPicPath") String logoPicPath,
                                    @RequestParam(value = "attach") MultipartFile attach,
                                    @RequestParam(value = "status") Integer status,
                                    HttpServletResponse response,
                                    HttpServletRequest request,
                                    Model model){
        AppInfo appInfo1 = new AppInfo();
        appInfo1.setId(id);
        appInfo1.setSoftwareName(softwareName);
        appInfo1.setAPKName(APKName);
        appInfo1.setSupportROM(supportROM);
        appInfo1.setInterfaceLanguage(interfaceLanguage);
        appInfo1.setSoftwareName(softwareName);
        appInfo1.setSoftwareSize(softwareSize);
        appInfo1.setFlatformId(flatformId);
        appInfo1.setDownloads(downloads);
        appInfo1.setCategoryLevel1(categoryLevel1);
        appInfo1.setCategoryLevel2(categoryLevel2);
        appInfo1.setCategoryLevel3(categoryLevel3);
        appInfo1.setUpdateDate(new Date());
        appInfo1.setStatus(status);
        appInfo1.setAppInfo(appInfo);
        if (!attach.isEmpty()){
            Map<String,String> resultMap = fileUpload(attach,request,model);
            if (resultMap.get("status") == "200"){
                appInfo1.setLogoPicPath(resultMap.get("fileName"));
                appInfo1.setLogoLocPath(resultMap.get("filePath"));
            } else {
                return "developer/appinfomodify";
            }
        }
        if (appInfoService.update(appInfo1) == 1){
            return "redirect:/dev/flatform/app/list";
        }
        return "developer/appinfomodify";
    }

    @RequestMapping("/delfile.json")
    @ResponseBody
    public Map<String,Object> delFile(@RequestParam(value = "id") Integer id){
        Map<String,Object> resultMap = new HashMap<>();
        AppInfo appInfo = appInfoService.getAppInfoById(id);
        File picFile = new File(appInfo.getLogoLocPath());
        if (picFile.delete()){
            resultMap.put("result","success");
        } else {
            resultMap.put("result","failed");
        }
        return resultMap;
    }
}
