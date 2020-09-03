package com.adv.hxsoft.util;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class SDFileUtil {

    private static SDFileUtil instance;
    private static final int SUCCESS = 1;
    private static final int FAILED = 0;
    private Context context;
    private FileOperateCallback callback;
    private volatile boolean isSuccess;
    private String errorStr;

    public static SDFileUtil getInstance(Context context) {
        if (instance == null)
            instance = new SDFileUtil(context);
        return instance;
    }

    private SDFileUtil(Context context) {
        this.context = context;
    }

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (callback != null) {
                if (msg.what == SUCCESS) {
                    callback.onSuccess();
                }
                if (msg.what == FAILED) {
                    callback.onFailed(msg.obj.toString());
                }
            }
        }
    };

    public SDFileUtil copyAssetsToSD(final String srcPath, final String sdPath) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                copyAssetsToDst(context, srcPath, sdPath);
                if (isSuccess)
                    handler.obtainMessage(SUCCESS).sendToTarget();
                else
                    handler.obtainMessage(FAILED, errorStr).sendToTarget();
            }
        }).start();
        return this;
    }

    public void setFileOperateCallback(FileOperateCallback callback) {
        this.callback = callback;
    }

    private void copyAssetsToDst(Context context, String srcPath, String dstPath) {
        try {
            String fileNames[] = context.getAssets().list(srcPath);
            if (fileNames.length > 0) {
//                File file = new File(Environment.getExternalStorageDirectory(), dstPath);
                File file = new File(dstPath);
                if (!file.exists()) file.mkdirs();
                for (String fileName : fileNames) {
                    if (!srcPath.equals("")) { // assets 文件夹下的目录
                        copyAssetsToDst(context, srcPath + File.separator + fileName, dstPath + File.separator + fileName);
                    } else { // assets 文件夹
                        copyAssetsToDst(context, fileName, dstPath + File.separator + fileName);
                    }
                }
            } else {
//                File outFile = new File(Environment.getExternalStorageDirectory(), dstPath);
                File outFile = new File(dstPath);
                InputStream is = context.getAssets().open(srcPath);
                FileOutputStream fos = new FileOutputStream(outFile);
                byte[] buffer = new byte[1024];
                int byteCount;
                while ((byteCount = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, byteCount);
                }
                fos.flush();
                is.close();
                fos.close();
            }
            isSuccess = true;
        } catch (Exception e) {
            e.printStackTrace();
            errorStr = e.getMessage();
            isSuccess = false;
        }
    }

    public SDFileUtil copyFileToSD(final String srcPath, final String sdPath) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                copyFileToDst(context, srcPath, sdPath);
                if (isSuccess)
                    handler.obtainMessage(SUCCESS).sendToTarget();
                else
                    handler.obtainMessage(FAILED, errorStr).sendToTarget();
            }
        }).start();
        return this;
    }
    /**
     *
     * @param context
     * @param srcPath   /mnt/sdcard/woss/aaa/bbb
     * @param dstPath  /storage/external_storage/sda4/woss/aaa/bbb
     */
    private void copyFileToDst(Context context, String srcPath, String dstPath) {
        try {
//            String fileNames[];
            //先删除  再创建
            String cmd2 = "rm -fr " + dstPath + "/yunben";
            String strPath2 =  RootManager.execCMD (cmd2);

            File userDIR  = new File(dstPath+File.separator+"yunben");
            System.out.println ("userDIR="+userDIR);
            if (userDIR.exists()==false)
            {
                userDIR.mkdirs();
            }

            String cmd = "cp -R "+srcPath+" "+dstPath;
            String strPath =  RootManager.execCMD (cmd);
            System.out.println ("strPath=="+strPath);
            if("error".equals (strPath)){
                errorStr = "error";
                isSuccess = false;
            }else {
                isSuccess = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
            errorStr = e.getMessage();
            System.out.println ("errorStr=="+errorStr);
            System.out.println ("e=="+e.toString ());
            isSuccess = false;
        }
    }

    public interface FileOperateCallback {
        void onSuccess();

        void onFailed(String error);
    }
}
