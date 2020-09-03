package com.adv.hxsoft.util;

import android.annotation.SuppressLint;
import android.os.Environment;

import com.adv.hxsoft.APP;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/* renamed from: com.hhzt.iptv.module.common.utils.SdCardUtil */
public class SdCardUtil {

    /**
     * 从sd卡获取图片资源
     * @return
     */
    public static List<String> getImagePathFromSD(String filePath) {
        // 图片列表
        List<String> imagePathList = new ArrayList<String> ();
        // 得到sd卡内image文件夹的路径   File.separator(/)

        // 得到该路径文件夹下所有的文件
        File fileAll = new File(filePath);
        File[] files = fileAll.listFiles();
        // 将所有的文件存入ArrayList中,并过滤所有图片格式的文件
        if(files!=null) {
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                if (checkIsImageFile (file.getPath ())) {
                    imagePathList.add (file.getPath ());
                }
            }
        }
        // 返回得到的图片列表
        return imagePathList;
    }

    /**
     * 检查扩展名，得到图片格式的文件
     * @param fName  文件名
     * @return
     */
    @SuppressLint("DefaultLocale")
    private static boolean checkIsImageFile(String fName) {
        boolean isImageFile = false;
        // 获取扩展名
        String FileEnd = fName.substring(fName.lastIndexOf(".") + 1,
                fName.length()).toLowerCase();
        if (FileEnd.equals("jpg") || FileEnd.equals("png") || FileEnd.equals("gif")
                || FileEnd.equals("jpeg")|| FileEnd.equals("bmp") ) {
            isImageFile = true;
        } else {
            isImageFile = false;
        }
        return isImageFile;
    }

    /**
     * 从sd卡获取视频资源
     * @return
     */
    public static List<String> getVideoPathFromSD(String filePath) {
        // 视频列表
        List<String> videoPathList = new ArrayList<String> ();

        // 得到该路径文件夹下所有的文件
        File fileAll = new File(filePath);
        File[] files = fileAll.listFiles();
        // 将所有的文件存入ArrayList中,并过滤所有视频格式的文件
        if(files!=null) {
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                if (checkIsVideoFile (file.getPath ())) {
                    videoPathList.add (file.getPath ());
                }
            }
        }
        // 返回得到的视频列表
        return videoPathList;
    }

    /**
     * 检查扩展名，得到图片格式的文件
     * @param fName  文件名
     * @return
     */
    @SuppressLint("DefaultLocale")
    private static boolean checkIsVideoFile(String fName) {
        boolean isVideoFile = false;
        // 获取扩展名
        String FileEnd = fName.substring(fName.lastIndexOf(".") + 1,
                fName.length()).toLowerCase();
        if (FileEnd.equals("mp4") || FileEnd.equals("3gp") || FileEnd.equals("avi") || FileEnd.equals("flv")) {
            isVideoFile = true;
        } else {
            isVideoFile = false;
        }
        return isVideoFile;
    }


    public static File initSdCard() {
        //这个是SD卡目录
        File file = Environment.getExternalStorageDirectory ();

//        String path =  Environment.getDataDirectory()+"/data/"+ APP.getApp().getPackageName();
////            System.out.println("path=="+path);
//        File file = new File(path);
        return file;

    }
}
