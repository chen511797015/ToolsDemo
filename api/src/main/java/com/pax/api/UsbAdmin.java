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
import android.hardware.usb.UsbRequest;
import android.util.Log;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Iterator;

import javax.security.auth.login.LoginException;


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
                    int productId = mUsbDevice.getProductId();
                    Log.d(TAG, "ProductId: " + productId + ",DeviceId" + mUsbDevice.getDeviceId());
//                    if (!(productId == 30017)) {
//                        mUsbManager.requestPermission(mUsbDevice, mPendingIntent);
//                        l = true;
//                        return true;
//                    }
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
            int length = -1;

            //批量传输数据
            int var6;
            for (int var5 = 0; var5 < var4; var5++) {
                for (var6 = var5 * 10000; var6 < (var5 + 1) * 10000; ++var6) {
                    var8[var6 % 10000] = var1[var6];
                }
                //传输数据,超时时间
                length = mConnection.bulkTransfer(mOutEndpoint, var8, var8.length, writeTimeOut);
            }
            if (var3 % 1000 != 0) {
                byte[] var9 = new byte[var1.length - var4 * 10000];
                for (var6 = var4 * 10000; var6 < var1.length; ++var6) {
                    var9[var6 - var4 * 10000] = var1[var6];
                }
                //传输数据,超时时间
                length = mConnection.bulkTransfer(mOutEndpoint, var9, var9.length, writeTimeOut);

            }

            if (length < 0) {
                Log.e(TAG, "print error...");
                OpenPort("");
            } else {
                Log.d(TAG, "print " + length + " byte...");
            }
            Log.d(TAG, "print end...");
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
            //int transfer = mConnection.bulkTransfer(mInEndpoint, var1, var1.length, readTimeOut);

//            byte[] by = null;
//            int inMax = mInEndpoint.getMaxPacketSize();
//            ByteBuffer byteBuffer = ByteBuffer.allocate(inMax);
//            UsbRequest usbRequest = new UsbRequest();
//            usbRequest.setClientData(this);
//            usbRequest.initialize(mConnection, mInEndpoint);
//            boolean l = usbRequest.queue(byteBuffer, inMax);
//            if (mConnection.requestWait() == usbRequest) {
//                byte[] retData = byteBuffer.array();
//                by = new byte[retData.length];
//                int i = 0;
//                for (Byte byte1 : retData) {
//                    by[i] = byte1;
//                    i++;
//                    Log.e(TAG, "这个数据是: " + byte1);
//                }
//            }
//            Log.e(TAG, "transfer: " + transfer);
            int inMax = mInEndpoint.getMaxPacketSize();
            byte[] buffer = new byte[inMax];
            buffer[0] = 0x1d;
            buffer[1] = 0x61;
            buffer[2] = 0x00;

            //ByteBuffer byteBuffer = ByteBuffer.allocate(inMax);
            ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);
            UsbRequest usbRequest = new UsbRequest();
            usbRequest.initialize(mConnection, mInEndpoint);
            //boolean queue = usbRequest.queue(byteBuffer, inMax);
            //Thread.sleep(2000);
            while (true) {
                usbRequest.queue(byteBuffer, inMax);
                if (mConnection.requestWait() == usbRequest) {
                    byte[] retData = byteBuffer.array();
                    try {
                        for (int i = 0; i < retData.length; i++) {
                            Log.e(TAG, "收到数据：" + retData[i]);
                        }
                        return 1;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // break;
                }
            }
            //return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }


    /**
     * 又一个接收数据的方法
     *
     * @return
     */
    private byte[] otherData() {
        byte[] buffer = new byte[8];
        mConnection.controlTransfer(UsbConstants.USB_TYPE_VENDOR | UsbConstants.USB_DIR_IN, 0x5f, 0, 0, buffer, 8, 1000);
        mConnection.controlTransfer(UsbConstants.USB_TYPE_VENDOR | UsbConstants.USB_DIR_OUT, 0xa1, 0, 0, null, 0, 1000);
        //baud rate
        int baudRate = 4800;
        long factor = 1532620800 / baudRate;
        int divisor = 3;
        while ((factor > 0xfff0) && (divisor > 0)) {
            factor >>= 3;
            divisor--;
        }
        factor = 0x10000 - factor;
        short a = (short) ((factor & 0xff00) | divisor);
        short b = (short) (factor & 0xff);
        int m = -100;
        m = mConnection.controlTransfer(UsbConstants.USB_TYPE_VENDOR | UsbConstants.USB_DIR_OUT, 0x9a, 0x1312, a, null, 0, 1000);
        Log.d("m1----->", "m=" + m);
        m = mConnection.controlTransfer(UsbConstants.USB_TYPE_VENDOR | UsbConstants.USB_DIR_OUT, 0x9a, 0x0f2c, b, null, 0, 1000);
        Log.d("m2----->", "m=" + m);
        m = mConnection.controlTransfer(UsbConstants.USB_TYPE_VENDOR | UsbConstants.USB_DIR_IN, 0x95, 0x2518, 0, buffer, 8, 1000);
        Log.d("m3----->", "m=" + m);
        m = mConnection.controlTransfer(UsbConstants.USB_TYPE_VENDOR | UsbConstants.USB_DIR_OUT, 0x9a, 0x0518, 0x0050, null, 0, 1000);
        Log.d("m4----->", "m=" + m);
        m = mConnection.controlTransfer(UsbConstants.USB_TYPE_VENDOR | UsbConstants.USB_DIR_OUT, 0xa1, 0x501f, 0xd90a, null, 0, 1000);
        Log.d("m5----->", "m=" + m);
        m = mConnection.controlTransfer(UsbConstants.USB_TYPE_VENDOR | UsbConstants.USB_DIR_OUT, 0x9a, 0x1312, a, null, 0, 1000);
        Log.d("m6----->", "m=" + m);
        m = mConnection.controlTransfer(UsbConstants.USB_TYPE_VENDOR | UsbConstants.USB_DIR_OUT, 0x9a, 0x0f2c, b, null, 0, 1000);
        Log.d("m7----->", "m=" + m);
        m = mConnection.controlTransfer(UsbConstants.USB_TYPE_VENDOR | UsbConstants.USB_DIR_OUT, 0xa4, 0, 0, null, 0, 1000);
        Log.d("m8----->", "m=" + m);

        for (int i = 0; i < buffer.length; i++) {
            Log.d(TAG, "otherData: " + buffer[i]);
        }

        return buffer;
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
                    if (fromUser) {
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
        }
    };
}
