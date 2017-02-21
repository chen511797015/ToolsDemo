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
    PrinterUtil mPrinterUtil = null;

    public static PrinterManager getInstance(Context mContext) {
        if (mPrinterManager == null) {
            mPrinterManager = new PrinterManager(mContext);
        }
        return mPrinterManager;
    }

    private PrinterManager(Context mContext) {
        this.mContext = mContext;
        if (null == mPrinterUtil) {
            mPrinterUtil = PrinterUtil.registerPrinter(mContext);
        }
    }

    public void prnBitmap(Bitmap bitmap) throws Exception {
        mPrinterUtil.printBitmap(bitmap);
    }


}
