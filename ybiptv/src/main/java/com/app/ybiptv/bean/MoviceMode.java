package com.app.ybiptv.bean;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.List;

/**
 * 影视数据
 *
 */
public class MoviceMode implements Serializable {

//      "id": 4,
//              "program_name": "美人鱼",
//              "md5": "BE899578CA8771BE4274DE533B451C47.mp4",
//              "program_type": "爆笑喜剧",
//              "viewing_level": 0,
//              "is_it_on_the_shelf": 1,
//              "production_area": "香港",
//              "year_of_production": "2000",
//              "director": "周星驰",
//              "performer": "邓超",
//              "brief_introduction": "搞笑",
//              "little_poster": "http://127.0.0.1:8080/api/upload/showImg?imgFile=1602053441631.jpg",
//              "big_poster": "http://127.0.0.1:8080/api/upload/showImg?imgFile=1601370299219.jpg",
//              "vip_or_not": 0,
//              "home_pag_recommendation": 0,
//              "source_type": "",
//              "douban_score": 0.0,
//              "program_category_id": 1,
//              "path": "/home/超完美谋杀案.mp4",
//              "play_url": "http://192.168.0.250/dy/BE899578CA8771BE4274DE533B451C47.mp4",
//              "copyright_expiration_time": "2020-12-02 15:20:15",
//              "ip": "192.168.0.240",
//              "port": 8686
//
    int id;      //编号
    String program_name = "未知";   //电影名字
    String md5;    //md5加密
    String program_type; // 类别名称
    String production_area; // 地区
    String year_of_production;  //年份
    String director;   //导演
    String performer;   //演员
    String brief_introduction;    //简介
    String little_poster;   //图片
    String big_poster;     //图片
    int vip_or_not;    //是否是VIP
    String douban_score;  //评分
    String play_url;   //播放地址

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
                ", md5='" + md5 + '\'' +
                ", program_type='" + program_type + '\'' +
                ", production_area='" + production_area + '\'' +
                ", year_of_production='" + year_of_production + '\'' +
                ", director='" + director + '\'' +
                ", performer='" + performer + '\'' +
                ", brief_introduction='" + brief_introduction + '\'' +
                ", little_poster='" + little_poster + '\'' +
                ", big_poster='" + big_poster + '\'' +
                ", vip_or_not=" + vip_or_not +
                ", douban_score='" + douban_score + '\'' +
                ", play_url='" + play_url + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    public String getDouban_score() {
        return douban_score;
    }

    public void setDouban_score(String douban_score) {
        this.douban_score = douban_score;
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

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getProgram_type() {
        return program_type;
    }

    public void setProgram_type(String program_type) {
        this.program_type = program_type;
    }

    public String getProduction_area() {
        return production_area;
    }

    public void setProduction_area(String production_area) {
        this.production_area = production_area;
    }

    public String getYear_of_production() {
        return year_of_production;
    }

    public void setYear_of_production(String year_of_production) {
        this.year_of_production = year_of_production;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getPerformer() {
        return performer;
    }

    public void setPerformer(String performer) {
        this.performer = performer;
    }

    public String getBrief_introduction() {
        return brief_introduction;
    }

    public void setBrief_introduction(String brief_introduction) {
        this.brief_introduction = brief_introduction;
    }

    public String getLittle_poster() {
        return little_poster;
    }

    public void setLittle_poster(String little_poster) {
        this.little_poster = little_poster;
    }

    public String getBig_poster() {
        return big_poster;
    }

    public void setBig_poster(String big_poster) {
        this.big_poster = big_poster;
    }

    public int getVip_or_not() {
        return vip_or_not;
    }

    public void setVip_or_not(int vip_or_not) {
        this.vip_or_not = vip_or_not;
    }

    public String getPlay_url() {
        return play_url;
    }

    public void setPlay_url(String play_url) {
        this.play_url = play_url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setEpisodes(List<EpisodesMode> episodes) {
        this.episodes = episodes;
    }
}
