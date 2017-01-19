package cn.pax.toolsdemo;

import android.app.Application;

import cn.pax.toolsdemo.util.UsbAdmin;

/**
 * Created by chendd on 2017/1/12.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //new UsbAdmin(this).openUsb();
    }
}
