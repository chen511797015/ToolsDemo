package cn.pax.toolsdemo.tool;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import cn.pax.toolsdemo.util.BitmapUtil;
import cn.pax.toolsdemo.util.PrinterUtil;


/**
 * Created by chendd on 2017/1/6.
 * 打印线程
 */

public class PrintImgThread implements Runnable {

    private static final String TAG = "PrintThread";


    Context mContext;
    int mResId;

    public PrintImgThread(Context mContext, int resId) {
        this.mContext = mContext;
        this.mResId = resId;
    }

    @Override
    public void run() {
        print();
    }

    /**
     * 打印
     */
    private void print() {
        try {
            ByteArrayOutputStream mBaos = new ByteArrayOutputStream();
            mBaos.write(PrinterConstants.ESC_ALIGN_CENTER);
            Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), mResId);
            Bitmap comp = BitmapUtil.comp(bitmap);
            mBaos.write(PrinterUtil.decodeBitmap(comp));
            PrinterUtil.writeData(mContext, mBaos.toByteArray());
            PrinterUtil.writeData(mContext, PrinterConstants.FullCut);
            //PrinterUtil.writeData(mContext, new byte[]{0x1b, 0x23, 0x23, 0x43, 0x54, 0x46, 0x44, (byte) 0x12c});//切刀前走纸距离,无效
            //PrinterUtil.writeData(mContext, new byte[]{0x1d, 0x56, 0x60});
            //PrinterUtil.writeData(mContext, new byte[]{0x1b, 0x23, 0x23, 0x41, 0x43, 0x46, 0x44, 0x00});打印切纸后走纸

        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "打印异常");
        }

    }
}
