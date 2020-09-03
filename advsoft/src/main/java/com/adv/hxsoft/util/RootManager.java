package com.adv.hxsoft.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;


public class RootManager {

    private static boolean accessRoot = false;
    private static RootManager instance;
    private boolean hasGivenPermission = false;
    private Boolean hasRooted = null;
    private long lastPermissionCheck = -1;

    private RootManager() {
    }

    public static synchronized RootManager getInstance() {
        RootManager rootManager;
        synchronized (RootManager.class) {
            if (instance == null) {
                instance = new RootManager();
            }
            rootManager = instance;
        }
        return rootManager;
    }


    public static String execCMD(String cmd) {

        DataOutputStream dataOutputStream = null;
        try {
            Process prccess = Runtime.getRuntime().exec(cmd);

            InputStream is = prccess.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuffer sb = new StringBuffer();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                line = br.readLine();
            }
            prccess.waitFor();

            br.close();
            is.close();
            ;

            return sb.toString();


        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }



}
