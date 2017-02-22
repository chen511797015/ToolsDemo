package com.pax.api;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

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

    public void prnSetGray(int level) throws Exception {
        mPrinterUtil.writeData(PrinterConstants.setPrinterConcentration(level));
    }

    /**
     * 获取打印机状态
     *
     * @return
     */
    public byte prnStatus() throws Exception {
//        byte[] ret = new byte[]{0x1d, 0x67, 0x31};
//        int readData = mPrinterUtil.readData(ret);
        return 0;

    }

    public void prnInit() throws Exception {
        mPrinterUtil.writeData(new byte[]{0x1b, 0x40});
    }

    public void prnStr(String data) throws Exception {
        mPrinterUtil.writeData(data);
    }

    public void prnBytes(byte[] mBytes) throws Exception {
        mPrinterUtil.writeData(mBytes);
    }


    public void prnBitmap(Bitmap bitmap) throws Exception {
        mPrinterUtil.printBitmap(bitmap);
    }

    /**
     * 切刀
     *
     * @param step 1--全切
     *             0--半切
     */
    public void prnStartCut(int step) {
        if (1 == step) {
            mPrinterUtil.writeData(PrinterConstants.FullCut);
        } else {
            mPrinterUtil.writeData(PrinterConstants.HalfCut);
        }
    }


}
