package cn.pax.toolsdemo.util;

import android.text.TextUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 数字工具
 */
public class NumberUtil {
    public static double formatDouble(double num) {
        DecimalFormat df = new DecimalFormat("#.000");
        double result = Double.parseDouble(df.format(num));
        return result;
    }

    /**
     * 格式化double数据，保留len长度小数，自动四舍五入
     *
     * @param num
     * @param len
     * @return
     */
    public static double formatDouble(double num, int len) {
        BigDecimal bd = new BigDecimal(num + "");
        bd = bd.setScale(len, BigDecimal.ROUND_DOWN);
        double result = bd.doubleValue();
        return result;
    }

    /**
     * 按给定位数截取浮点数
     *
     * @param num
     * @param len
     * @return
     */
    public static double cutDouble(double num, int len) {
        StringBuilder fm = new StringBuilder("#.");
        for (int i = 0; i < len; i++) {
            fm.append("0");
        }
        DecimalFormat df = new DecimalFormat(fm.toString());
        double result = Double.parseDouble(df.format(num));
        return result;
    }

    /**
     * 按给定位数截取浮点数
     *
     * @param num
     * @param len
     * @return
     */
    public static double cutDouble(String num, int len) {
        StringBuilder fm = new StringBuilder("#.");
        for (int i = 0; i < len; i++) {
            fm.append("0");
        }
        DecimalFormat df = new DecimalFormat(fm.toString());
        double result = Double.parseDouble(df.format(Double.valueOf(num)));
        return result;
    }

    /**
     * 截取两位小数
     *
     * @param num
     * @return
     */
    public static String cut2DoubleStr(double num) {
        DecimalFormat df = new DecimalFormat("#.00");
        return df.format(num);
    }

    /**
     * 截取两位小数
     *
     * @param numStr
     * @return
     */
    public static String cut2DoubleStr(String numStr) {
        if (TextUtils.isEmpty(numStr)) {
            numStr = "0";
        }
        DecimalFormat df = new DecimalFormat("#.00");
        return df.format(numStr);
    }

    /**
     * 格式化double数据，保留2位小数，自动四舍五入并放回字符串
     *
     * @param num
     * @return
     */
    public static String format2DoubleStr(String num) {
        if (TextUtils.isEmpty(num)) {
            num = "0";
        }
        BigDecimal bigDecimal = new BigDecimal(num);
        bigDecimal = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
        return bigDecimal.toString();
    }

    /**
     * 格式化double数据，保留2位小数，自动四舍五入并返回double
     *
     * @param num
     * @return
     */
    public static double format2Double(double num) {
        BigDecimal bigDecimal = new BigDecimal(num);
        bigDecimal = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
        return bigDecimal.doubleValue();
    }

    /**
     * 格式化double数据，保留2位小数，自动四舍五入并放回字符串
     *
     * @param num
     * @return
     */
    public static String format2DoubleStr(double num) {
        BigDecimal bigDecimal = new BigDecimal(num);
        bigDecimal = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
        return bigDecimal.toString();
    }

    /**
     * 截掉小数点取整
     *
     * @param num
     * @return
     */
    public static String formatDoubleToInt(double num) {
        String numStr = num + "";
        if (numStr.indexOf(".") > -1) {
            numStr = numStr.substring(0, numStr.indexOf("."));
        }
        return numStr;
    }
}
