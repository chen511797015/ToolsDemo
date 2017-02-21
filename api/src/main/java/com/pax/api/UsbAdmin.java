package com.pax.api;

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

import java.io.UnsupportedEncodingException;
import java.util.Iterator;

/**
 * Created by chendd on 2017/2/21.
 */

public class UsbAdmin implements IPort {

    private static final String TAG = "UsbAdmin";

    Context mContext;
    PendingIntent mPendingIntent = null;
    UsbManager mUsbManager = null;
    UsbDevice mUsbDevice = null;
    UsbDeviceConnection mConnection = null;
    UsbEndpoint mOutEndpoint = null;
    UsbEndpoint mInEndpoint = null;


    static boolean l = false;
    boolean L = false;
    static String iMsg = "";
    static String jMsg = "";

    int readTimeOut = 1000;
    int writeTimeOut = 1000;


    private static final String ACTION_USB_PERMISSION =
            "com.android.example.USB_PERMISSION";

    public UsbAdmin(Context mContext) {
        this.mContext = mContext;
        mPendingIntent = PendingIntent.getBroadcast(mContext, 0, new Intent(ACTION_USB_PERMISSION), 0);
        IntentFilter mFilter = new IntentFilter(ACTION_USB_PERMISSION);
        mContext.registerReceiver(mUsbReceiver, mFilter);
        jMsg = "HPRT";
    }

    public UsbAdmin(Context mContext, String var1) {
        this.mContext = mContext;
        mPendingIntent = PendingIntent.getBroadcast(mContext, 0, new Intent(ACTION_USB_PERMISSION), 0);
        IntentFilter mFilter = new IntentFilter(ACTION_USB_PERMISSION);
        mContext.registerReceiver(mUsbReceiver, mFilter);
        jMsg = var1;
    }

    @Override
    public void IsBLEType(boolean var1) {
        //蓝牙et设备
    }

    @Override
    public void SetReadTimeout(int var1) {
        this.readTimeOut = var1;
    }

    @Override
    public void SetWriteTimeout(int var1) {
        this.writeTimeOut = var1;
    }

    @Override
    public void InitPort() {
        //初始化打印机
    }

    @Override
    public boolean OpenPort(String var1) {
        mUsbManager = (UsbManager) mContext.getSystemService(Context.USB_SERVICE);
        Iterator<UsbDevice> mIterator = mUsbManager.getDeviceList().values().iterator();
        while (mIterator.hasNext()) {
            mUsbDevice = (UsbDevice) mIterator.next();
            int interfaceCount = mUsbDevice.getInterfaceCount();
            for (int i = 0; i < interfaceCount; i++) {
                if (mUsbDevice.getInterface(i).getInterfaceClass() == UsbConstants.USB_CLASS_PRINTER) {
                    //找到打印机类,然后请求权限
                    mUsbManager.requestPermission(mUsbDevice, mPendingIntent);
                    l = true;
                    return true;
                }
            }
        }
        l = false;
        return false;
    }

    @Override
    public boolean OpenPort(String var1, String var2) {
        return false;
    }

    @Override
    public boolean OpenPort(UsbDevice var1) {
        try {
            mUsbManager = (UsbManager) mContext.getSystemService(Context.USB_SERVICE);
            L = true;
            if (var1 != null) {
                //创建usb连接
                mConnection = null;
                mConnection = mUsbManager.openDevice(var1);
                if (mConnection == null) {
                    l = false;
                    return false;
                }
                //获取usb接口
                UsbInterface var2 = var1.getInterface(0);
                int var3;
                for (var3 = 0; var3 < var2.getEndpointCount(); var3++) {//根据接口获取usb端口数
                    UsbEndpoint var4 = var2.getEndpoint(var3);
                    //判断传输方向
                    if (UsbConstants.USB_CLASS_PRINTER == var2.getInterfaceClass() && var4.getDirection() == UsbConstants.USB_DIR_IN) {
                        mInEndpoint = var4;
                        if (var2.getEndpointCount() == 1) {
                            mOutEndpoint = var4;
                        }
                    }

                    //判断传输方向
                    if (UsbConstants.USB_CLASS_PRINTER == var2.getInterfaceClass() && var4.getDirection() == UsbConstants.USB_DIR_OUT) {
                        mOutEndpoint = var4;
                        if (var2.getEndpointCount() == 1) {
                            mInEndpoint = var4;
                        }
                    }
                }

                //创建连接,并申明接口
                mConnection = mUsbManager.openDevice(var1);
                mConnection.claimInterface(var2, true);
                byte[] var12 = mConnection.getRawDescriptors();

                byte[] var13 = new byte[255];
                byte var9 = var12[15];
                int var10;
                if ((var10 = mConnection.controlTransfer(128, 6, 768 | var9, 0, var13, 255, 0)) <= 2) {
                    iMsg = new String(var13, 2, var10, "ASCII");
                } else {
                    byte[] var11 = new byte[(var10 - 2) / 2];
                    var3 = 0;
                    for (int var5 = 2; var5 < var10; ++var5) {
                        if (var5 % 2 == 0) {
                            var11[var3] = var13[var5];
                            ++var3;
                        }
                    }
                    iMsg = new String(var11, "ASCII");
                }
                iMsg = iMsg.trim();
                try {
                    Thread.sleep(100L);
                } catch (InterruptedException var6) {
                    var6.printStackTrace();
                }
                l = true;
                Log.d(TAG, "connect Check Array Is Wrong!");
            } else {
                l = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            l = false;
            return false;
        }
        return l;
    }

    /**
     * 关闭usb连接
     *
     * @return
     */
    @Override
    public boolean ClosePort() {
        if (mConnection != null) {
            mConnection.close();
            mConnection = null;
            mUsbDevice = null;
            l = false;
        }
        return true;
    }

    @Override
    public int WriteData(byte[] var1) {
        return WriteData(var1, 0, var1.length);
    }

    @Override
    public int WriteData(byte[] var1, int var2) {
        return WriteData(var1, 0, var2);
    }

    @Override
    public int WriteData(byte[] var1, int var2, int var3) {
        try {
            byte[] var8 = new byte[10000];
            int var4 = var3 / 10000;

            //批量传输数据
            int var6;
            for (int var5 = 0; var5 < var4; var5++) {
                for (var6 = var5 * 10000; var6 < (var5 + 1) * 10000; ++var6) {
                    var8[var6 % 10000] = var1[var6];
                }
                //传输数据,超时时间
                mConnection.bulkTransfer(mOutEndpoint, var8, var8.length, writeTimeOut);
            }
            if (var3 % 1000 != 0) {
                byte[] var9 = new byte[var1.length - var4 * 10000];
                for (var6 = var4 * 10000; var6 < var1.length; ++var6) {
                    var9[var6 - var4 * 10000] = var1[var6];
                }
                //传输数据,超时时间
                mConnection.bulkTransfer(mOutEndpoint, var9, var9.length, writeTimeOut);
            }
            return var3;

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "print error!");
            return -1;
        }
    }

    @Override
    public int ReadData(byte[] var1) {
        try {
            return mConnection.bulkTransfer(mInEndpoint, var1, var1.length, readTimeOut);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public int ReadData(byte[] var1, int var2, int var3) {
        try {
            return mConnection.bulkTransfer(mInEndpoint, var1, var1.length, readTimeOut);
        } catch (Exception var4) {
            return -1;
        }
    }

    @Override
    public boolean IsOpen() {
        return l;
    }

    @Override
    public String GetPortType() {
        return "USB";
    }

    @Override
    public String GetPrinterName() {
        return iMsg;
    }

    @Override
    public String GetPrinterModel() {
        return iMsg;
    }


    BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    //获取用户点击返回权限信息
                    boolean fromUser = intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false);
                    if (!fromUser) {
                        //用户授权失败
                        return;
                    }
                    //boolean port = OpenPort(device);
                    if (device != null) {
                        //打开usb设备
                        OpenPort(device);
                    } else {
                        //关闭usb设备
                        ClosePort();
                    }
                }
            }
        }
    };
}
