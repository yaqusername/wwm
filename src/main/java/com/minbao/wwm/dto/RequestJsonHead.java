package com.minbao.wwm.dto;

import java.io.Serializable;

public class RequestJsonHead implements Serializable {

    private static final long serialVersionUID = 4724442180900072589L;

    private String aid;
    private String ver;
    private String ln;
    private String mod;
    private String de;
    private int sync;
    private int userId;
    private int brandId;
    private String userName;
    private String chkCode;

    public String getAid() {
        return aid;
    }
    public void setAid(String aid) {
        this.aid = aid;
    }
    public String getVer() {
        return ver;
    }
    public void setVer(String ver) {
        this.ver = ver;
    }
    public String getLn() {
        return ln;
    }
    public void setLn(String ln) {
        this.ln = ln;
    }
    public String getMod() {
        return mod;
    }
    public void setMod(String mod) {
        this.mod = mod;
    }
    public String getDe() {
        return de;
    }
    public void setDe(String de) {
        this.de = de;
    }
    public int getSync() {
        return sync;
    }
    public void setSync(int sync) {
        this.sync = sync;
    }
    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getChkCode() {
        return chkCode;
    }

    public void setChkCode(String chkCode) {
        this.chkCode = chkCode;
    }

    @Override
    public String toString() {
        return "RequestJsonHead{" +
                "aid='" + aid + '\'' +
                ", ver='" + ver + '\'' +
                ", ln='" + ln + '\'' +
                ", mod='" + mod + '\'' +
                ", de='" + de + '\'' +
                ", sync=" + sync +
                ", userId=" + userId +
                ", brandId=" + brandId +
                ", userName='" + userName + '\'' +
                ", chkCode='" + chkCode + '\'' +
                '}';
    }
}
