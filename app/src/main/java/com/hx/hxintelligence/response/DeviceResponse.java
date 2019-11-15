package com.hx.hxintelligence.response;

import java.util.List;

public class DeviceResponse extends BaseResponse {
    private String home_id;
    private List<DeviceBean> devices;

    public String getHome_id() {
        return home_id;
    }

    public void setHome_id(String home_id) {
        this.home_id = home_id;
    }

    public List<DeviceBean> getDevices() {
        return devices;
    }

    public void setDevices(List<DeviceBean> devices) {
        this.devices = devices;
    }

    public class DeviceBean{
        private String name;  //设备的名字
        private String md5;   //如果是极联主机设备（即思想者mini，插座等等），代表设备的唯一id。如果是分机，则此id的意义就是分机的父设备(主机)的唯一id
        private int sub_id;  //若为极联主机（即思想者mini，插座等等），sub_id=0。若为极联的分机设备(如零火开关，门磁等等)，sub_id>0。
        private String main_type; //geeklink:极联主机 slave:极联分机 database:码库分机 custom:自学遥控 315m:安防设备
        private int sub_type;  //
        private String room_id; //设备在哪个房间里面
        private String in_room_order; //设备在房间中排列的序号
        private PropertyInfo property;  //分机的属性
        private List<FbsInfo> fbs;  //当main_type=custom的时候，这个字段才有效，否则是没有返回的
        private List<KeyInfo> keys;  //当main_type=slave，sub_id=0x31～0x34或0x41～0x44 或0x37～0x39的时候(即反馈开关设备或者轻触开关设备)，这个字段才有效，否则是没有返回的

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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

        public String getMain_type() {
            return main_type;
        }

        public void setMain_type(String main_type) {
            this.main_type = main_type;
        }

        public int getSub_type() {
            return sub_type;
        }

        public void setSub_type(int sub_type) {
            this.sub_type = sub_type;
        }

        public String getRoom_id() {
            return room_id;
        }

        public void setRoom_id(String room_id) {
            this.room_id = room_id;
        }

        public String getIn_room_order() {
            return in_room_order;
        }

        public void setIn_room_order(String in_room_order) {
            this.in_room_order = in_room_order;
        }

        public PropertyInfo getProperty() {
            return property;
        }

        public void setProperty(PropertyInfo property) {
            this.property = property;
        }

        public List<FbsInfo> getFbs() {
            return fbs;
        }

        public void setFbs(List<FbsInfo> fbs) {
            this.fbs = fbs;
        }

        public List<KeyInfo> getKeys() {
            return keys;
        }

        public void setKeys(List<KeyInfo> keys) {
            this.keys = keys;
        }

        public class PropertyInfo{
            private String status;
            private String temp;   //主机温度，单位是摄氏度
            private String hum;    //主机湿度，单位是百分比

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getTemp() {
                return temp;
            }

            public void setTemp(String temp) {
                this.temp = temp;
            }

            public String getHum() {
                return hum;
            }

            public void setHum(String hum) {
                this.hum = hum;
            }
        }

        public class FbsInfo{
            private String name;   //FB某路开关的名字，有多少路就有多少个名字
            private String road;   //表示第几路

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getRoad() {
                return road;
            }

            public void setRoad(String road) {
                this.road = road;
            }
        }

        public class KeyInfo{
            private String name;  //按键的名字，每个按键都有自己的一个名字
            private String key_id;  //表示某个按键的id，每个按键的id在自学习码设备里面是唯一的

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getKey_id() {
                return key_id;
            }

            public void setKey_id(String key_id) {
                this.key_id = key_id;
            }
        }
    }
}
