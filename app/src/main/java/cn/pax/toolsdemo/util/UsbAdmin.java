package cn.pax.toolsdemo.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.util.Log;


import java.util.HashMap;
import java.util.Iterator;


/**
 * Created by chendd on 2017/1/12.
 */

public class UsbAdmin {

    private static final String TAG = "UsbAdmin";

    Context mContext;

    UsbManager mUsbManager;
    PendingIntent mPermissionIntent = null;
    UsbDevice mDevice;
    UsbDeviceConnection mConnection;//usb连接
    UsbEndpoint mEndpointIntr;

    static final String ACTION_USB_PERMISSION =
            "com.android.example.USB_PERMISSION";

    static final int USB_REQUEST_CODE = 10011;
    static final int USB_FLAG = 0;


    public UsbAdmin(Context mContext) {
        this.mContext = mContext;
        mUsbManager = (UsbManager) mContext.getSystemService(Context.USB_SERVICE);
        mPermissionIntent = PendingIntent.getBroadcast(mContext, USB_REQUEST_CODE, new Intent(ACTION_USB_PERMISSION), USB_FLAG);
        IntentFilter mFilter = new IntentFilter(ACTION_USB_PERMISSION);
        mContext.registerReceiver(mReceiver, mFilter);


    }


    /**
     * 打开usb设备
     */
    public void openUsb() {

        HashMap<String, UsbDevice> deviceList = mUsbManager.getDeviceList();
        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
        while (deviceIterator.hasNext()) {
            UsbDevice device = deviceIterator.next();
            mUsbManager.requestPermission(device, mPermissionIntent);
        }
    }

    /**
     * 识别usb打印机
     *
     * @param device
     */
    @SuppressLint("NewApi")
    private void findPrintDevice(UsbDevice device) {
        UsbInterface mUsbInterface = null;
        UsbEndpoint ep = null;
        String deviceName = device.getDeviceName();
        Log.i(TAG, "deviceName: " + deviceName);
        int productId = device.getProductId();
        Log.i(TAG, "productId: " + productId);
        int vendorId = device.getVendorId();
        Log.i(TAG, "vendorId: " + vendorId);
        int deviceId = device.getDeviceId();
        Log.i(TAG, "deviceId: " + deviceId);
        int interfaceCount = device.getInterfaceCount();
        Log.i(TAG, "interfaceCount: " + interfaceCount);
        int deviceClass = device.getDeviceClass();
        Log.i(TAG, "deviceClass: " + deviceClass);

        if (deviceClass == 0) {

            mUsbInterface = device.getInterface(0);
            int interfaceClass = mUsbInterface.getInterfaceClass();
            Log.i(TAG, "interfaceClass: " + interfaceClass);
            String name = mUsbInterface.getName();
            Log.i(TAG, "name: " + name);

        }


    }

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.e(TAG, "onReceive: " + action);
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    //获取用户点击返回权限信息
                    boolean fromUser = intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false);
                    Log.e(TAG, "用户授权是否授权: " + fromUser);
                    if (fromUser) {
                        if (device != null) {//设备不为空,找到usb打印机
                            findPrintDevice(device);
                        } else {
                            //关闭usb连接

                        }
                    }

                }
            }
        }
    };


}
