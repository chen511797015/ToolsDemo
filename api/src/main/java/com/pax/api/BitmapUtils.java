package com.pax.api;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.bitmap;
import static android.content.ContentValues.TAG;

/**
 * Created by chendd on 2017/2/21.
 */

public class BitmapUtils {
    private static final String TAG = "BitmapUtils";

    public static List<Bitmap> getBitmapList(Bitmap bitmap) {
        List<Bitmap> pieces = new ArrayList<>();
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Log.e(TAG, "image width: " + width + ",image height:" + height);
        if (width > 576) {//此数据可修改,具体看打印机的位图最大宽度
            double ratio = (double) 576 / width;
            width = (int) 576;
            height *= ratio;
            bitmap = zoomBitmap(bitmap, width, height);
        }
        //int height = bitmap.getHeight();
        int count = height / 8;
        if (height < 8) {
            pieces.add(bitmap);
            return pieces;
        }
        for (int i = 0; i < count; i++) {
            Bitmap result = Bitmap.createBitmap(bitmap, 0, height / count * i,
                    bitmap.getWidth(), height / count);
            pieces.add(result);
        }
        return pieces;
    }

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

    /**
     * 压缩图片
     *
     * @param bmp          原始图片
     * @param desireWidth  压缩宽度
     * @param desireHeight 压缩高度
     * @return
     */
    private static Bitmap zoomBitmap(Bitmap bmp, int desireWidth, int desireHeight) {
        // 获得图片的宽高
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        // 设置想要的大小
        int newWidth = desireWidth;
        int newHeight = desireHeight;
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newBmp = Bitmap.createBitmap(bmp, 0, 0, width, height, matrix, true);
        return newBmp;
    }
}
