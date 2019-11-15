package com.hx.hxintelligence.response;

import java.util.List;

public class RoomResponse extends BaseResponse {

    private String home_id;  //房间ID
    private List<RoomBean> rooms;  //返回的是某一个家庭下房间数组

    public String getHome_id() {
        return home_id;
    }

    public void setHome_id(String home_id) {
        this.home_id = home_id;
    }

    public List<RoomBean> getRooms() {
        return rooms;
    }

    public void setRooms(List<RoomBean> rooms) {
        this.rooms = rooms;
    }

    public class RoomBean{
        private String room_id;   //房间id
        private String name;    //房间名字

        public String getRoom_id() {
            return room_id;
        }

        public void setRoom_id(String room_id) {
            this.room_id = room_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
