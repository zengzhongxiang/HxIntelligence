package com.app.ybiptv.utils;

/**
 *
 */

public class Consts {

    public static final String IP_ADDR_KEY = "ip_addr_key";   //服务器地址键
    public static final String IP_ROOM_NO_KEY = "ip_room_no_key";  //房间号

    public static String ROOT_ADDR = "";  //http://192.168.0.250:8080

    public static final String GET_MOVICE_TITLE_ADR = "/api/v1/type/findAll"; // 获取标题栏
    public static final String GET_SERIE_INFO_ADR =  "/api/v1/dy/getDyByType"; // 获取类型影视列表
    public static final String GET_MOVICE_DEFAULT_ADR = "/api/v1/dy/getDyDetail"; // 影视详情

    public static final String GET_HEART_ADR = "/api/v1/heart"; // 心跳

    public static final String SEARCH_MOVICE_ADR = "/api/v1/get_search_info"; // 搜索影视
    public static final String POST_RECOMMENT_MOVIE_ADR = "/api/v1/get_recommend_info";  //获取推荐

    public static String GET_MOVICE_TITLE; // 获取标题栏
    public static String GET_SERIE_INFO; // 获取类型影视列表
    public static String GET_MOVICE_DEFAULT; // 影视详情
    public static String SEARCH_MOVICE; // 搜索影视
    public static String POST_RECOMMENT_MOVIE;  //获取推荐

    public static String GET_HEART;  // 心跳

    public static final int DEFUALT_PAGE = 0;
    public static final int PAGE_COUNT = 24;

    public static void initUrl(){
        GET_MOVICE_TITLE = ROOT_ADDR + GET_MOVICE_TITLE_ADR;
        GET_SERIE_INFO = ROOT_ADDR + GET_SERIE_INFO_ADR;
        GET_MOVICE_DEFAULT = ROOT_ADDR + GET_MOVICE_DEFAULT_ADR;
        SEARCH_MOVICE = ROOT_ADDR + SEARCH_MOVICE_ADR;
        POST_RECOMMENT_MOVIE = ROOT_ADDR + POST_RECOMMENT_MOVIE_ADR;
        GET_HEART = ROOT_ADDR + GET_HEART_ADR;
    }

}
