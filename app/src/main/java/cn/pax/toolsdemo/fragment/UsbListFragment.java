package cn.pax.toolsdemo.fragment;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Iterator;

import cn.pax.toolsdemo.R;
import cn.pax.toolsdemo.base.BaseFragment;

/**
 * Created by chendd on 2017/1/17.
 * 显示usb设备信息
 */

public class UsbListFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = "UsbListFragment";

    TextView textView;
    Button btn_show;

    Button btn_show_usb;//显示所有usb设备
    TextView tv_all_sb;//显示所有usb设备信息

    private static final String ACTION_USB_PERMISSION =
            "com.android.example.USB_PERMISSION";
    private StringBuilder sb;
    private UsbManager manager;


    @Override
    protected int setView() {
        return R.layout.fragment_usb_list;
    }

    @Override
    protected void findView() {
        textView = (TextView) mView.findViewById(R.id.sb);
        btn_show = (Button) mView.findViewById(R.id.btn_show);

        btn_show_usb = (Button) mView.findViewById(R.id.btn_show_usb);
        tv_all_sb = (TextView) mView.findViewById(R.id.tv_all_sb);

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initEvent() {

        btn_show.setOnClickListener(this);
        btn_show_usb.setOnClickListener(this);

    }

    @Override
    protected void init() {

        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        getActivity().registerReceiver(mReceiver, filter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_show:
                show(v);
                break;
            case R.id.btn_show_usb:
                findAllUsbDevice();
                break;
        }

    }

    private void findAllUsbDevice() {
        manager = (UsbManager) getActivity().getSystemService(Context.USB_SERVICE);
        HashMap<String, UsbDevice> deviceList = manager.getDeviceList();
        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
        StringBuffer stringBuffer = new StringBuffer();
        while (deviceIterator.hasNext()) {
            UsbDevice usbDevice = deviceIterator.next();
            stringBuffer.append("DeviceName=" + usbDevice.getDeviceName() + "\n");
            stringBuffer.append("DeviceId=" + usbDevice.getDeviceId() + "\n");
            stringBuffer.append("VendorId=" + usbDevice.getVendorId() + "\n");
            stringBuffer.append("ProductId=" + usbDevice.getProductId() + "\n");
            stringBuffer.append("DeviceClass=" + usbDevice.getDeviceClass() + "\n");
            stringBuffer.append("InterfaceCount=" + usbDevice.getInterfaceCount() + "\n");
            int deviceClass = usbDevice.getDeviceClass();
            if (deviceClass == 0 && usbDevice.getInterfaceCount() != 0) {
                UsbInterface anInterface = usbDevice.getInterface(0);
                stringBuffer.append("device Class 为0-------------\n");
                stringBuffer.append("Interface.describeContents()=" + anInterface.describeContents() + "\n");
                stringBuffer.append("Interface.getEndpointCount()=" + anInterface.getEndpointCount() + "\n");
                stringBuffer.append("Interface.getId()=" + anInterface.getId() + "\n");
                //http://blog.csdn.net/u013686019/article/details/50409421
                //http://www.usb.org/developers/defined_class/#BaseClassFFh
                //通过下面的InterfaceClass 来判断到底是哪一种的，例如7就是打印机，8就是usb的U盘
                int interfaceClass = anInterface.getInterfaceClass();
                stringBuffer.append("Interface.getInterfaceClass()=" + interfaceClass + "\n");
                if (interfaceClass == 1) {
                    stringBuffer.append("此设备是音频类\n");
                } else if (interfaceClass == 2) {
                    stringBuffer.append("此设备是通信类\n");
                } else if (interfaceClass == 3) {
                    stringBuffer.append("此设备是人机接口类\n");
                } else if (interfaceClass == 5) {
                    stringBuffer.append("此设备是物理类\n");
                } else if (interfaceClass == 6) {
                    stringBuffer.append("此设备是图像类\n");
                } else if (interfaceClass == 7) {
                    stringBuffer.append("此设备是打印机类\n");
                } else if (interfaceClass == 8) {
                    stringBuffer.append("此设备是大数据存储类\n");
                } else if (interfaceClass == 9) {
                    stringBuffer.append("此设备是集线器类\n");
                } else if (interfaceClass == 10) {
                    stringBuffer.append("此设备是CDC数据类\n");
                } else if (interfaceClass == 11) {
                    stringBuffer.append("此设备是智能卡类\n");
                } else if (interfaceClass == 12) {
                    stringBuffer.append("此设备是安全类\n");
                } else if (interfaceClass == 13) {
                    stringBuffer.append("此设备是诊断设备类\n");
                }

                stringBuffer.append("anInterface.getInterfaceProtocol()=" + anInterface.getInterfaceProtocol() + "\n");
                stringBuffer.append("anInterface.getInterfaceSubclass()=" + anInterface.getInterfaceSubclass() + "\n");
                stringBuffer.append("device Class 为0------end-------\n");
            }

            stringBuffer.append("DeviceProtocol=" + usbDevice.getDeviceProtocol() + "\n");
            stringBuffer.append("DeviceSubclass=" + usbDevice.getDeviceSubclass() + "\n");
            stringBuffer.append("+++++++++++++++++++++++++++\n");
            stringBuffer.append("                           \n");
        }
        tv_all_sb.setText(stringBuffer.toString());
        Log.e(TAG, "USB设备信息:\n " + stringBuffer.toString());
    }


    public void show(View view) {
        sb = new StringBuilder();
        manager = (UsbManager) getActivity().getSystemService(Context.USB_SERVICE);
        HashMap<String, UsbDevice> deviceList = manager.getDeviceList();
        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();

        while (deviceIterator.hasNext()) {
            UsbDevice usbDevice = deviceIterator.next();
            if (usbDevice.getVendorId() == 1157 && usbDevice.getProductId() == 30017 ||
                    usbDevice.getVendorId() == 10473 && usbDevice.getProductId() == 649) {
                manager.requestPermission(usbDevice, PendingIntent.getBroadcast(getActivity(), 0, new Intent(ACTION_USB_PERMISSION), 0));
            }
        }

    }


    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.e(TAG, "onReceive: " + action);
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice usbDevice = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    sb = new StringBuilder();
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (usbDevice != null) {
                            //call method to set up device communication
                            //建立usb通信
                            sb.append("DeviceName=" + usbDevice.getDeviceName() + "\n");
                            sb.append("DeviceId=" + usbDevice.getDeviceId() + "\n");
                            sb.append("VendorId=" + usbDevice.getVendorId() + "\n");
                            sb.append("ProductId=" + usbDevice.getProductId() + "\n");
                            sb.append("DeviceClass=" + usbDevice.getDeviceClass() + "\n");
                            sb.append("InterfaceCount=" + usbDevice.getInterfaceCount() + "\n");
                            int deviceClass = usbDevice.getDeviceClass();
                            if (deviceClass == 0 && usbDevice.getInterfaceCount() != 0) {
                                UsbInterface anInterface = usbDevice.getInterface(0);
                                sb.append("device Class 为0-------------\n");
                                sb.append("Interface.describeContents()=" + anInterface.describeContents() + "\n");
                                sb.append("Interface.getEndpointCount()=" + anInterface.getEndpointCount() + "\n");
                                sb.append("Interface.getId()=" + anInterface.getId() + "\n");
                                //http://blog.csdn.net/u013686019/article/details/50409421
                                //http://www.usb.org/developers/defined_class/#BaseClassFFh
                                //通过下面的InterfaceClass 来判断到底是哪一种的，例如7就是打印机，8就是usb的U盘
                                sb.append("Interface.getInterfaceClass()=" + anInterface.getInterfaceClass() + "\n");
                                if (anInterface.getInterfaceClass() == UsbConstants.USB_CLASS_PRINTER) {
                                    sb.append("此设备是打印机\n");
                                } else if (anInterface.getInterfaceClass() == 8) {
                                    sb.append("此设备是U盘\n");
                                } else if (anInterface.getInterfaceClass() == 3) {
                                    sb.append("此设备是扫码枪或者键盘鼠标\n");
                                }
                                sb.append("anInterface.getInterfaceProtocol()=" + anInterface.getInterfaceProtocol() + "\n");
                                sb.append("anInterface.getInterfaceSubclass()=" + anInterface.getInterfaceSubclass() + "\n");
                                sb.append("device Class 为0------end-------\n");
                            }

                            sb.append("DeviceProtocol=" + usbDevice.getDeviceProtocol() + "\n");
                            sb.append("DeviceSubclass=" + usbDevice.getDeviceSubclass() + "\n");
                            sb.append("+++++++++++++++++++++++++++\n");
                            sb.append("                           \n");
                        }
                        textView.setText(sb);
                        Log.e(TAG, "设备信息``: " + sb.toString());
                    } else {
                        Log.d(TAG, "permission denied for device ");
                    }
                }
            }
        }
    };
}