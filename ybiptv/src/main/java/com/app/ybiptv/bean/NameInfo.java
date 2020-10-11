package com.app.ybiptv.bean;

public class NameInfo {
    private String zh_CN;
    private String en_US;

    public NameInfo(String zh_CN, String en_US) {
        this.zh_CN = zh_CN;
        this.en_US = en_US;
    }

    public String getZh_CN() {
        return zh_CN;
    }

    public void setZh_CN(String zh_CN) {
        this.zh_CN = zh_CN;
    }

    public String getEn_US() {
        return en_US;
    }

    public void setEn_US(String en_US) {
        this.en_US = en_US;
    }
}
