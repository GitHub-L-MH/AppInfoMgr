package com.app.controller;

import com.app.pojo.*;
import com.app.service.*;
import com.app.vo.AppInfoEx;
import com.app.vo.Page;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.Appinfo;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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
    private DataDictionaryService dataDictionaryService;
    @Autowired
    private AppCategoryService appCategoryService;
    @Autowired
    private AppInfoService appInfoService;
    @Autowired
    private AppVersionService appVersionService;

    @RequestMapping(value = "/logout")
    public String logout(HttpSession session){
        //清除session实现用户推退出
        session.removeAttribute("devUserSession");
        return "redirect:/index.jsp";
    }


    /**
     * 转向AppInfoList页面，根据条件查询
     * @param model
     * @param querySoftwareName
     * @param queryStatus
     * @param queryFlatformId
     * @param queryCategoryLevel1
     * @param queryCategoryLevel2
     * @param queryCategoryLevel3
     * @param currPageNo
     * @return
     */
    @RequestMapping(value = "/flatform/app/list")
    public String sendAppInfoList(Model model,
                                  @RequestParam(value = "querySoftwareName",required = false) String querySoftwareName,
                                  @RequestParam(value = "queryStatus",required = false) Integer queryStatus,
                                  @RequestParam(value = "queryFlatformId",required = false) Integer queryFlatformId,
                                  @RequestParam(value = "queryCategoryLevel1",required = false) Integer queryCategoryLevel1,
                                  @RequestParam(value = "queryCategoryLevel2",required = false) Integer queryCategoryLevel2,
                                  @RequestParam(value = "queryCategoryLevel3",required = false) Integer queryCategoryLevel3,
                                  @RequestParam(value = "pageIndex",required = false) Integer currPageNo){
        //分页类
        Page page = new Page();
        //设置页面大小（每页面显示几条数据）
        page.setPageSize(4);
        //参数Map（用于存储查询条件）
        Map<String,Object> paramMap = new HashMap<>();
        //条件1.软件名称，支持模糊查询
        paramMap.put("softwareName",querySoftwareName);
        //条件2.软件状态ID
        paramMap.put("status",queryStatus);
        //条件3.平台ID
        paramMap.put("flatformId",queryFlatformId);
        //条件4.一级分类ID
        paramMap.put("categoryLevel1",queryCategoryLevel1);
        //条件5.二级分类ID
        paramMap.put("categoryLevel2",queryCategoryLevel2);
        //条件6.三级分类ID
        paramMap.put("categoryLevel3",queryCategoryLevel3);
        //根据Map条件查询有几条符合的数据
        page.setTotalCount(appInfoService.getAppInfoCount(paramMap));
        //设置当前页如果小于等于0，值为1，（防止出现sql异常）
        if (currPageNo == null || currPageNo <= 0){
            currPageNo = 1;
        }
        //设置当前页如果小于等于0，值为1，（防止出现sql异常）
        page.setCurrentPageNo(currPageNo);
        if (page.getCurrentPageNo() <= 0){
            page.setCurrentPageNo(1);
        }

        //设置数据偏移数量
        paramMap.put("offSet",(page.getCurrentPageNo()-1)*page.getPageSize());
        //设置查询出几条数据
        paramMap.put("pageSize",page.getPageSize());
        //根据上述条件进行查询，返回值为扩展类List集合,(原因：多表连接查询)
        List<AppInfoEx> appInfoExList = appInfoService.getAppInfoExPageByMap(paramMap);
        //数据字典实体类，用于存放条件
        DataDictionary dataDictionary = new DataDictionary();
        //设置TypeCode的值
        dataDictionary.setTypeCode("APP_STATUS");
        //查询TypeCode列为“APP_STATUS”值的数据
        List<DataDictionary>  statusList = dataDictionaryService.getDataDictionaryListByObj(dataDictionary);
        //设置TypeCode的值
        dataDictionary.setTypeCode("APP_FLATFORM");
        //查询TypeCode列为“APP_FLATFORM”值的数据
        List<DataDictionary>  flatFormList = dataDictionaryService.getDataDictionaryListByObj(dataDictionary);
        //查询一级菜单
        List<AppCategory> categoryLevel1List = appCategoryService.getCategoryLevel1List();

        //app类别实体类（用于保存条件）
        AppCategory appCategory = new AppCategory();
        if (queryCategoryLevel1 != null){
            appCategory.setParentId(queryCategoryLevel1);
            List<AppCategory> categoryLevel2List = appCategoryService.getAppCategoryListByObj(appCategory);
            model.addAttribute("categoryLevel2List",categoryLevel2List);
        }
        if (queryCategoryLevel2 != null){
            appCategory.setParentId(queryCategoryLevel2);
            List<AppCategory> categoryLevel3List = appCategoryService.getAppCategoryListByObj(appCategory);
            model.addAttribute("categoryLevel3List",categoryLevel3List);
        }
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

    /**
     * 异步查询二级和三级分类
     * @param id
     * @return
     */
    @RequestMapping(value = "/categorylevellist.json")
    @ResponseBody
    public List<AppCategory> categorylevellist(@RequestParam(value = "pid",required = false) Integer id){
        //如果没有id表示查询一级分类
        if (id == null){
            return appCategoryService.getCategoryLevel1List();
        }
        //app分类实体类
        AppCategory appCategory = new AppCategory();
        //存储条件，父级ID
        appCategory.setParentId(id);
        //查询结果
        List<AppCategory> appCategoryList = appCategoryService.getAppCategoryListByObj(appCategory);
        return appCategoryList;
    }

    /**
     * 转向appInfoAdd页面
     * @return
     */
    @RequestMapping("/flatform/app/appinfoadd")
    public String sendAppInfoAdd(){
        return "developer/appinfoadd";
    }

    /**
     * 异步查询平台信息
     * @param tcode
     * @return
     */
    @RequestMapping(value = "datadictionarylist.json")
    @ResponseBody
    public List<DataDictionary> DataDictionarylist(@RequestParam(value = "tcode") String tcode){
        //数据字典类
        DataDictionary dataDictionary = new DataDictionary();
        //传入参数在数据字典中提取平台集合
        dataDictionary.setTypeCode(tcode);
        List<DataDictionary>  flatFormList = dataDictionaryService.getDataDictionaryListByObj(dataDictionary);
        return flatFormList;
    }

    /**
     * 异步查询APKName是否重复
     * @param APKName
     * @return
     */
    @RequestMapping(value = "/apkexist.json")
    @ResponseBody
    public Map<String,Object> apkExist(@RequestParam(value = "APKName") String APKName){
        //结果Map
        Map<String,Object> resultMap = new HashMap<>();
        //参数Map
        Map<String,Object> paramMap = new HashMap<>();
        //如果APKName是空或者是“”返回empty
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


    /**
     * 添加App基本信息
     * @param a_logoPicPath
     * @param softwareName
     * @param APKName
     * @param supportROM
     * @param interfaceLanguage
     * @param softwareSize
     * @param downloads
     * @param flatformId
     * @param categoryLevel1
     * @param categoryLevel2
     * @param categoryLevel3
     * @param status
     * @param appInfo
     * @param model
     * @param request
     * @return
     */
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
                             HttpSession session){
        DevUser devUser = (DevUser) session.getAttribute("devUserSession");
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
        appInfo1.setCreatedBy(devUser.getId());
        appInfo1.setCreationDate(new Date());
        appInfo1.setDevId(devUser.getId());
        Map<String,String> resultMap = fileUpload(a_logoPicPath,request,model,appInfo1.getAPKName());
        if (resultMap.get("status") == "200"){
            appInfo1.setLogoPicPath(resultMap.get("fileName"));
            appInfo1.setLogoLocPath(resultMap.get("filePath"));
        } else {
            return "developer/appinfoadd";
        }
        if (appInfoService.add(appInfo1) == 1){

            return "redirect:/dev/flatform/app/list";
        } else {
            return "developer/appinfoadd";
        }

    }

    /**
     * 文件上传方法
     * @param attach
     * @param request
     * @param model
     * @return
     */
    private Map<String,String> fileUpload(MultipartFile attach, HttpServletRequest request,Model model,String logoName){
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
                String fileName = logoName+".jpg";
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

    /**
     * 转向appinfomodify页面，并根据appID查询数据显示
     * @param id
     * @param model
     * @return
     */
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

    /**
     * 更新app基础信息
     * @param id
     * @param softwareName
     * @param APKName
     * @param supportROM
     * @param interfaceLanguage
     * @param softwareSize
     * @param downloads
     * @param flatformId
     * @param categoryLevel1
     * @param categoryLevel2
     * @param categoryLevel3
     * @param appInfo
     * @param logoLocPath
     * @param logoPicPath
     * @param attach
     * @param status
     * @param response
     * @param request
     * @param model
     * @return
     */
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
                                    HttpSession session,
                                    HttpServletRequest request,
                                    Model model){
        DevUser devUser = (DevUser) session.getAttribute("devUserSession");
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
        appInfo1.setModifyBy(devUser.getId());
        appInfo1.setModifyDate(new Date());
        appInfo1.setUpdateDate(new Date());
        appInfo1.setStatus(status);
        appInfo1.setAppInfo(appInfo);
        if (!attach.isEmpty()){
            Map<String,String> resultMap = fileUpload(attach,request,model,appInfo1.getAPKName());
            if (resultMap.get("status") == "200"){
                appInfo1.setLogoPicPath(resultMap.get("fileName"));
                appInfo1.setLogoLocPath(resultMap.get("filePath"));
            } else {
                Map<String,Object> paramMap = new HashMap<>();
                paramMap.put("id",id);
                paramMap.put("offSet",0);
                paramMap.put("pageSize",5);
                AppInfoEx resultAppInfo = appInfoService.getAppInfoExPageByMap(paramMap).get(0);
                model.addAttribute("appInfo",resultAppInfo);
                return "developer/appinfomodify";
            }
        }
        if (appInfoService.update(appInfo1) == 1){
            return "redirect:/dev/flatform/app/list";
        }
        return "developer/appinfomodify";
    }

    /**
     * 异步删除logo图片文件
     * @param id
     * @return
     */
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

    /**
     * 删除app基本信息，同时删除该app下所有的版本信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/delapp.json")
    @ResponseBody
    public Map<String,Object> delApp(@RequestParam(value = "id") Integer id){
        Map<String,Object> resultMap = new HashMap<>();
        AppVersion appVersion = new AppVersion();
        appVersion.setAppId(id);
        List<AppVersion> appVersionList = appVersionService.getAppVersionListByObj(appVersion);
        for (AppVersion version : appVersionList) {
            File file = new File(version.getApkLocPath());
            if (file.delete()){
                if (appVersionService.deleteAppVersionById(version.getId()) != 1) {
                    resultMap.put("delResult","false");
                }
            }
        }
        if (appVersionService.getAppVersionListByObj(appVersion).size() == 0){
            File file = new File(appInfoService.getAppInfoById(id).getLogoLocPath());
            if (file.delete()){
                if (appInfoService.deleteAppInfoById(id) == 1) {
                    resultMap.put("delResult","true");
                } else {
                    resultMap.put("delResult","notexist");
                }
            }

        }
        return resultMap;
    }

    /**
     * 上架操作
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}/sale.json",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> saleSwitch(@PathVariable(value = "id") Integer id){
        Map<String,Object> resultMap = new HashMap<>();
        AppInfo appInfo = appInfoService.getAppInfoById(id);
        AppInfo appInfoParam = new AppInfo();
        if (appInfo.getStatus() == 4){
            appInfoParam.setId(id);
            appInfoParam.setStatus(5);
            appInfoParam.setOffSaleDate(new Date());
        } else if (appInfo.getStatus() == 5){
            appInfoParam.setId(id);
            appInfoParam.setStatus(4);
            appInfoParam.setOnSaleDate(new Date());
        }
        if (appInfoService.update(appInfoParam) == 1){
            resultMap.put("errorCode","0");
            resultMap.put("resultMsg","success");
        } else {
            resultMap.put("errorCode","0");
            resultMap.put("resultMsg","failed");
        }
        return resultMap;
    }
}
