package com.hx.hxintelligence.utils;

public class UnicodeUtil {

    public static String decode(String unicodeStr) {
        if (unicodeStr == null) {
            return null;
        }
        StringBuffer retBuf = new StringBuffer();
        int maxLoop = unicodeStr.length();
        for (int i = 0; i < maxLoop; i++) {
            if (unicodeStr.charAt(i) == '\\') {
                if ((i < maxLoop - 5) && ((unicodeStr.charAt(i + 1) == 'u') || (unicodeStr.charAt(i + 1) == 'U')))
                    try {
                        retBuf.append((char) Integer.parseInt(unicodeStr.substring(i + 2, i + 6), 16));
                        i += 5;
                    } catch (NumberFormatException localNumberFormatException) {
                        retBuf.append(unicodeStr.charAt(i));
                    }
                else
                    retBuf.append(unicodeStr.charAt(i));
            } else {
                retBuf.append(unicodeStr.charAt(i));
            }
        }
        return retBuf.toString();
    }


    public static String decode2(String unicodeStr) {
        if (unicodeStr == null) {
            return null;
        }
        StringBuffer stringBuffer = new StringBuffer();
        int maxLoop = unicodeStr.length();
        for (int i = 0; i < maxLoop; i++) {
            if (unicodeStr.charAt(i) == '&' && unicodeStr.charAt(i + 1) == '#') {
                int endNode = -1; // 结束节点.
                for (int j = i + 2; j < i + 10; j++) {
                    if (unicodeStr.charAt(j) == ';') {
                        endNode = j;
                        break;
                    }
                }
                if (endNode != -1) {
                    char c = (char) Integer.parseInt(unicodeStr.substring(i + 2, endNode), 10);
                    stringBuffer.append(c);
                    i = endNode;
                }
            }
        }
        return stringBuffer.toString();
    }
}
