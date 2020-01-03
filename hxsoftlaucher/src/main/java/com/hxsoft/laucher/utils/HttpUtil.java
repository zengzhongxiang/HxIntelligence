package com.hxsoft.laucher.utils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.Map;

public class HttpUtil {

     public static void get(String url,  final ResultCallback<String> callback)
    {
        RequestParams requestParams = new RequestParams (url);
        requestParams.setConnectTimeout(500);
        x.http().get(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                callback.success(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ex.printStackTrace();
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


        x.http().post(requestParams, new Callback.CommonCallback<String>() {
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

    /**
     * 下载文件
     * @param url
     * @param savePath
     */
    public static void downFileSyn(String url, String savePath)
    {
        RequestParams requestParams = new RequestParams (url);


        requestParams.setAutoRename(true);
        requestParams.setSaveFilePath(savePath);

        try {
            x.http().postSync(requestParams,String.class);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

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
