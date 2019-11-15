package com.hx.hxintelligence.response;

/**
 * 网络请求返回结果
 *
 * @author caoxuefeng
 */
public class BaseResponse {

    private String method;    //方法
    private String seq;   //序列 time+random
    private String ack;   //ok：成功，error：失败
    private String msg;   //ack为error，这里是错误信息，ack为ok时不返

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public String getAck() {
        return ack;
    }

    public void setAck(String ack) {
        this.ack = ack;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
