package com.app.ybiptv.bean;

/**
 * 影视标题栏数据
 *
 */
public class MoviceTitlerMode {

    String id;     //标题编号
    String programName;   //标题名称
    String state;
    String explain_;
    String creationTime;
    String numberOfFilms;

    public MoviceTitlerMode(String id, String programName) {
        this.id = id;
        this.programName = programName;
    }

    public MoviceTitlerMode(String id, String programName, String state, String explain_, String creationTime, String numberOfFilms) {
        this.id = id;
        this.programName = programName;
        this.state = state;
        this.explain_ = explain_;
        this.creationTime = creationTime;
        this.numberOfFilms = numberOfFilms;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getExplain_() {
        return explain_;
    }

    public void setExplain_(String explain_) {
        this.explain_ = explain_;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public String getNumberOfFilms() {
        return numberOfFilms;
    }

    public void setNumberOfFilms(String numberOfFilms) {
        this.numberOfFilms = numberOfFilms;
    }
}
