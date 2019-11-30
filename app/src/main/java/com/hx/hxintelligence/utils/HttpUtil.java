package com.hx.hxintelligence.utils;

import android.text.TextUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public class HttpUtil {

     public static void get(String url,  final ResultCallback<String> callback)
    {
        RequestParams requestParams = new RequestParams (url);

        x.http().get(requestParams, new Callback.CacheCallback<String>() {
            @Override
            public boolean onCache(String result) {
                if(!TextUtils.isEmpty (result)){
                    callback.success(result);
                }
                return false;
            }

            @Override
            public void onSuccess(String result) {
                callback.success(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                callback.fail(ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

    public static void hxpost(String content,String session, int random,String time,final ResultCallback<String> callback)
    {
        String  serverStri = Constants.JL_URL+"?random="+random+"&time="+time;

        RequestParams requestParams = new RequestParams (serverStri);
        requestParams.setHeader ("appid",Constants.APPID);
        requestParams.setHeader ("session",session);
        requestParams.setHeader ("sign",shaEnc256("appkey="+Constants.APPSECRET+"&random="+random+"&time="+time));
        requestParams.setAsJsonContent(true);
//        System.out.println ("js_request.toString()=="+js_request.toString());
        requestParams.setBodyContent(content);
//        System.out.println ("sign=="+shaEnc256("appkey=241f2c786c3eb775&random="+random+"&time="+time));
//        if (mapParam!=null)
//        {
//            for (Map.Entry<String,Object> e : mapParam.entrySet())
//            {
//                String key = e.getKey();
//                Object value = e.getValue();
//                requestParams.addParameter(key,value);
//            }
//        }

        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
//                Gson gson = new Gson();
//                BaseResponse response = gson.fromJson(result, BaseResponse.class);
//                if("error".equals (response.getAck ())) {  //说明接口报错了
//                    if("wrong session".equals (response.getMsg ())){  //session 被占用了  重新登录
//                    }
//                }
                callback.success(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                callback.fail(ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
//        x.http().post(requestParams, new Callback.CacheCallback<String>() {
//            @Override
//            public boolean onCache(String result) {
//                System.out.println ("带缓存的=="+result);
//                if(!TextUtils.isEmpty (result)){
//                    callback.success(result);
//                }
//                return false;
//            }
//
//            @Override
//            public void onSuccess(String result) {
//                System.out.println ("不带缓存的=="+result);
//                callback.success(result);
//            }
//
//            @Override
//            public void onError(Throwable ex, boolean isOnCallback) {
//                callback.fail(ex.getMessage());
//            }
//
//            @Override
//            public void onCancelled(CancelledException cex) {
//
//            }
//
//            @Override
//            public void onFinished() {
//
//            }
//        });

    }

    /**
     * SHA加密
     *
     * @param strSrc
     *            明文
     * @return 加密之后的密文
     */
    public static String shaEnc256(String strSrc) {
        MessageDigest md = null;
        String strDes = null;
        byte[] bt = strSrc.getBytes();
        try {
            md = MessageDigest.getInstance("SHA-256");// 将此换成SHA-1、SHA-512、SHA-384等参数
            md.update(bt);
            strDes = bytes2Hex(md.digest()); // to HexString
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        return strDes;
    }

    /**
     * byte数组转换为16进制字符串
     *
     * @param bts
     *            数据源
     * @return 16进制字符串
     */
    public static String bytes2Hex(byte[] bts) {
        String des = "";
        String tmp = null;
        for (int i = 0; i < bts.length; i++) {
            tmp = (Integer.toHexString(bts[i] & 0xFF));
            if (tmp.length() == 1) {
                des += "0";
            }
            des += tmp;
        }
        return des;
    }


    public static void post(String url, Map<String,Object> mapParam, final ResultCallback<String> callback)
    {
        RequestParams requestParams = new RequestParams (url);
        if (mapParam!=null)
        {
            for (Map.Entry<String,Object> e : mapParam.entrySet())
            {
                String key = e.getKey();
                Object value = e.getValue();
                requestParams.addParameter(key,value);
            }
        }


        x.http().post(requestParams, new Callback.CacheCallback<String>() {
            @Override
            public boolean onCache(String result) {
                System.out.println ("带缓存的=="+result);
                if(!TextUtils.isEmpty (result)){
                    callback.success(result);
                }
                return false;
            }

            @Override
            public void onSuccess(String result) {
                System.out.println ("不带缓存的=="+result);
                callback.success(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                callback.fail(ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }



    /**
     * 下载文件
     * @param url
     * @param savePath
     */
       public static void downFile(String url, String savePath, final ResultCallback<File> callback)
       {
           RequestParams requestParams = new RequestParams (url);


           requestParams.setAutoRename(true);
           requestParams.setSaveFilePath(savePath);
           x.http().post(requestParams, new Callback.ProgressCallback<File>() {
              @Override
              public void onSuccess(File result) {
                   System.out.println("!!!成功");
                   callback.success(result);
              }

              @Override
              public void onError(Throwable ex, boolean isOnCallback) {
                  System.out.println("!!!失败:"+ex.getMessage());
                   callback.fail(ex.getMessage());
              }

              @Override
              public void onCancelled(CancelledException cex) {

              }

              @Override
              public void onFinished() {

              }

              @Override
              public void onWaiting() {

              }

              @Override
              public void onStarted() {

              }

              @Override
              public void onLoading(long total, long current, boolean isDownloading) {

              }
          });
       }
}
