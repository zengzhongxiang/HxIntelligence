package com.adv.hxsoft.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import com.adv.hxsoft.APP;
import com.adv.hxsoft.util.SpApplyTools;
import com.adv.hxsoft.util.SystemInfo;

public class USBReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.e("TAG", "action === " + intent.getAction());
        APP app = APP.getApp ();
        if (intent.getAction().equals("android.intent.action.MEDIA_MOUNTED")) {//U盘插入
            String path = intent.getDataString();
            System.out.println ("U盘插入=="+path);
            if (!TextUtils.isEmpty (path)) {
                String[] pathArr = path.split("file://");
                System.out.println ("pathArr=="+pathArr.length);
                if (pathArr.length == 2) {
                    String pathString = pathArr[1];//U盘路径
                    System.out.println ("pathString=="+pathString);
                    if (!TextUtils.isEmpty (pathString)) {
                        // doSomething
                        System.out.println ("app=="+app);
                        if(app!=null){
                            SpApplyTools.putBoolean (SpApplyTools.ISUSBSDCARD,true);
                            SpApplyTools.putString (SpApplyTools.PATHSTRING,pathString);
//                            app.setIsusbSdcard(true);
//                            app.setUsbFile(pathString);
                            SpApplyTools.putString (SpApplyTools.USB_PATH,pathString);
                            SystemInfo.rebootApp(context);
//                            String baseDIR = pathString+"/woss";
//                            File file = new File (baseDIR);
//                            final String sdDIR =  SdCardUtil.getSDCardPath().getPath();
//                            System.out.println ("file.exists()=="+file.exists());
//                            if (file.exists()) {   //有woss这个目录
//                                if (BaseActivity.getBaseInstance () != null) {
//                                    BaseActivity.getBaseInstance ().getBaseHandler ().sendEmptyMessage (HandlerValue.DOWN_SERVICE_VALUE_13);
//                                }
//                            }
                        }
                    }
                }
            }
        } else if (intent.getAction().equals("android.intent.action.MEDIA_UNMOUNTED")) {//U盘拔出
            // doSomething
            System.out.println ("U盘拔出");
            if(app!=null){
//                app.setIsusbSdcard(false);
//                app.setUsbFile("");
                SpApplyTools.putBoolean (SpApplyTools.ISUSBSDCARD,false);
                SpApplyTools.putString (SpApplyTools.PATHSTRING,"");
                SpApplyTools.putString (SpApplyTools.USB_PATH,"");
            }
        }else if (intent.getAction().equals("android.intent.action.MEDIA_REMOVED")){ // 完全拔出
            System.out.println ("完全拔出");
            if(app!=null){
//                app.setIsusbSdcard(false);
//                app.setUsbFile("");
                SpApplyTools.putBoolean (SpApplyTools.ISUSBSDCARD,false);
                SpApplyTools.putString (SpApplyTools.PATHSTRING,"");
                SpApplyTools.putString (SpApplyTools.USB_PATH,"");
            }
        }
    }
}

