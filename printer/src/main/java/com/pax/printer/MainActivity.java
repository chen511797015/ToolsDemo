package com.pax.printer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * 打印订单
     *
     * @param view
     */
    public void printOrder1(View view) {
        PrintTerUtils.getPrintTerUtils(this).toPrintOrder();
    }

    /**
     * 打印回收单
     *
     * @param view
     */
    public void printOrder2(View view) {
        PrintTerUtils.getPrintTerUtils(this).toPrint2();
    }

    /**
     * 打印回收单
     *
     * @param view
     */
    public void printOrder3(View view) {
        PrintTerUtils.getPrintTerUtils(this).toPrint3();
    }
}
