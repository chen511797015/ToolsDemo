package cn.pax.toolsdemo.util;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbConstants;
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
        try {
            if (mDevice != null) {
                findPrintDevice(mDevice);
                if (mConnection == null) {
                    HashMap<String, UsbDevice> deviceList = mUsbManager.getDeviceList();
                    Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
                    while (deviceIterator.hasNext()) {
                        UsbDevice device = deviceIterator.next();
                        if (device.getVendorId() == 1157 && device.getProductId() == 30017 ||
                                device.getVendorId() == 10473 && device.getProductId() == 649)
                            mUsbManager.requestPermission(device, mPermissionIntent);
                    }
                }
            } else {
                HashMap<String, UsbDevice> deviceList = mUsbManager.getDeviceList();
                Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
                while (deviceIterator.hasNext()) {
                    UsbDevice device = deviceIterator.next();
                    if (device.getVendorId() == 1157 && device.getProductId() == 30017 ||
                            device.getVendorId() == 10473 && device.getProductId() == 649)
                        mUsbManager.requestPermission(device, mPermissionIntent);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 识别usb打印机
     *
     * @param device
     */
    @SuppressLint("NewApi")
    private void findPrintDevice(UsbDevice device) {
        if (device != null) {
            mDevice = device;
            UsbInterface mUsbInterface = null;
            UsbEndpoint ep = null;
            int interfaceCount = device.getInterfaceCount();
            int deviceClass = device.getDeviceClass();
            if (deviceClass == 0 & interfaceCount != 0) {
                for (int i = 0; i < interfaceCount; i++) {
                    mUsbInterface = device.getInterface(i);
                    int interfaceClass = mUsbInterface.getInterfaceClass();
                    if (interfaceClass == UsbConstants.USB_CLASS_PRINTER) {
                        int endpointCount = mUsbInterface.getEndpointCount();
                        for (int j = 0; j < endpointCount; j++) {
                            UsbEndpoint endpoint = mUsbInterface.getEndpoint(j);
                            if (endpoint.getDirection() == 0 && endpoint.getType() == UsbConstants.USB_ENDPOINT_XFER_BULK) {
                                ep = endpoint;
                                Log.d(TAG, "接口是: " + i + "      类是: " + mUsbInterface.getInterfaceClass() +
                                        "       端点是: " + j
                                        + "     方向是: " + endpoint.getDirection() +
                                        "       类型是: " + endpoint.getType());
                                //break;
                            }

                        }
                    }

                }
            }

            mEndpointIntr = ep;
            if (mDevice != null && mUsbInterface != null) {
                UsbDeviceConnection connection = mUsbManager.openDevice(mDevice);
                boolean claimInterface = connection.claimInterface(mUsbInterface, true);//申明接口
                if (connection != null && claimInterface) {
                    Log.d(TAG, "usb设备连接成功!");
                    mConnection = connection;
                } else {
                    Log.d(TAG, "usb设备连接失败!");
                    mConnection = null;
                }
            }
        }
    }


    /**
     * 批量传输数据
     */
    public boolean sendCommand(byte[] mContent) {
        boolean result;
        synchronized (this) {
            int len = -1;
            if (mConnection != null) {
                // 批量传输数据: 传输方向,传输内容,传输内容长度,超时时间
                len = mConnection.bulkTransfer(mEndpointIntr, mContent, mContent.length, 10000);
            }
            if (len < 0) {
                //打印失败,尝试打开打印机
                Log.d(TAG, "发送失败");
                openUsb();
                result = false;
            } else {
                Log.d(TAG, "发送成功");
                result = true;
            }
        }
        return result;
    }


    /**
     * 读取数据
     */
    public byte[] readFromUsb() {
        return null;
    }

    /**
     * 关闭
     */
    public void closeUsb() {
        if (mConnection != null) {
            mConnection.close();
            mConnection = null;
        }
    }

    /**
     * 判断usb连接状态
     *
     * @return
     */
    public boolean getUsbStatus() {
        if (mConnection == null)
            return false;
        else
            return true;
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
                            mConnection = null;
                        }
                    }

                }
            }
        }
    };
}
