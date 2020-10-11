package com.app.ybiptv.bean;

/**
 * WebSocket 数据
 *
 */
public class WebSocketGetBean {

    int way;
    String data;
    int code = -250;
    String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getWay() {
        return way;
    }

    public void setWay(int way) {
        this.way = way;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "WebSocketGetBean{" +
                "way=" + way +
                ", data='" + data + '\'' +
                ", code=" + code +
                ", message='" + message + '\'' +
                '}';
    }

}
