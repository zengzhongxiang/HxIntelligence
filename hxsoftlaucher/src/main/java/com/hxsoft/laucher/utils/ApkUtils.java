package com.hxsoft.laucher.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@SuppressLint("WrongConstant")
public class ApkUtils {
    public static void install(Context context, File file) {
       // requestPermission((Activity) context);
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        intent.setFlags(268435456);
        context.startActivity(intent);
    }

    public static void install(Context context, Uri uri) {
        // requestPermission((Activity) context);
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        intent.setFlags(268435456);
        context.startActivity(intent);
    }

    private static void requestPermission(Activity context) {
        if (VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE}, 0);

            }
        }
    }


    @SuppressLint("WrongConstant")
    public static void installApk(Context context, String str) {
        if (context != null && !TextUtils.isEmpty(str)) {
            File file = new File(str);
            ApkUtils.modifyFile(file);
            Intent intent = new Intent("android.intent.action.VIEW");
            if (VERSION.SDK_INT >= 24) {
                intent.setFlags(1);
                intent.setDataAndType(FileProvider.getUriForFile(context, "com.woss.iptv.fileProvider", file), "application/vnd.android.package-archive");
            } else {
                intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                intent.setFlags(268435456);
            }
            context.startActivity(intent);
        }
    }


    public static void installApkToNewTask(Context context, String str) {
        if (context != null && !TextUtils.isEmpty(str)) {
            File file = new File(str);
            ApkUtils.modifyFile(file);
            Intent intent = new Intent("android.intent.action.VIEW");
            if (VERSION.SDK_INT >= 24) {
                intent.setFlags(1);
                intent.setDataAndType(FileProvider.getUriForFile(context, "com.hassmedia.cloudlauncher.fileProvider", file), "application/vnd.android.package-archive");
            } else {
                intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                intent.setFlags(268435456);
            }
            intent.addFlags(268435456);
            context.startActivity(intent);
        }
    }

    public static void modifyFile(File file) {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("su chmod -R 777 ");
            stringBuilder.append(file.getAbsolutePath());
            Runtime.getRuntime().exec(stringBuilder.toString()).waitFor();
            System.out.println("修改成功:");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("修改失败:"+e.getMessage());
        }
    }

    public static void uninstall(Context context, String str) {
        if (ApkUtils.isAvailable(context, str)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("package:");
            stringBuilder.append(str);
            context.startActivity(new Intent("android.intent.action.DELETE", Uri.parse(stringBuilder.toString())));
        }
    }

    public static void uninstallToNewTask(Context context, String str) {
        if (ApkUtils.isAvailable(context, str)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("package:");
            stringBuilder.append(str);
            Intent intent = new Intent("android.intent.action.DELETE", Uri.parse(stringBuilder.toString()));
            intent.addFlags(268435456);
            context.startActivity(intent);
        }
    }

    public static boolean isAvailable(Context context, String str) {
       try
       {
           int i = 0;
           List installedPackages = context.getPackageManager().getInstalledPackages(0);
           List arrayList = new ArrayList();
           if (installedPackages != null) {
               while (i < installedPackages.size()) {
                   arrayList.add(((PackageInfo) installedPackages.get(i)).packageName);
                   i++;
               }
           }
           return arrayList.contains(str);
       }catch (Throwable e)
       {
           return false;
       }
    }

    public static boolean isAvailable(Context context, File file) {
        return ApkUtils.isAvailable(context, ApkUtils.getPackageName(context, file.getAbsolutePath()));
    }

    public static String getPackageName(Context context, String str) {
        PackageInfo packageArchiveInfo = context.getPackageManager().getPackageArchiveInfo(str, 1);
        return packageArchiveInfo != null ? packageArchiveInfo.applicationInfo.packageName : null;
    }

    public static String getChannelFromApk(Context context, String str) {

        return null;
    }

    public static void startAppWithPackageName(Context context, String str) {
        String str2;
        ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
        if (VERSION.SDK_INT > 20) {
            str2 = ((RunningAppProcessInfo) activityManager.getRunningAppProcesses().get(0)).processName;
        } else {
            str2 = ((RunningTaskInfo) activityManager.getRunningTasks(1).get(0)).topActivity.getPackageName();
        }
        if (!str2.equals(str)) {
            PackageInfo packageInfo;
            try {
                packageInfo = context.getPackageManager().getPackageInfo(str, 0);
            } catch (NameNotFoundException e) {
                e.printStackTrace();
                packageInfo = null;
            }
            if (packageInfo != null) {
                Intent intent = new Intent("android.intent.action.MAIN", null);
                intent.addCategory("android.intent.category.LAUNCHER");
                intent.setPackage(packageInfo.packageName);
                ResolveInfo resolveInfo = (ResolveInfo) context.getPackageManager().queryIntentActivities(intent, 0).iterator().next();
                if (resolveInfo != null) {
                    str2 = resolveInfo.activityInfo.packageName;
                    str = resolveInfo.activityInfo.name;
                    intent = new Intent("android.intent.action.MAIN");
                    intent.addCategory("android.intent.category.LAUNCHER");
                    intent.addFlags(268435456);
                    //intent.putExtra("server", MyApp.getIPAddress());
                    //intent.putExtra("uuid", MyApp.getUUID());
                    intent.setComponent(new ComponentName(str2, str));
                    context.startActivity(intent);
                }
            }
        }
    }

    /**
     * 修改权限
     * @param dir 目录
     */
    public static void chmodDir777(String dir) {
        if (new File(dir).exists()) {
            try {
                String command = "chmod -R 777 " + dir;
                if (Runtime.getRuntime().exec(command).waitFor() == 0) {
                } else {
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e2) {
                e2.printStackTrace();
            }
        }
    }

    public static boolean silentInstall(String apkPath) {
        boolean result = false;
        DataOutputStream dataOutputStream = null;
        BufferedReader errorStream = null;
        BufferedReader successStream = null;
        Process process = null;
        try {
            // 申请 su 权限
            process = Runtime.getRuntime().exec("su");
            dataOutputStream = new DataOutputStream(process.getOutputStream());
            // 执行 pm install 命令
            String command = "pm install -r " + apkPath + "\n";
            dataOutputStream.write(command.getBytes(Charset.forName("UTF-8")));
            dataOutputStream.writeBytes("exit\n");
            dataOutputStream.flush();
            process.waitFor();
            errorStream = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            StringBuilder errorMsg = new StringBuilder();
            String line;
            while ((line = errorStream.readLine()) != null) {
                errorMsg.append(line);
            }
            StringBuilder successMsg = new StringBuilder();
            successStream = new BufferedReader(new InputStreamReader(process.getInputStream()));
            // 读取命令执行结果
            while ((line = successStream.readLine()) != null) {
                successMsg.append(line);
            }
            // 如果执行结果中包含 Failure 字样就认为是操作失败，否则就认为安装成功
            if (!(errorMsg.toString().contains("Failure") || successMsg.toString().contains("Failure"))) {
                result = true;
            }
        } catch (Exception e) {
        } finally {
            try {
                if (process != null) {
                    process.destroy();
                }
                if (dataOutputStream != null) {
                    dataOutputStream.close();
                }
                if (errorStream != null) {
                    errorStream.close();
                }
                if (successStream != null) {
                    successStream.close();
                }
            } catch (Exception e) {
                // ignored
            }
        }
        return result;
    }

    public static boolean isRoot() {
        return new File("/system/bin/su").exists() || new File("/system/xbin/su").exists();
    }


    /**
     * 安装apk
     * @param apkAbsolutePath
     * @return
     */
    public static boolean install(String apkAbsolutePath) {
        String result = "";
        ProcessBuilder processBuilder = new ProcessBuilder(new String[]{"pm", "install", "-r", apkAbsolutePath});
        Process process = null;
        InputStream errIs = null;
        InputStream inIs = null;
        try {
            int read;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            process = processBuilder.start();
            errIs = process.getErrorStream();
            while (true) {
                read = errIs.read();
                if (read == -1) {
                    break;
                }
                baos.write(read);
            }
            baos.write(10);
            inIs = process.getInputStream();
            while (true) {
                read = inIs.read();
                if (read == -1) {
                    break;
                }
                baos.write(read);
            }
             result = new String(baos.toByteArray());


        } catch (IOException e2) {
            e2.printStackTrace();

        } finally {
            if (errIs != null) {
                try {
                    errIs.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inIs != null) {
                try {
                    inIs.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (process != null) {
                process.destroy();
            }
        }
        if (result == null && (result.endsWith("Success") || result.endsWith("Success\n"))) {
            return true;
        }
        return false;
    }



    public static void startAppWithPackageName(Context context, String str,String id) {
        String str2;
        ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
        if (VERSION.SDK_INT > 20) {
            str2 = ((RunningAppProcessInfo) activityManager.getRunningAppProcesses().get(0)).processName;
        } else {
            str2 = ((RunningTaskInfo) activityManager.getRunningTasks(1).get(0)).topActivity.getPackageName();
        }
        if (!str2.equals(str)) {
            PackageInfo packageInfo;
            try {
                packageInfo = context.getPackageManager().getPackageInfo(str, 0);
            } catch (NameNotFoundException e) {
                e.printStackTrace();
                packageInfo = null;
            }
            if (packageInfo != null) {
                Intent intent = new Intent("android.intent.action.MAIN", null);
                intent.addCategory("android.intent.category.LAUNCHER");
                intent.setPackage(packageInfo.packageName);
                intent.putExtra("typeId",id);
                ResolveInfo resolveInfo = (ResolveInfo) context.getPackageManager().queryIntentActivities(intent, 0).iterator().next();
                if (resolveInfo != null) {
                    str2 = resolveInfo.activityInfo.packageName;
                    str = resolveInfo.activityInfo.name;
                    intent = new Intent("android.intent.action.MAIN");
                    intent.addCategory("android.intent.category.LAUNCHER");
                    intent.addFlags(268435456);
                    //intent.putExtra("server", MyApp.getIPAddress());
                    //intent.putExtra("uuid", MyApp.getUUID());
                    intent.putExtra("typeId",id);
                    intent.setComponent(new ComponentName(str2, str));
                    context.startActivity(intent);
                }
            }
        }
    }

    public static void rebootApp(Context context) {
        Intent launchIntentForPackage = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        launchIntentForPackage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(launchIntentForPackage);
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public static boolean checkPackage(Context paramContext, String paramString)
    {
        if ((paramString == null) || ("".equals(paramString))) {
            return false;
        }
        try
        {
            paramContext.getPackageManager().getApplicationInfo(paramString, 8192);
            return true;
        }
        catch (NameNotFoundException localNameNotFoundException) {}
        return false;
    }


    public static List scanLocalInstallAppList(Context r7) {

        throw new UnsupportedOperationException("Method not decompiled: com.hassmedia.cloudlauncher.utils.ApkUtils.scanLocalInstallAppList(android.content.Context):java.util.List<com.hassmedia.cloudlauncher.bean.AppDataClient>");
    }

    public static String getAppVersion(Context context, String str) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(str, 0);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("");
            stringBuilder.append(packageInfo.versionCode);
            return stringBuilder.toString();
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static List deleteExistApk(Context context, List list) {
        if (list == null) {
            return null;
        }
        List scanLocalInstallAppList = ApkUtils.scanLocalInstallAppList(context);
        for (int i = 0; i < scanLocalInstallAppList.size(); i++) {
            if (list.contains(scanLocalInstallAppList.get(i))) {
                list.remove(scanLocalInstallAppList.get(i));
            }
        }
        return list;
    }

    public static List  deleteSelfAppDataClient(Context context, List list) {
       return null;
    }

    public static boolean isForeground(Activity activity) {
        return ApkUtils.isForeground(activity, activity.getClass().getName());
    }

    public static boolean isForeground(Context context, String str) {
        if (context == null || TextUtils.isEmpty(str)) {
            return false;
        }
        List runningTasks = ((ActivityManager) context.getSystemService("activity")).getRunningTasks(1);
        if (runningTasks == null || runningTasks.size() <= 0 || !str.equals(((RunningTaskInfo) runningTasks.get(0)).topActivity.getClassName())) {
            return false;
        }
        return true;
    }

    /**
     *
     * @param context
     * @param packageName  包名
     * @param itemId    传入woss_hotel_ppt 里面的id参数
     */
    public static void insertApp(Context context,String packageName,String itemId){
        if(!TextUtils.isEmpty (packageName)){
            if (ApkUtils.isAvailable(context, packageName)){
                System.out.println("已安装");
                if(TextUtils.isEmpty (itemId)){
                    ApkUtils.startAppWithPackageName(context, packageName);
                }else {
                    ApkUtils.startAppWithPackageName(context,packageName,itemId);
                }

                return;
            }else {
                File f = new File(SdCardUtil.getSDCardPath().getPath()+"/woss/apk/"+packageName+".apk");
                System.out.println ("f.exists()=="+f.exists());
                if (f.exists() == false) {
                    Toast.makeText(context, "APK不存在，请联系系统管理员", 0).show();
                } else {
                    ApkUtils.install(context,f);
                }
                //                Toast.makeText(mContext, "正在下载APK，请稍候再试", 0).show();


            }
        }else{
            Toast.makeText(context, "APK包名不存在，请联系系统管理员", 0).show();
        }
    }
}
