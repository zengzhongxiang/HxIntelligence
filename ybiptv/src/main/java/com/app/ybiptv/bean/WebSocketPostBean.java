package com.app.ybiptv.bean;

/**
 * websocket post 发送数据.
 *
 */
public class WebSocketPostBean {

    String type;
    String mac;
    String room_number;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getRoom_number() {
        return room_number;
    }

    public void setRoom_number(String room_number) {
        this.room_number = room_number;
    }

    @Override
    public String toString() {
        return "WebSocketPostBean{" +
                "type='" + type + '\'' +
                ", mac='" + mac + '\'' +
                ", room_number='" + room_number + '\'' +
                '}';
    }

}

/*

{
	"type": "1",
	"mac": "FF:FF:FF:FF:FF:FF",
	"room_number": "123"
}

*/