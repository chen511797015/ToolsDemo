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
        mUsbAdmin.OpenPort("USB_PRINTER");
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
        int ret = -1;
        if (mUsbAdmin != null) {
            // ret = mUsbAdmin.WriteData(mBytes);
            for (Bitmap bm : BitmapUtils.getBitmapList(bitmap)) {
                ret = mUsbAdmin.WriteData(BitmapUtils.decodeBitmap(bm));
            }
            if (ret == -1) {
                throw new Exception("打印机连接异常,请检查打印机状态...");
            }
        }
        return ret;
    }


}
