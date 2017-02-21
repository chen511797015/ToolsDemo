package com.pax.api;

import android.hardware.usb.UsbDevice;

/**
 * Created by chendd on 2017/2/21.
 */

public interface IPort {

    String PortType = "";
    boolean IsPortOpen = false;
    String paramPortSetting = "";

    void IsBLEType(boolean var1);

    void SetReadTimeout(int var1);

    void SetWriteTimeout(int var1);

    void InitPort();

    boolean OpenPort(String var1);

    boolean OpenPort(String var1, String var2);

    boolean OpenPort(UsbDevice var1);

    boolean ClosePort();

    int WriteData(byte[] var1);

    int WriteData(byte[] var1, int var2);

    int WriteData(byte[] var1, int var2, int var3);

    int ReadData(byte[] var1);

    int ReadData(byte[] var1, int var2, int var3);

    boolean IsOpen();

    String GetPortType();

    String GetPrinterName();

    String GetPrinterModel();


}
