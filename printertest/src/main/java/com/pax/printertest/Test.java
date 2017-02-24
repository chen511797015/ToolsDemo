package com.pax.printertest;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.input.InputManager;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.hardware.usb.UsbRequest;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.InputDevice;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import java.nio.ByteBuffer;
import java.util.HashMap;

/**
 * Created by chendd on 2017/2/22.
 */

public class Test extends Activity {

    private UsbManager mUsbManager;
    private UsbDevice mUsbDevice;
    private UsbDeviceConnection mUsbConnection;
    private InputManager inputManager;
    private UsbEndpoint epOut, epIn;
    private UsbInterface mUsbInterface;

    private final int VendorID = 4400;
    private final int ProductID = 12594;
    private boolean device_null = true;
    private final String ACTION_USB_PERMISSION = "com.hhd.USB_PERMISSION";
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        button = (Button) findViewById(R.id.button1);
        mUiThread = Thread.currentThread();
        mUsbManager = (UsbManager) getSystemService(USB_SERVICE);
        HashMap<String, UsbDevice> map = mUsbManager.getDeviceList();
        for (UsbDevice device : map.values()) {
            if (device.getVendorId() == VendorID && device.getProductId() == ProductID) {
                System.out.println("找到，进来了");
                setDevice(device);
                device_null = false;
            }
        }
        button.setOnClickListener(new MyButton());
        readThread = new ReadThread();
        readThread.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //设备连接或移除的广播
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action))/*连接*/ {
                UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE); //拿到连接的设备
                inputManager = (InputManager) Test.this.getSystemService(INPUT_SERVICE);
                int[] id_device = inputManager.getInputDeviceIds();//获取所有的设备id
                InputDevice inputDevice = inputManager.getInputDevice(id_device[id_device.length - 1]);
                System.out.println("挂载的设备是什么：" + inputDevice + "\t设备：" + mUsbDevice);
                if (device.getVendorId() == VendorID && device.getProductId() == ProductID) {
                    close();
                    setDevice(device);
                    device_null = false;
                }
            } else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action))/*移除*/ {
                UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                 /*关闭连接*/
                close();
            }
        }
    };

    /**
     * 打开连接
     *
     * @param
     */
    private boolean openDevice() {
        if (mUsbDevice == null)
            return false;
        closeDevice();
        mUsbConnection = mUsbManager.openDevice(mUsbDevice);
        System.out.println("打开连接：" + mUsbConnection);
        if (mUsbConnection != null)
            return mUsbConnection.claimInterface(mUsbInterface, true);
        return false;
    }

    /**
     * UsbInterface
     * 进行端点设置和通讯
     *
     * @param intf
     */
    private void setEndpoint(UsbInterface intf) {
        if (intf == null)
            return;
        //设置接收数据的端点
        if (intf.getEndpoint(0) != null) {
            epIn = intf.getEndpoint(0);
        }
        //当端点为2的时候
        if (intf.getEndpointCount() == 2) {
            //设置发送数据的断点
            if (intf.getEndpoint(1) != null)
                epOut = intf.getEndpoint(1);
        }
    }

    /*-----------------------------------------------------------------------*/
    private ReadThread readThread;

    /**
     * 开启一个线程来处理数据，或对权限的判断
     *
     * @author FLT-PC
     */
    private class ReadThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (!isInterrupted()) {
                final byte[] buffer = receiveUsbData();
                if (buffer != null)
                    onDataReceived(buffer);
            }
        }
    }

    /**
     * 接收到的数据进行处理,并且执行发送数据
     *
     * @param buffer 传过来的数据
     */
    private void onDataReceived(final byte[] buffer) {
        runOnUiThread1(new Runnable() {
            @Override
            public void run() {
                System.out.println("来；了");
                for (int i = 0; i < buffer.length; i++) {
                    Log.e("onDataReceived-->", "这个数据是：" + buffer[i]);
                }
            }
        });
    }

    //检测到设备，并赋值
    private void setDevice(UsbDevice device) {
        if (device == null)
            return;
        if (mUsbDevice != null)
            mUsbDevice = null;
        mUsbDevice = device;
        //获取设备接口，一般都是一个接口，可以打印getInterfaceCount()查看接口个数，在这个接口上有两个端点，OUT和IN
        mUsbInterface = mUsbDevice.getInterface(0);
        setEndpoint(mUsbInterface);
    }

    private boolean b = false;

    /*--------------------------发送数据-----------------------------------*/
    //该方法是为了，应对某种设备需要先发数据才可以接受
    private int sendDataBulkTransfer() {
        byte[] data = new byte[6];
        data[0] = 0x42;
        data[1] = 0x45;
        data[2] = 0x47;
        data[3] = 0x49;
        data[4] = 0x4f;
        data[5] = 0x01;
        final int length = data.length;
        int ref = -100;
        if (epOut != null) {
            ref = mUsbConnection.bulkTransfer(epOut, data, length, 1000);
            mUsbConnection.claimInterface(mUsbInterface, true);
        }
        Log.d("成功了没------>", "ref=" + ref);
        return ref;
    }

    /**
     * 用ControlTransfer发送数据
     *
     * @return
     */
    private int sendDataControlTransfer() {
        mUsbInterface = mUsbDevice.getInterface(0);
        setEndpoint(mUsbInterface);
        byte[] buffer = new byte[6];
        buffer[0] = 0x42;
        buffer[1] = 0x45;
        buffer[2] = 0x47;
        buffer[3] = 0x49;
        buffer[4] = 0x4f;
        buffer[5] = 0x01;
        final int length = buffer.length;
        int ref = -100;
        //发送  COM
        if (epOut != null) {
            ref = mUsbConnection.controlTransfer(0x01, 0x06, 0x02, 0x01, buffer, length, 1000);
            mUsbConnection.claimInterface(mUsbInterface, true);
        }
        return ref;
    }

    /**
     * 用UsbRequest发送数据
     *
     * @return
     */
    private int sendDataUsbRequest() {
        mUsbInterface = mUsbDevice.getInterface(0);
        setEndpoint(mUsbInterface);
        byte[] buffer = new byte[6];
        buffer[0] = 0x66;
        buffer[1] = 0x69;
        buffer[2] = 0x71;
        buffer[3] = 0x73;
        buffer[4] = 0x78;
        buffer[5] = 0x01;
        ByteBuffer buffer2 = ByteBuffer.wrap(buffer);
        UsbRequest request = new UsbRequest();
        request.setClientData(this);
        request.initialize(mUsbConnection, epOut);
        boolean b = request.queue(buffer2, buffer.length);
        if (mUsbConnection.requestWait() == request) {
            request.close();
            return 1;
        }
        request.close();
        return b ? 1 : -1;
    }

    /*---------------------------发送数据的所有擦、测试方法结束----------------------------------------*/
    //接收数据的方法
    private byte[] receiveData() {
        if (epIn == null || mUsbConnection == null)
            return null;
        byte[] by = null;
        Log.d("接收数据------》", "receiveData");
        int inMax = epIn.getMaxPacketSize();
        ByteBuffer byteBuffer = ByteBuffer.allocate(inMax);
        UsbRequest usbRequest = new UsbRequest();
        usbRequest.setClientData(this);
        usbRequest.initialize(mUsbConnection, epIn);
        boolean l = usbRequest.queue(byteBuffer, inMax);
        if (mUsbConnection.requestWait() == usbRequest) {
            byte[] retData = byteBuffer.array();
            by = new byte[retData.length];
            int i = 0;
            for (Byte byte1 : retData) {
                by[i] = byte1;
                i++;
                System.err.println("这个数据是：" + byte1);
            }
        }
        usbRequest.close();
        return by;
    }

    /**
     * 用UsbConnection.bulkTransfer接收数据
     *
     * @return
     */
    private byte[] receiveBulkTransferData() {
        mUsbInterface = mUsbDevice.getInterface(0);
        setEndpoint(mUsbInterface);
        byte[] buffer = new byte[epIn.getMaxPacketSize()];
        //sendDataUsbRequest();
        int n = mUsbConnection.bulkTransfer(epIn, buffer, epIn.getMaxPacketSize(), 1000);
        Log.d("接收成功了没------>", "n=" + n);
        if (n > 0)
            return buffer;
        return null;
    }

    /**
     * 用UsbConnection.controlTransfer接收数据
     *
     * @return
     */
    private byte[] receiveControlTransferData() {
        byte[] buffer = new byte[epIn.getMaxPacketSize()];
        int n = mUsbConnection.controlTransfer(0x00, 0x06, 0x00, 0x00, buffer, epIn.getMaxPacketSize(), 1000);
        if (n > 0)
            return buffer;
        return null;
    }

    /**
     * 接收数据和打开连接处理的关键
     *
     * @return
     */
    private byte[] receiveUsbData() {
        if (mUsbDevice != null && mUsbManager.hasPermission(mUsbDevice)) {
            if (device_null) {
                device_null = false;
                System.out.println("该设备获取到了权限了");
            }
            //当b为true时，则表示连接已经打开了,接着为接收数据
            if (b == true) {
                return otherData();//receiveBulkTransferData();//receiveData();//receiveUsbRequestData();
            } else/*此时连接还未打开，执行打开连接步骤*/ {
                HashMap<String, UsbDevice> map = mUsbManager.getDeviceList();
                for (UsbDevice device : map.values()) {
                    if (device.getVendorId() == VendorID && device.getProductId() == ProductID) {
                        setDevice(device);
                        device_null = false;
                    }
                }
                b = openDevice();
            }
        }
        return null;
    }

    /**
     * 用UsbRequest
     * 接收数据
     *
     * @return
     */
    private byte[] receiveUsbRequestData() {
        //当对象为空时下面步骤将不执行
        if (mUsbConnection == null || epIn == null)
            return null;
        ByteBuffer byteBuffer = ByteBuffer.allocate(6);
        final UsbRequest usbRequest = new UsbRequest();
        usbRequest.initialize(mUsbConnection, epIn);
        usbRequest.queue(byteBuffer, 6);
        if (mUsbConnection.requestWait() == usbRequest) {
            System.out.println("拿到数据了");
            usbRequest.close();
            try {
                finalize();
            } catch (Throwable e) {
                e.printStackTrace();
            }
            return byteBuffer.array();
        }
        usbRequest.close();
        try {
            finalize();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 又一个接收数据的方法
     *
     * @return
     */
    private byte[] otherData() {
        byte[] buffer = new byte[8];
        mUsbConnection.controlTransfer(UsbConstants.USB_TYPE_VENDOR | UsbConstants.USB_DIR_IN, 0x5f, 0, 0, buffer, 8, 1000);
        mUsbConnection.controlTransfer(UsbConstants.USB_TYPE_VENDOR | UsbConstants.USB_DIR_OUT, 0xa1, 0, 0, null, 0, 1000);
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
        m = mUsbConnection.controlTransfer(UsbConstants.USB_TYPE_VENDOR | UsbConstants.USB_DIR_OUT, 0x9a, 0x1312, a, null, 0, 1000);
        Log.d("m1----->", "m=" + m);
        m = mUsbConnection.controlTransfer(UsbConstants.USB_TYPE_VENDOR | UsbConstants.USB_DIR_OUT, 0x9a, 0x0f2c, b, null, 0, 1000);
        Log.d("m2----->", "m=" + m);
        m = mUsbConnection.controlTransfer(UsbConstants.USB_TYPE_VENDOR | UsbConstants.USB_DIR_IN, 0x95, 0x2518, 0, buffer, 8, 1000);
        Log.d("m3----->", "m=" + m);
        m = mUsbConnection.controlTransfer(UsbConstants.USB_TYPE_VENDOR | UsbConstants.USB_DIR_OUT, 0x9a, 0x0518, 0x0050, null, 0, 1000);
        Log.d("m4----->", "m=" + m);
        m = mUsbConnection.controlTransfer(UsbConstants.USB_TYPE_VENDOR | UsbConstants.USB_DIR_OUT, 0xa1, 0x501f, 0xd90a, null, 0, 1000);
        Log.d("m5----->", "m=" + m);
        m = mUsbConnection.controlTransfer(UsbConstants.USB_TYPE_VENDOR | UsbConstants.USB_DIR_OUT, 0x9a, 0x1312, a, null, 0, 1000);
        Log.d("m6----->", "m=" + m);
        m = mUsbConnection.controlTransfer(UsbConstants.USB_TYPE_VENDOR | UsbConstants.USB_DIR_OUT, 0x9a, 0x0f2c, b, null, 0, 1000);
        Log.d("m7----->", "m=" + m);
        m = mUsbConnection.controlTransfer(UsbConstants.USB_TYPE_VENDOR | UsbConstants.USB_DIR_OUT, 0xa4, 0, 0, null, 0, 1000);
        Log.d("m8----->", "m=" + m);
        return buffer;
    }

    private Handler handler = new Handler();

    /**
     * 关闭连接，情况部分对象
     *
     * @param
     * @param
     */
    private void closeDevice() {
        if (mUsbConnection != null) {
            synchronized (mUsbConnection) {
                mUsbConnection.releaseInterface(mUsbInterface);
                mUsbConnection.close();
                mUsbConnection = null;
                epOut = null;
                epIn = null;
            }
        }
    }

    /**
     * 关闭连接，情况所有对象
     *
     * @return
     */
    private boolean close() {
        if (mUsbConnection != null) {
            synchronized (mUsbConnection) {
                mUsbConnection.releaseInterface(mUsbInterface);
                mUsbConnection.close();
                mUsbConnection = null;
                epOut = null;
                epIn = null;
                mUsbInterface = null;
                mUsbDevice = null;
                return true;
            }
        }
        return false;
    }

    /**
     * 单击发送数据按钮发送数据
     *
     * @author FLT-PC
     */
    class MyButton implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            //sendDataBulkTransfer();
            if (mUsbManager.hasPermission(mUsbDevice))
                sendDataUsbRequest();
        }
    }

    private Thread mUiThread;

    public final void runOnUiThread1(Runnable runnable) {
        if (Thread.currentThread() != mUiThread) {
            handler.post(runnable);
        } else {
            runnable.run();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_USB_PERMISSION);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        unregisterReceiver(receiver);
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_USB_PERMISSION);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        registerReceiver(receiver, filter);
        HashMap<String, UsbDevice> map = mUsbManager.getDeviceList();
        mUiThread = Thread.currentThread();
        for (UsbDevice device : map.values()) {
            if (device.getVendorId() == VendorID && device.getProductId() == ProductID) {
                close();
                setDevice(device);
                device_null = false;
            }
        }
        openDevice();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(receiver);
    }


}
