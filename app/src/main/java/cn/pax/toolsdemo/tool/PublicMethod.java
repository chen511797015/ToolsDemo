package cn.pax.toolsdemo.tool;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by chendd on 2016/11/9.
 */

public class PublicMethod {


    /**
     * 空白字符
     *
     * @param num
     * @return
     */
    public static String getSpaceByNum(int num) {
        String spaceStr = "";
        for (int i = 1; i < num; i++) {
            spaceStr += " ";
        }
        return spaceStr;
    }

    /**
     * 隐藏输入法
     *
     * @param context
     */
    public static void hideSoftForWindow(Context context) {
        if (((Activity) context).getCurrentFocus() != null) {
            InputMethodManager inputMethod = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethod.hideSoftInputFromWindow(((Activity) context).getCurrentFocus().getWindowToken(), 0);
        }
    }

    /**
     * 获取字符串个数
     *
     * @param s
     * @return
     */
    public static int getWordCount(String s) {
        s = s.replaceAll("[^\\x00-\\xff]", "**");
        int length = s.length();
        return length;
    }
}
