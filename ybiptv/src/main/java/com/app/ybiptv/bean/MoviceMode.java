package com.app.ybiptv.bean;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.List;

/**
 * 影视数据
 *
 */
public class MoviceMode implements Serializable {

    int id;      //编号
    String program_name = "未知";   //名字
    String path;
    String introduction; // 描述
    String director; // 导演
    String actor; //演员
    String released_time;   //年份
    String area;    //地区
    String play_url;   //路径
    String sort;     //分类
    String type;     //类型 电影还是电视剧
    int count;        //   电视剧多少集  暂时不需要
    List<EpisodesMode> episodes;   //   电视剧  暂时不需要

    public List<EpisodesMode> getEpisodes() {
        return episodes;
    }

    @Override
    public String toString() {
        return "MoviceMode{" +
                "id=" + id +
                ", program_name='" + program_name + '\'' +
                ", path='" + path + '\'' +
                ", introduction='" + introduction + '\'' +
                ", director='" + director + '\'' +
                ", actor='" + actor + '\'' +
                ", released_time='" + released_time + '\'' +
                ", area='" + area + '\'' +
                ", play_url='" + play_url + '\'' +
                ", sort='" + sort + '\'' +
                ", count=" + count +
                ", type='" + type + '\'' +
                ", episodes=" + episodes +
                '}';
    }

    public void setEpisodes(List<EpisodesMode> episodes) {
        this.episodes = episodes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProgram_name() {
        return program_name;
    }

    public void setProgram_name(String program_name) {
        this.program_name = program_name;
    }


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getIntroduction() {
        return !TextUtils.isEmpty(introduction) ? introduction : "未知";
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getDirector() {
        return !TextUtils.isEmpty(director) ? director : "未知";
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getActor() {
        return !TextUtils.isEmpty(actor) ? actor : "未知";
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public String getReleased_time() {
        return released_time;
    }

    public void setReleased_time(String released_time) {
        this.released_time = released_time;
    }

    public String getArea() {
        return !TextUtils.isEmpty(area) ? area : "未知";
    }

    public void setArea(String area) {
        this.area = area;
    }


    public String getPlay_url() {
        return play_url;
    }

    public void setPlay_url(String play_url) {
        this.play_url = play_url;
    }

    public String getSort() {
        return !TextUtils.isEmpty(sort) ? sort : "未知";
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getType() {
        return !TextUtils.isEmpty(type) ? type: "未知";
    }

    public void setType(String type) {
        this.type = type;
    }

}
