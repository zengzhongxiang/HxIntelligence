package com.app.ybiptv.bean;

import java.util.ArrayList;

/**
 *
 */
public class RecommendMode {

    /**
     * id : 2
     * name : 测试连续剧功能
     * spell_name : CSLXJGN
     * director : 李小虎
     * actor : 李大呼
     * introduction : ceshi
     * released_time : 1525167126
     * area : 美国
     * poster_url : /storage/poster/7jdlaEbZz12BJz9Et1N0KXQ3UkGLsdeHWdSMIrE9.jpeg
     * sort : 剧情,犯罪
     * type : 电视剧
     * count : 1
     * episodes : [{"id":3,"pid":2,"name":"第一集","duration":"00:05:23","play_url":"/vod/episodes/bmOPapaROIDUYrN9uNbfvAcYA78XY8gLSpbFHAVY.mp4/index.m3u8","is_pay":"0","price":0,"created_at":"2018-05-12 13:55:13","updated_at":"2018-05-12 13:55:13","source":"1","introduction":"test"}]
     */

    private int id;
    private String name;
    private String spell_name;
    private String director;
    private String actor;
    private String introduction;
    private int released_time;
    private String area;
    private String poster_url;
    private String sort;
    private String type;
    private int count;
    private ArrayList<EpisodesEntity> episodes;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpell_name() {
        return spell_name;
    }

    public void setSpell_name(String spell_name) {
        this.spell_name = spell_name;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public int getReleased_time() {
        return released_time;
    }

    public void setReleased_time(int released_time) {
        this.released_time = released_time;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getPoster_url() {
        return poster_url;
    }

    public void setPoster_url(String poster_url) {
        this.poster_url = poster_url;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
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

    public ArrayList<EpisodesEntity> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(ArrayList<EpisodesEntity> episodes) {
        this.episodes = episodes;
    }

    public static class EpisodesEntity {
        /**
         * id : 3
         * pid : 2
         * name : 第一集
         * duration : 00:05:23
         * play_url : /vod/episodes/bmOPapaROIDUYrN9uNbfvAcYA78XY8gLSpbFHAVY.mp4/index.m3u8
         * is_pay : 0
         * price : 0
         * created_at : 2018-05-12 13:55:13
         * updated_at : 2018-05-12 13:55:13
         * source : 1
         * introduction : test
         */

        private int id;
        private int pid;
        private String name;
        private String duration;
        private String play_url;
        private String is_pay;
        private int price;
        private String created_at;
        private String updated_at;
        private String source;
        private String introduction;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getPid() {
            return pid;
        }

        public void setPid(int pid) {
            this.pid = pid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public String getPlay_url() {
            return play_url;
        }

        public void setPlay_url(String play_url) {
            this.play_url = play_url;
        }

        public String getIs_pay() {
            return is_pay;
        }

        public void setIs_pay(String is_pay) {
            this.is_pay = is_pay;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getIntroduction() {
            return introduction;
        }

        public void setIntroduction(String introduction) {
            this.introduction = introduction;
        }
    }
}
