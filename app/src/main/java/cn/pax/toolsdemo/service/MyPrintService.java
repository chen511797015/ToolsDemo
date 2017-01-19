package cn.pax.toolsdemo.service;

import android.printservice.PrintJob;
import android.printservice.PrintService;
import android.printservice.PrinterDiscoverySession;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by chendd on 2017/1/18.
 * 打印机服务
 */

public class MyPrintService extends PrintService {
    private static final String TAG = "MyPrintService";


    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate: -----------------");
        super.onCreate();
    }

    @Nullable
    @Override
    protected PrinterDiscoverySession onCreatePrinterDiscoverySession() {
        Log.e(TAG, "onCreatePrinterDiscoverySession: ");
        return null;
    }

    @Override
    protected void onConnected() {
        Log.e(TAG, "onConnected: ");
        super.onConnected();
    }

    @Override
    protected void onDisconnected() {
        Log.e(TAG, "onDisconnected: ");
        super.onDisconnected();
    }

    @Override
    protected void onRequestCancelPrintJob(PrintJob printJob) {
        Log.e(TAG, "onRequestCancelPrintJob: ");

    }

    @Override
    protected void onPrintJobQueued(PrintJob printJob) {
        Log.e(TAG, "onPrintJobQueued: ");




    }
}
