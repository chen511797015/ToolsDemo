package cn.pax.toolsdemo.fragment;


import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pax.toolsdemo.R;
import cn.pax.toolsdemo.base.BaseFragment;
import cn.pax.toolsdemo.tool.EcrPosprintException;
import cn.pax.toolsdemo.tool.PrintImgThread;
import cn.pax.toolsdemo.tool.PrintOrderThread;
import cn.pax.toolsdemo.tool.PrintThread;
import cn.pax.toolsdemo.tool.PrinterConstants;
import cn.pax.toolsdemo.util.PrinterUtil;
import cn.pax.toolsdemo.util.ToastUtil;

/**
 * Created by chendd on 2017/1/12.
 */

public class PrintFragment extends BaseFragment {

    private static final String TAG = "PrintFragment";
    @BindView(R.id.btn_print_show)
    Button btnPrintShow;
    @BindView(R.id.tv_print_show_printer)
    TextView tvPrintShowPrinter;
    @BindView(R.id.btn_print_init)
    Button btnPrintInit;
    @BindView(R.id.btn_print_info)
    Button btnPrintInfo;
    @BindView(R.id.btn_print_concentration)
    Button btnPrintConcentration;
    @BindView(R.id.sp_print_concentration)
    Spinner spPrintConcentration;
    @BindView(R.id.btn_print_order)
    Button btnPrintOrder;
    @BindView(R.id.brn_print_bar_code)
    Button brnPrintBarCode;
    @BindView(R.id.btn_print_qr_code)
    Button btnPrintQrCode;


    String mPrintConcentration = "20";


    @Override
    protected int setView() {
        return R.layout.fragment_print;

    }

    @Override
    protected void findView() {
        ButterKnife.bind(this, mView);

    }

    @Override
    protected void initView() {


        //初始化下拉列表
        initSp();


    }

    private void initSp() {
        final String[] mItems = new String[40];
        for (int i = 0; i < 40; i++) {
            mItems[i] = i + "";
        }
        // 建立Adapter并且绑定数据源
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, mItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //绑定 Adapter到控件
        spPrintConcentration.setAdapter(adapter);
        spPrintConcentration.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ToastUtil.showToast("当前浓度: " + mItems[position]);
                mPrintConcentration = mItems[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void initEvent() {


    }

    @Override
    protected void init() {

    }


    @OnClick({R.id.btn_print_show, R.id.btn_print_init, R.id.btn_print_info, R.id.btn_print_concentration, R.id.btn_print_order, R.id.brn_print_bar_code, R.id.btn_print_qr_code})
    public void onClick(View view) {
        if (!PrinterUtil.getInstance(getActivity()).getUsbStatus()) {
            PrinterUtil.getInstance(getActivity()).openUsb();
        }
        switch (view.getId()) {
            case R.id.btn_print_show:
                //查找打印机信息
                findPrinterInfo();
                break;
            case R.id.btn_print_init://初始化打印机
                startPrint(PrinterConstants.InitPrint);
                try {
                    startPrint("初始化成功".getBytes("GBK"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_print_info://打印机信息
                startPrint(PrinterConstants.PRINT_SELF_CHECKING_INFORMATION);
                break;
            case R.id.btn_print_concentration://打印机浓度
                try {
                    startPrint(PrinterConstants.setPrinterConcentration(Integer.parseInt(mPrintConcentration)));
                    startPrint(PrinterConstants.PRINT_CONCENTRATION_LEVEL);
                } catch (EcrPosprintException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_print_order:// 打印订单
                new PrintOrderThread(getActivity()).run();
                break;
            case R.id.brn_print_bar_code://打印条码
                new PrintImgThread(getActivity(), R.mipmap.bar_code_2).run();

                break;
            case R.id.btn_print_qr_code://打印二维码
                new PrintImgThread(getActivity(), R.mipmap.pax_logo).run();
                break;
        }
    }

    /**
     * 开始打印
     *
     * @param mCommand
     */
    private void startPrint(byte[] mCommand) {
        new PrintThread(getActivity(), mCommand).run();
    }

    /**
     * 查找打印机信息
     */
    private void findPrinterInfo() {

        StringBuilder sb = new StringBuilder();
        UsbManager manager = (UsbManager) getActivity().getSystemService(Context.USB_SERVICE);
        HashMap<String, UsbDevice> deviceList = manager.getDeviceList();
        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();

        while (deviceIterator.hasNext()) {
            UsbDevice usbDevice = deviceIterator.next();
            if (usbDevice.getVendorId() == 1157 && usbDevice.getProductId() == 30017 ||
                    usbDevice.getVendorId() == 10473 && usbDevice.getProductId() == 649) {
                sb.append("打印机设备信息如下:\n");
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
                    sb.append("Interface.getInterfaceClass()=" + anInterface.getInterfaceClass() + "\n");
                    sb.append("anInterface.getInterfaceProtocol()=" + anInterface.getInterfaceProtocol() + "\n");
                    sb.append("anInterface.getInterfaceSubclass()=" + anInterface.getInterfaceSubclass() + "\n");
                    sb.append("device Class 为0------end-------\n");
                }
                sb.append("DeviceProtocol=" + usbDevice.getDeviceProtocol() + "\n");
                sb.append("DeviceSubclass=" + usbDevice.getDeviceSubclass() + "\n");
                sb.append("+++++++++++++++++++++++++++\n");
                sb.append("                           \n");
            }
        }
        tvPrintShowPrinter.setText(sb);
    }
}
