package com.hx.hxintelligence.response;

import java.util.List;

public class HomeResponse extends BaseResponse {

    private List<HomeBean> homes;  //家庭数组

    public List<HomeBean> getHomes() {
        return homes;
    }

    public void setHomes(List<HomeBean> homes) {
        this.homes = homes;
    }

    public class HomeBean{
        private String home_id;   //家庭的唯一id
        private String name;    //家庭的名字
        private String center_id;  //家庭中控的id，控制情景和控制分机都需要用到。注：情景是保存在中控中的，所以控制情景需要用到该id

        public String getHome_id() {
            return home_id;
        }

        public void setHome_id(String home_id) {
            this.home_id = home_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCenter_id() {
            return center_id;
        }

        public void setCenter_id(String center_id) {
            this.center_id = center_id;
        }
    }
}
