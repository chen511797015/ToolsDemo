package com.pax.api;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.UnsupportedEncodingException;

import static android.R.attr.bitmap;

/**
 * Created by chendd on 2017/2/21.
 */

public class PrinterUtil implements PrinterInterface {

    static Context mContext;
    static UsbAdmin mUsbAdmin = null;

    public static UsbAdmin registerPrinter(Context mContext) {
        if (mUsbAdmin == null) {
            mUsbAdmin = new UsbAdmin(mContext);
        }
        return mUsbAdmin;
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
    public int printBitmap(Bitmap bitmap) {

        return 0;
    }






}
