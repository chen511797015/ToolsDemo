package cn.pax.toolsdemo.tool;

import android.content.Context;

import cn.pax.toolsdemo.util.PrinterUtil;

/**
 * Created by chendd on 2017/1/20.
 */

public class PrintThread implements Runnable {

    Context mContext;
    byte[] mCommand;


    public PrintThread(Context mContext, byte[] mCommand) {
        this.mCommand = mCommand;
        this.mContext = mContext;
    }

    @Override
    public void run() {
        try {
            print();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void print() throws Exception {
        PrinterUtil.writeData(mContext, mCommand);
    }
}
