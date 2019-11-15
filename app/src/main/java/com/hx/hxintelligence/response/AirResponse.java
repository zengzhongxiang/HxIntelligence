package com.hx.hxintelligence.response;

import java.util.List;

/**
 * 空调返回的数据
 */
public class AirResponse extends BaseResponse {
    private List<AirInfo> data;  //返回的空调数据

    public List<AirInfo> getData() {
        return data;
    }
    public void setData(List<AirInfo> data) {
        this.data = data;
    }

    public class AirInfo{
        private String attr;
        private String value;

        public String getAttr() {
            return attr;
        }

        public void setAttr(String attr) {
            this.attr = attr;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
