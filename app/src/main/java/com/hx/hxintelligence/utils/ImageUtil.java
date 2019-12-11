package com.hx.hxintelligence.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class ImageUtil {

    public static Bitmap base64ToBitmap(String base64String) {
//        byte[] decode = Base64.decode(base64String.toString().trim(), Base64.URL_SAFE);
//        System.out.println ("decode=="+decode);
//        Bitmap bitmap = BitmapFactory.decodeByteArray(decode, 0, decode.length);
        Bitmap bitmap = null;
        try {
            byte[] bitmapByte = Base64.decode(base64String, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapByte, 0, bitmapByte.length);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }
}
