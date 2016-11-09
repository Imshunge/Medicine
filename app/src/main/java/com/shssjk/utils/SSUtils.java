package com.shssjk.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.InputStream;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by admin on 2016/7/28.
 */
public class SSUtils {

    public static String getHost(String url){
        if (SPStringUtils.isEmpty(url)){
            return null;
        }
        if(url.startsWith("http://") || url.startsWith("https://")){
            return url = url.replaceAll("http://" , "").replaceAll("https://" , "");
        }
        return url;
    }


    /**
     *
     * @Title: isNetworkAvaiable
     * @Description:(是否打开网络)
     * @param: @param pContext
     * @param: @return
     * @return: boolean
     * @throws
     */
    public static boolean isNetworkAvaiable(Context pContext){
        boolean isAvaiable = false ;
        ConnectivityManager cm = (ConnectivityManager)pContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if(networkInfo!=null && networkInfo.isAvailable()){
            isAvaiable = true;
        }
        return isAvaiable;
    }


    public static String convertFullTimeFromPhpTime(long phpTime){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date(phpTime * 1000));
    }
    static public Integer str2Int(String str) {
        Integer result = null;
        if (null == str || 0 == str.length()) {
            return null;
        }
        try {
            result = Integer.parseInt(str);
        }
        catch (NumberFormatException e) {
            String negativeMode = "";
            if(str.indexOf('-') != -1)
                negativeMode = "-";
            str = str.replaceAll("-", "" );
            if (str.indexOf('.') != -1) {
                str = str.substring(0, str.indexOf('.'));
                if (str.length() == 0) {
                    return (Integer)0;
                }
            }
            String strNum = str.replaceAll("[^\\d]", "" );
            if (0 == strNum.length()) {
                return null;
            }
            result = Integer.parseInt(negativeMode + strNum);
        }
        return result;
    }

    /**
     * 判断对象是否为空
     *
     * @param obj
     * @return
     */
    public static boolean isEmpty(Object obj) {
        boolean result = true;
        if (obj == null) {
            return true;
        }
        if (obj instanceof String) {
            result = (obj.toString().trim().length() == 0) || obj.toString().trim().equals("null");
        } else if (obj instanceof Collection) {
            result = ((Collection) obj).size() == 0;
        } else {
            result = ((obj == null) || (obj.toString().trim().length() < 1)) ? true : false;
        }
        return result;
    }

    /**
     * 查找数字
     * @param content
     * @return
     */
    public  static String getNumbers(String content) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            return matcher.group(0);
        }
        return "";
    }
    // 从assets 文件夹中获取文件并读取数据
    public static String getFromAssets(String fileName,Context context) {
        String result = "";
        try {
            InputStream in =context.getResources().getAssets().open(fileName);
            // 获取文件的字节数
            int lenght = in.available();
            // 创建byte数组
            byte[] buffer = new byte[lenght];
            // 将文件中的数据读到byte数组中
            in.read(buffer);
            // result = EncodingUtils.getString(buffer, "UTF-8");
            result = new String(buffer, "GB2312");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
