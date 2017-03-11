package com.shssjk.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by admin on 2016/7/28.
 */
public class SSUtils {

    public static String getHost(String url) {
        if (SPStringUtils.isEmpty(url)) {
            return null;
        }
        if (url.startsWith("http://") || url.startsWith("https://")) {
            return url = url.replaceAll("http://", "").replaceAll("https://", "");
        }
        return url;
    }


    /**
     * @throws
     * @Title: isNetworkAvaiable
     * @Description:(是否打开网络)
     * @param: @param pContext
     * @param: @return
     * @return: boolean
     */
    public static boolean isNetworkAvaiable(Context pContext) {
        boolean isAvaiable = false;
        ConnectivityManager cm = (ConnectivityManager) pContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isAvailable()) {
            isAvaiable = true;
        }
        return isAvaiable;
    }


    public static String convertFullTimeFromPhpTime(long phpTime) {
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
        } catch (NumberFormatException e) {
            String negativeMode = "";
            if (str.indexOf('-') != -1)
                negativeMode = "-";
            str = str.replaceAll("-", "");
            if (str.indexOf('.') != -1) {
                str = str.substring(0, str.indexOf('.'));
                if (str.length() == 0) {
                    return (Integer) 0;
                }
            }
            String strNum = str.replaceAll("[^\\d]", "");
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
     * 查找字符串中的数字
     *
     * @param content
     * @return
     */
    public static String getNumbers(String content) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            return matcher.group(0);
        }
        return "";
    }

    // 从assets 文件夹中获取文件并读取数据
    public static String getFromAssets(String fileName, Context context) {
        String result = "";
        try {
            InputStream in = context.getResources().getAssets().open(fileName);
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

    public static Double string2double(String content) {
        if (!SSUtils.isEmpty(content)) {
            return Double.parseDouble(content);
        }
        return 0.0;
    }

//    float   转 string

    public static String float2String(Float content) {
        if (!SSUtils.isEmpty(content)) {
            String f1Str = Float.toString(content);
            return f1Str.split("\\.")[0];
        }


        return "";
    }
    public static String RemoveStrPointAftet0(String content) {
        if (!SSUtils.isEmpty(content)) {
            return content.split("\\.")[0];
        }
        return "";
    }


//    string   转 float
    public static Float string2float(String content) {
//        BigDecimal b2 = new BigDecimal(content);



        if (!SSUtils.isEmpty(content)) {
            return Float.parseFloat(content);
        }
        return 0.00f;
    }
    /**
     *银联支付乘以100
     * @param content
     * @return
     */
    public static BigDecimal stringMul100(String content) {
        if (SSUtils.isEmpty(content)) {
            return new BigDecimal("0");
        }
        BigDecimal b2 = new BigDecimal(content);
        BigDecimal b =new BigDecimal("100");
        b2.multiply(b);
        return b2.multiply(b);
    }
    /*
     * 将时间戳转换为时间
     */
    public static String stampToDate(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }


    /**
     * Java将Unix时间戳转换成指定格式日期字符串
     *
     * @param timestampString 时间戳 如："1473048265";
     * @param formats         要格式化的格式 默认："yyyy-MM-dd HH:mm:ss";
     * @return 返回结果 如："2016-09-05 16:06:42";
     */
    public static String TimeStamp2Date(String timestampString, String formats) {
        if (TextUtils.isEmpty(formats))
            formats = "yyyy-MM-dd HH:mm:ss";
        Long timestamp = Long.parseLong(timestampString) * 1000;
        String date = new SimpleDateFormat(formats, Locale.CHINA).format(new Date(timestamp));
        return date;
    }

    /**
     * 动态设置ListView的高度
     *
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        if (listView == null) return;
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }


    //计算合格率  是否大于等于 0.7
    public static boolean calculateReate(int sum, int normal) {
        if (sum != 0) {
            double pas = (double) normal / (double) sum;
            if (pas >= 0.7) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }
//    判断是否为数字
    public  static boolean isNumber(String str){
        if(isEmpty(str)){
            return false;
        }
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }
    /**
     * 实际替换动作
     *
     * @param username username
     * @param regular  正则
     * @return
     */
    private static String replaceAction(String username, String regular) {
        return username.replaceAll(regular, "*");
    }
    /**
     * 根据用户名的不同长度，来进行替换 ，达到保密效果
     * @param userName 用户名
     * @return 替换后的用户名
     */
    public static String userNameReplaceWithStar(String userName) {
        String userNameAfterReplaced = "";

        if (userName == null){
            userName = "";
        }

        int nameLength = userName.length();

        if (nameLength <= 1) {
            userNameAfterReplaced = "*";
        } else if (nameLength == 2) {
            userNameAfterReplaced = replaceAction(userName, "(?<=\\d{0})\\d(?=\\d{1})");
        } else if (nameLength >= 3 && nameLength <= 6) {
            userNameAfterReplaced = replaceAction(userName, "(?<=\\d{1})\\d(?=\\d{1})");
        } else if (nameLength == 7) {
            userNameAfterReplaced = replaceAction(userName, "(?<=\\d{1})\\d(?=\\d{2})");
        } else if (nameLength == 8) {
            userNameAfterReplaced = replaceAction(userName, "(?<=\\d{2})\\d(?=\\d{2})");
        } else if (nameLength == 9) {
            userNameAfterReplaced = replaceAction(userName, "(?<=\\d{2})\\d(?=\\d{3})");
        } else if (nameLength == 10) {
            userNameAfterReplaced = replaceAction(userName, "(?<=\\d{3})\\d(?=\\d{3})");
        } else if (nameLength >= 11) {
            userNameAfterReplaced = replaceAction(userName, "(?<=\\d{3})\\d(?=\\d{4})");
        }
        return userNameAfterReplaced;
    }


    public static String getTwoPointFloatStr(float value){
        DecimalFormat df = new DecimalFormat("0.00000000000");
        return df.format(value);
    }
    /**
     * 银行卡后4位显示
     * 使用 "**** **** **** "+repalceAndHideBankNum(str)
     * 输出样式**** **** **** 5454
     * @param str
     * @return
     */
    public static String repalceAndHideBankNum(String str) {
        StringBuilder stringBuilder = new StringBuilder(str);
        if (str.length() > 4) {
            stringBuilder.replace(0, str.length() - 4, "");
        }
        return "**** **** **** "+stringBuilder;
    }

    /**
     * 计算两个时间戳的时间差
     * @param hqtime  为原时间戳
     * @return 时间差 单位为小时
     */
    public static Long getTimeLag(long hqtime) {
        if (hqtime == 0) {
            return 0L;
        } else {
            Long s = (System.currentTimeMillis() - hqtime) / (1000 * 60 * 60);
            return s;
        }
    }


}
