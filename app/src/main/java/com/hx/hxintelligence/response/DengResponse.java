package com.hx.hxintelligence.response;

public class DengResponse {
    private String name;   //FB某路开关的名字，有多少路就有多少个名字
    private int road;   //表示第几路
    private String md5;   //如果是极联主机设备（即思想者mini，插座等等），代表设备的唯一id。如果是分机，则此id的意义就是分机的父设备(主机)的唯一id
    private int sub_id;  //若为极联主机（即思想者mini，插座等等），sub_id=0。若为极联的分机设备(如零火开关，门磁等等)，sub_id>0。

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public int getRoad() {
        return road;
    }

    public void setRoad(int road) {
        this.road = road;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public int getSub_id() {
        return sub_id;
    }

    public void setSub_id(int sub_id) {
        this.sub_id = sub_id;
    }
}
