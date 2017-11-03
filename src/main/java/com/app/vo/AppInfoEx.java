package com.app.vo;

import com.app.pojo.AppInfo;

import java.util.Date;

public class AppInfoEx extends AppInfo {
    private String flatformName;
    private String categoryLevel1Name;
    private String categoryLevel2Name;
    private String categoryLevel3Name;
    private String statusName;
    private String versionNo;

    @Override
    public String toString() {
        return "AppInfoEx{" +
                "flatformName='" + flatformName + '\'' +
                ", categoryLevel1Name='" + categoryLevel1Name + '\'' +
                ", categoryLevel2Name='" + categoryLevel2Name + '\'' +
                ", categoryLevel3Name='" + categoryLevel3Name + '\'' +
                ", statusName='" + statusName + '\'' +
                ", versionNo='" + versionNo + '\'' +
                '}';
    }

    public AppInfoEx() {
    }

    public AppInfoEx(Integer id, String softwareName, String aPKName, String supportROM, String interfaceLanguage, Double softwareSize, Date updateDate, Integer devId, String appInfo, Integer status, Date onSaleDate, Date offSaleDate, Integer flatformId, Integer categoryLevel3, Integer downloads, Integer createdBy, Date creationDate, Integer modifyBy, Date modifyDate, Integer categoryLevel1, Integer categoryLevel2, String logoPicPath, String logoLocPath, Integer versionId, String flatformName, String categoryLevel1Name, String categoryLevel2Name, String categoryLevel3Name, String statusName, String versionNo) {
        super(id, softwareName, aPKName, supportROM, interfaceLanguage, softwareSize, updateDate, devId, appInfo, status, onSaleDate, offSaleDate, flatformId, categoryLevel3, downloads, createdBy, creationDate, modifyBy, modifyDate, categoryLevel1, categoryLevel2, logoPicPath, logoLocPath, versionId);
        this.flatformName = flatformName;
        this.categoryLevel1Name = categoryLevel1Name;
        this.categoryLevel2Name = categoryLevel2Name;
        this.categoryLevel3Name = categoryLevel3Name;
        this.statusName = statusName;
        this.versionNo = versionNo;
    }

    public String getFlatformName() {
        return flatformName;
    }

    public void setFlatformName(String flatformName) {
        this.flatformName = flatformName;
    }

    public String getCategoryLevel1Name() {
        return categoryLevel1Name;
    }

    public void setCategoryLevel1Name(String categoryLevel1Name) {
        this.categoryLevel1Name = categoryLevel1Name;
    }

    public String getCategoryLevel2Name() {
        return categoryLevel2Name;
    }

    public void setCategoryLevel2Name(String categoryLevel2Name) {
        this.categoryLevel2Name = categoryLevel2Name;
    }

    public String getCategoryLevel3Name() {
        return categoryLevel3Name;
    }

    public void setCategoryLevel3Name(String categoryLevel3Name) {
        this.categoryLevel3Name = categoryLevel3Name;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(String versionNo) {
        this.versionNo = versionNo;
    }
}
