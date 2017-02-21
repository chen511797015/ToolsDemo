package com.pax.api;

import android.graphics.Bitmap;

/**
 * Created by chendd on 2017/2/21.
 */

public class BitmapUtils {


    /**
     * 解码位图
     *
     * @param bitmap 要解码的位图资源文件
     * @return
     */
    public static byte[] decodeBitmap(Bitmap bitmap) {
        return decodeBitmap(bitmap, (byte) 0, (byte) 0);
    }

    /**
     * 解码位图
     *
     * @param bitmap 要解码的位图资源文件
     * @param var1   半色调模式
     * @param var2   缩放模式
     * @return
     */
    public static byte[] decodeBitmap(Bitmap bitmap, byte var1, byte var2) {
        PrinterDataCore var3;
        (var3 = new PrinterDataCore()).HalftoneMode = var1;
        var3.ScaleMode = var2;
        return var3.PrintDataFormat(bitmap);
    }
}
