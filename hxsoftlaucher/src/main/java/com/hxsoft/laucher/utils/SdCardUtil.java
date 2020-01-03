package com.hxsoft.laucher.utils;

import android.os.Build.VERSION;
import android.os.Environment;
import android.os.StatFs;

import com.hxsoft.laucher.activity.App;

import java.io.File;

/* renamed from: com.hhzt.iptv.module.common.utils.SdCardUtil */
public class SdCardUtil {
    public static boolean couldDownLoad() {
        if (!Environment.getExternalStorageState().equals("mounted")) {
            return false;
        }
        long totalBlocks;
        long availableBlocks;
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        if (VERSION.SDK_INT >= 18) {
            totalBlocks = stat.getBlockCountLong();
            availableBlocks = stat.getAvailableBlocksLong();
        } else {
            totalBlocks = (long) stat.getBlockCount();
            availableBlocks = (long) stat.getAvailableBlocks();
        }
        if (((double) ((((float) availableBlocks) * 1.0f) / ((float) totalBlocks))) > 0.3d) {
            return true;
        }
        return false;
    }

    public static File  getSDCardPath()
    {

//        System.out.println("ExistSDCard=="+ExistSDCard());
//        System.out.println("Environment.getDataDirectory()=="+Environment.getDataDirectory());
//        System.out.println("Environment.getExternalStorageDirectory()=="+Environment.getExternalStorageDirectory());

        if(ExistSDCard()) {
            //这个是SD卡目录
            return Environment.getExternalStorageDirectory();
        }else{
            //这个是内部存储目录
            String path =  Environment.getDataDirectory()+"/data/"+ App.getApp().getPackageName();
//            System.out.println("path=="+path);
            File file = new File(path);
            return file;
        }
    }

    public static File getAppDataPath()
    {
        File file =  Environment.getDataDirectory();
        return file;
    }

    //SD是否存在
    private static boolean ExistSDCard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else

            return false;
    }

}
