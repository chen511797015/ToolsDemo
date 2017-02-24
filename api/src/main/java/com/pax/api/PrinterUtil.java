package com.pax.api;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.UnsupportedEncodingException;


/**
 * Created by chendd on 2017/2/21.
 */

public class PrinterUtil implements PrinterInterface {

    static Context mContext;
    static UsbAdmin mUsbAdmin = null;
    static PrinterUtil mPrinterUtil = null;

    public static PrinterUtil registerPrinter(Context mContext) {
        if (null == mPrinterUtil) {
            mPrinterUtil = new PrinterUtil(mContext);
        }
        return mPrinterUtil;
    }

    private PrinterUtil(Context mContext) {
        if (null == mUsbAdmin) {
            mUsbAdmin = new UsbAdmin(mContext);
        }
        mUsbAdmin.OpenPort("");
    }


    @Override
    public int writeData(String mData) {
        int ret = -1;
        if (mUsbAdmin != null) {
            try {
                ret = mUsbAdmin.WriteData(mData.getBytes("GBK"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    @Override
    public int writeData(String mData, String mCode) {
        if (!"GBK".equals(mCode)) {
            mCode = "GBK";
        }
        int ret = -1;
        if (mUsbAdmin != null) {
            try {
                ret = mUsbAdmin.WriteData(mData.getBytes(mCode));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    @Override
    public int writeData(byte[] mBytes) {
        int ret = -1;
        if (mUsbAdmin != null) {
            ret = mUsbAdmin.WriteData(mBytes);
        }
        return ret;
    }

    @Override
    public int printBitmap(Bitmap bitmap) throws Exception {
        int ret = 0;
        if (mUsbAdmin != null) {
            // ret = mUsbAdmin.WriteData(mBytes);
            for (Bitmap bm : BitmapUtils.getBitmapList(bitmap)) {
                //打印失败的时候,保存一下数据,带连接上之后,继续打印
                ret = mUsbAdmin.WriteData(BitmapUtils.decodeBitmap(bm));
                if (ret == -1) {
                    throw new Exception("打印机连接异常,请检查打印机状态...");
                }
            }
        }
        return ret;
    }

    @Override
    public int readData(byte[] var1) throws Exception {
        int ret = 0;
        if (mUsbAdmin != null) {
            ret = mUsbAdmin.ReadData(var1);
        }
        return ret;
    }

    @Override
    public int readData(byte[] var1, int var2, int var3) throws Exception {
        int ret = 0;
        if (mUsbAdmin != null) {
            mUsbAdmin.ReadData(var1, var2, var3);
        }
        return ret;
    }

}
