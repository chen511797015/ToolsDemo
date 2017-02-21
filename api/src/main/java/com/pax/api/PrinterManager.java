package com.pax.api;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * Created by chendd on 2017/2/21.
 */

public class PrinterManager {

    private static final String TAG = "PrinterManager";
    Context mContext;
    static PrinterManager mPrinterManager = null;
    UsbAdmin mUsbAdmin = null;

    public static PrinterManager getInstance(Context mContext) {
        if (mPrinterManager == null) {
            mPrinterManager = new PrinterManager(mContext);
        }
        return mPrinterManager;
    }

    PrinterManager(Context mContext) {
        this.mContext = mContext;
        mUsbAdmin = new UsbAdmin(mContext);
        mUsbAdmin.OpenPort("USB_PRINTER");
    }


    public void prnBitmap(Bitmap bitmap) {


    }


}
