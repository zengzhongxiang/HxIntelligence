package com.app.ybiptv.bean;


import java.io.Serializable;

/**
 * 集数数据
 *
 */
public class EpisodesMode implements Serializable {

    int id;
    int pid;
    String name;
    String duration;
    String play_url;
    String is_pay;
    String price;
    String created_at;
    String updated_at;
    String source;
    String introduction;

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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
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

    @Override
    public String toString() {
        return "EpisodesMode{" +
                "id=" + id +
                ", pid=" + pid +
                ", name='" + name + '\'' +
                ", duration='" + duration + '\'' +
                ", play_url='" + play_url + '\'' +
                ", is_pay='" + is_pay + '\'' +
                ", price='" + price + '\'' +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                ", source='" + source + '\'' +
                ", introduction='" + introduction + '\'' +
                '}';
    }

}
