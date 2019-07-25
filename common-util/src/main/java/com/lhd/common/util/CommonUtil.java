package com.lhd.common.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.MessageDigest;
import java.util.UUID;

/**
 * Created by lihongde on 2018/06/15.
 */
public class CommonUtil {

    private static Logger logger = LogManager.getLogger(CommonUtil.class);

    private static final String DEFAULT_ENCODING = "UTF-8";

    public static String md5(String text) {
        return md5(text, DEFAULT_ENCODING);
    }

    public static String md5(String text, String encoding) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return hex(md.digest(text.getBytes(encoding)));
        } catch (Exception e) {
            logger.error("encrpted error", e);
        }
        return null;
    }

    /**
     * 创建指定数量的随机字符串
     *
     * @param length     长度
     * @param numberFlag 是否是数字
     *
     */
    public static String createRandom(int length, boolean numberFlag) {
        String retStr = "";
        String strTable = numberFlag ? "1234567890" : "1234567890abcdefghijkmnpqrstuvwxyz";
        boolean flag = true;
        do {
            int count = 0;
            StringBuilder sb = new StringBuilder(retStr);
            for (int i = 0; i < length; i++) {
                int intR = (int) Math.floor(Math.random() * strTable.length());
                char c = strTable.charAt(intR);
                if (('0' <= c) && (c <= '9')) {
                    count++;
                }
                sb.append(strTable.charAt(intR));
            }
            retStr = sb.toString();
            if (count >= 2) flag = false;
        } while (flag);

        return retStr;
    }

    public static boolean isMobile(String mobile){
        return mobile != null && mobile.matches("1\\d{10}");
    }

    public static String getUuid(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    private static String hex(byte[] array) {
        StringBuilder sb = new StringBuilder();
        for (byte anArray : array) {
            sb.append(Integer.toHexString((anArray & 0xFF) | 0x100), 1, 3);
        }
        return sb.toString();
    }

}
