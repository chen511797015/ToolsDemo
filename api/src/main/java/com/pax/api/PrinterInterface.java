package com.pax.api;

import android.graphics.Bitmap;

/**
 * Created by chendd on 2017/2/21.
 */

public interface PrinterInterface {

    int writeData(String mData) throws Exception;

    int writeData(String mData, String mCode) throws Exception;

    int writeData(byte[] mBytes) throws Exception;

    int printBitmap(Bitmap bitmap) throws Exception;

    int readData(byte[] var1) throws Exception;

    int readData(byte[] var1, int var2, int var3) throws Exception;

}
