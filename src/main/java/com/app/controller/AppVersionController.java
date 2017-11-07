package com.app.controller;

import com.app.pojo.AppInfo;
import com.app.pojo.AppVersion;
import com.app.pojo.DevUser;
import com.app.service.*;
import com.app.vo.AppInfoEx;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.omg.CORBA.ObjectHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/appVersion")
public class AppVersionController {

    @Autowired
    private DevUserService devUserService;
    @Autowired
    private DataDictionaryService dataDictionaryService;
    @Autowired
    private AppCategoryService appCategoryService;
    @Autowired
    private AppInfoService appInfoService;
    @Autowired
    private AppVersionService appVersionService;


    /**
     * 转到增加app版本信息
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/appversionadd")
    public String appVersionAdd(@RequestParam(value = "id") Integer id,
                                Model model){
        AppVersion appVersionParam = new AppVersion();
        appVersionParam.setAppId(id);
        List<AppVersion> appVersionList = appVersionService.getAppVersionListByObj(appVersionParam);
        model.addAttribute("appVersionList",appVersionList);
        model.addAttribute("appVersion",appVersionParam);
        return "developer/appversionadd";
    }

    @RequestMapping("/appversionmodify")
    public String appVersionModify(@RequestParam(value = "vid") Integer versionId,
                                   @RequestParam(value = "aid") Integer appId,
                                   Model model){
        AppVersion appVersionParam = new AppVersion();
        appVersionParam.setAppId(appId);
        List<AppVersion> appVersionList = appVersionService.getAppVersionListByObj(appVersionParam);
        AppVersion appVersion = appVersionService.getAppVersionById(versionId);
        model.addAttribute("appVersionList",appVersionList);
        model.addAttribute("appVersion",appVersion);
        return "developer/appversionmodify";
    }

    @RequestMapping("/addversionsave")
    public String addVersionSave(@RequestParam(value = "appId") Integer appId,
                                 @RequestParam(value = "versionNo") String versionNo,
                                 @RequestParam(value = "versionSize") double versionSize,
                                 @RequestParam(value = "publishStatus") Integer publishStatus,
                                 @RequestParam(value = "versionInfo") String versionInfo,
                                 @RequestParam(value = "a_downloadLink") MultipartFile a_downloadLink,
                                 Model model,
                                 HttpSession session,
                                 HttpServletRequest request){
        DevUser devUser = (DevUser) session.getAttribute("devUserSession");
        AppVersion appVersion = new AppVersion();
        appVersion.setId(0);
        appVersion.setAppId(appId);
        appVersion.setVersionNo(versionNo);
        appVersion.setVersionSize(versionSize);
        appVersion.setPublishStatus(publishStatus);
        appVersion.setVersionInfo(versionInfo);
        appVersion.setCreatedBy(devUser.getId());
        appVersion.setCreationDate(new Date());

        AppInfo appInfo1 = appInfoService.getAppInfoById(appId);
        String curFileName = appInfo1.getAPKName()+"-"+appVersion.getVersionNo();

        Map<String,String> resultMap = fileUpload(a_downloadLink,request,model,curFileName);
        if (resultMap.get("status") == "200"){
            appVersion.setApkFileName(resultMap.get("fileName"));
            appVersion.setDownloadLink(resultMap.get("DownloadPath"));
            appVersion.setApkLocPath(resultMap.get("filePath"));
        } else {
            return "developer/appversionadd";
        }
        if (appVersionService.add(appVersion) == 1){
            AppVersion appVersion1 = new AppVersion();
            appVersion1.setAppId(appId);
            List<AppVersion> appVersionList = appVersionService.getAppVersionListByObj(appVersion1);

            List<Integer> appVersionIds = new ArrayList<>();
            for (AppVersion version : appVersionList) {
                appVersionIds.add(version.getId());
            }
            Collections.sort(appVersionIds);

            AppInfo appInfo = new AppInfo();
            appInfo.setId(appId);

            appInfo.setVersionId(appVersionIds.get(appVersionIds.size()-1));
            if (appInfoService.update(appInfo) == 1) {
                return "redirect:/dev/flatform/app/list";
            } else {
                model.addAttribute("fileUploadError","更新版本信息失败！");
                return "developer/appversionadd";
            }
        } else {
            model.addAttribute("fileUploadError","添加失败");
            return "developer/appversionadd";
        }

    }

    /**
     * APK上传方法
     * @param attach
     * @param request
     * @param model
     * @return
     */
    private Map<String,String> fileUpload(MultipartFile attach, HttpServletRequest request, Model model,String curfileName){
        Map<String,String> resultMap = new HashMap<>();
        String idPicPath = null;
        if (!attach.isEmpty()){
            String uri = "H:\\Workspaces\\SSM\\AppInfoMgr\\src\\main\\webapp\\statics\\uploadfiles";
            String path = request.getSession().getServletContext().getRealPath("statics"+ File.separator + "uploadfiles");
            String oldFileName = attach.getOriginalFilename();//原文件名
            //源文件后缀名
            String prefix = FilenameUtils.getExtension(oldFileName);
            //限制文件大小
            int filesize = 1024*1024*500;
            //判断文件大小是否超过限制
            if (attach.getSize() > filesize){
                model.addAttribute("fileUploadError","文件不能超过500MB");
                resultMap.put("status","201");
            }else if (prefix.equalsIgnoreCase("APK")){//判断文件格式是否正确
                //随机生成文件名
                String fileName = curfileName+".apk";
                resultMap.put("DownloadPath","/statics/uploadfiles/"+fileName);
                resultMap.put("fileName",fileName);
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
     * 删除APK文件
     * @param id
     * @return
     */
    @RequestMapping(value = "/delfile.json")
    @ResponseBody
    public Map<String,Object> delApkFile(@RequestParam(value = "id") Integer id){
        Map<String,Object> resultMap = new HashMap<>();
        AppVersion appVersion = appVersionService.getAppVersionById(id);
        File apkFile = new File(appVersion.getApkLocPath());
        if (apkFile.delete()){
            resultMap.put("result","success");
        } else {
            resultMap.put("result","failed");
        }
        return resultMap;
    }

    /**
     *
     * @param appId
     * @param id
     * @param versionNo
     * @param versionSize
     * @param publishStatus
     * @param versionInfo
     * @param attach
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/appversionmodifysave")
    public String appVersionModify(@RequestParam(value = "appId") Integer appId,
                                   @RequestParam(value = "id") Integer id,
                                   @RequestParam(value = "versionNo") String versionNo,
                                   @RequestParam(value = "versionSize") double versionSize,
                                   @RequestParam(value = "publishStatus") Integer publishStatus,
                                   @RequestParam(value = "versionInfo") String versionInfo,
                                   @RequestParam(value = "attach") MultipartFile attach,
                                   Model model,
                                   HttpSession session,
                                   HttpServletRequest request){
        DevUser devUser = (DevUser) session.getAttribute("devUserSession");
        AppVersion appVersion = new AppVersion();
        appVersion.setId(id);
        appVersion.setAppId(appId);
        appVersion.setVersionNo(versionNo);
        appVersion.setVersionSize(versionSize);
        appVersion.setPublishStatus(publishStatus);
        appVersion.setVersionInfo(versionInfo.trim());
        appVersion.setModifyBy(devUser.getId());
        appVersion.setModifyDate(new Date());
        if (!attach.isEmpty()) {
            AppInfo appInfo = appInfoService.getAppInfoById(appId);
            String curFileName = appInfo.getAPKName()+"-"+appVersion.getVersionNo();
            Map<String,String> resultMap = fileUpload(attach,request,model,curFileName);
            if (resultMap.get("status") == "200"){
                appVersion.setApkFileName(resultMap.get("fileName"));
                appVersion.setDownloadLink(resultMap.get("DownloadPath"));
                appVersion.setApkLocPath(resultMap.get("filePath"));
            }
        }
        if (appVersionService.update(appVersion) == 1){
            return "redirect:/dev/flatform/app/list";
        } else {
            model.addAttribute("fileUploadError","更新失败！");
            return "developer/appversionmodify";
        }
    }

    /**
     * 转到查看页面
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "/appview/{id}")
    public String appView(@PathVariable(value = "id") Integer id,
                          Model model){
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("id",id);
        paramMap.put("offSet",0);
        paramMap.put("pageSize",5);
        List<AppInfoEx> appInfo = appInfoService.getAppInfoExPageByMap(paramMap);
        AppVersion appVersion = new AppVersion();
        appVersion.setAppId(id);
        List<AppVersion> appVersionList = appVersionService.getAppVersionListByObj(appVersion);

        model.addAttribute("appInfo",appInfo.get(0));
        model.addAttribute("appVersionList",appVersionList);
        return "developer/appinfoview";
    }
}
