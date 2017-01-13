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
        mFilter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        mFilter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
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
        StringBuffer sb = new StringBuffer();
        UsbInterface mUsbInterface = null;
        UsbEndpoint ep = null;
        String deviceName = device.getDeviceName();
        Log.i(TAG, "deviceName: " + deviceName);
        sb.append("deviceName: " + deviceName + "\r\n");
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

        sb.append("productId: " + productId + "\r\n");
        sb.append("vendorId: " + vendorId + "\r\n");
        sb.append("deviceId: " + deviceId + "\r\n");
        sb.append("interfaceCount: " + interfaceCount + "\r\n");
        sb.append("deviceClass: " + deviceClass + "\r\n");

        if (deviceClass == 0 & interfaceCount != 0) {
            for (int i = 0; i < interfaceCount; i++) {
                mUsbInterface = device.getInterface(i);
                int interfaceClass = mUsbInterface.getInterfaceClass();
                Log.i(TAG, "interfaceClass: " + interfaceClass);
                sb.append("interfaceClass: " + interfaceClass + "\r\n");
                String name = mUsbInterface.getName();
                Log.i(TAG, "name: " + name);
                sb.append("name: " + name + "\r\n");
            }
        }
        Log.e(TAG, sb.toString());
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
            //TODO usb设备的拔插
        }
    };


}
