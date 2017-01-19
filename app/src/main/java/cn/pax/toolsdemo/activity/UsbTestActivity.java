package cn.pax.toolsdemo.activity;

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
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Logger;

import javax.security.auth.login.LoginException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pax.toolsdemo.R;
import cn.pax.toolsdemo.service.MyPrintService;

/**
 * 测试usb设备
 */

public class UsbTestActivity extends AppCompatActivity {


    private static final String TAG = "UsbTestActivity";

    @BindView(R.id.btn_print)
    Button btnPrint;
    @BindView(R.id.btn_info)
    Button btnInfo;
    @BindView(R.id.tv_usb_info)
    TextView tvUsbInfo;


    private static final String ACTION_USB_PERMISSION =
            "com.android.example.USB_PERMISSION";
    private UsbManager mManager;
    private UsbDevice mDevice;
    private UsbDeviceConnection mConnection;
    private UsbEndpoint mEndpoint;//usb端点

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usb_test);
        ButterKnife.bind(this);

        startService(new Intent(this, MyPrintService.class));

        mManager = (UsbManager) getSystemService(Context.USB_SERVICE);


        initReceiver();

        /**
         * Returns the raw USB descriptors for the device.
         */


        openUsb();


    }

    /**
     * 打开usb
     */
    private void openUsb() {
        HashMap<String, UsbDevice> deviceList = mManager.getDeviceList();
        if (deviceList.size() == 0) {//没有usb设备
            mDevice = null;
            return;
        }
        Iterator<UsbDevice> iterator = deviceList.values().iterator();
        while (iterator.hasNext()) {
            UsbDevice usbDevice = iterator.next();
            if (usbDevice.getProductId() == 30017 && usbDevice.getVendorId() == 1157 || usbDevice.getProductId() == 649 && usbDevice.getVendorId() == 10473) {
                mManager.requestPermission(usbDevice, PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0));
            }
        }
    }

    private void initReceiver() {
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        registerReceiver(mReceiver, filter);
    }


    @OnClick({R.id.btn_print, R.id.btn_info})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_print:
                //打印信息

                new Thread() {
                    @Override
                    public void run() {
                        byte[] PRINT_CONCENTRATION_LEVEL = {0x1b, '#', 0x4B};
                        byte[] PRINT_SELF_CHECKING_INFORMATION = {0x1b, '#', '#', 0x53, 0x45, 0x4C, 0x46};//打印机浓度
                        //返回出厂日期
                        byte[] a = {0x1d, 0x67, 0x62};
                        byte[] bytes = {0x1b, 0x12, 0x4a};//打印机器名称
                        byte[] b = {};//打印机温度
                        boolean command = sendCommand(PRINT_CONCENTRATION_LEVEL);
                        Log.e(TAG, "run: " + command);
                    }
                }.start();

                break;
            case R.id.btn_info:
                //列出信息
                openUsb();
                getPrinterInfo();
                break;
        }
    }

    /**
     * 列出打印机信息
     */
    private void getPrinterInfo() {
        tvUsbInfo.setText("打印机设备信息获取失败!");
        if (mDevice != null) {
            StringBuffer sb = new StringBuffer();
            sb.append("打印机信息如下:\r\n");
            sb.append("ProductId=" + mDevice.getProductId() + "\r\n");
            sb.append("VendorId=" + mDevice.getVendorId() + "\r\n");
            sb.append("InterfaceCount=" + mDevice.getInterfaceCount() + "\r\n");
            if (mDevice.getInterfaceCount() != 0) {
                for (int i = 0; i < mDevice.getInterfaceCount(); i++) {
                }
            }
            tvUsbInfo.setText(sb);
        }
    }

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        Log.e(TAG, "授权成功!");
                        if (device != null) {
                            //执行操作
                            setDevice(device);
                        } else {
                            mDevice = null;
                        }
                    } else {
                        Log.e(TAG, "permission denied for device " + device);
                    }
                }
            }
        }
    };

    /**
     * 设置打印机信息
     *
     * @param device
     */
    private void setDevice(UsbDevice device) {
        if (null != device) {
            mDevice = device;
            int interfaceCount = device.getInterfaceCount();
            Log.e(TAG, "接口数量: " + interfaceCount);
            if (interfaceCount != 0) {
                for (int i = 0; i < interfaceCount; i++) {
                    UsbInterface usbInterface = device.getInterface(i);
                    int interfaceClass = usbInterface.getInterfaceClass();
                    if (interfaceClass == UsbConstants.USB_CLASS_PRINTER) {//打印机类型
                        int endpointCount = usbInterface.getEndpointCount();
                        for (int j = 0; j < endpointCount; j++) {
                            UsbEndpoint endpoint = usbInterface.getEndpoint(j);
                            if (endpoint.getDirection() == UsbConstants.USB_DIR_OUT && endpoint.getType() == UsbConstants.USB_ENDPOINT_XFER_BULK) {
                                Log.e(TAG, "接口是: " + i + "      类是: " + usbInterface.getInterfaceClass() +
                                        "       端点是: " + j
                                        + "     方向是: " + endpoint.getDirection() +
                                        "       类型是: " + endpoint.getType());

                                //建立连接
                                mEndpoint = endpoint;
                                UsbDeviceConnection connection = mManager.openDevice(mDevice);
                                boolean b = connection.claimInterface(usbInterface, true);//申明接口
                                if (null != connection && b) {
                                    mConnection = connection;
                                    Log.e(TAG, "设备连接成功");
                                } else {
                                    mConnection = null;
                                    Log.e(TAG, "设备连接失败");
                                }
                                break;
                            }
                        }
                    }
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
                len = mConnection.bulkTransfer(mEndpoint, mContent, mContent.length, 10000);
            }
            if (len < 0) {
                Log.e(TAG, "打印数据发送失败!" + len);
                result = false;
            } else {
                Log.e(TAG, "打印数据发送成功!" + len + "个字节");
                result = true;
            }
        }
        return result;
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(mReceiver);
        closeUsb();
        mDevice = null;
    }
}
