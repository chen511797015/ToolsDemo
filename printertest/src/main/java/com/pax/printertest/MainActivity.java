package com.pax.printertest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.pax.api.PrinterManager;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    String num = "";
    private PrinterManager mPrinterManager;


    @BindView(R.id.iv)
    ImageView iv;
    @BindView(R.id.tv_card_no)
    TextView tvCardNo;
    @BindView(R.id.tv_amount)
    TextView tvAmount;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_user)
    TextView tvUser;
    @BindView(R.id.iv_signature)
    ImageView ivSignature;
    @BindView(R.id.sl)
    ScrollView sl;
    @BindView(R.id.btn_print)
    Button btnPrint;
    @BindView(R.id.btn_cancel)
    Button btnCancel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mPrinterManager = PrinterManager.getInstance(this);

        initData();


    }

    private void initData() {
        num = "123456789";
        tvAmount.setText("RMB100");
        SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
        String date = mDateFormat.format(new java.util.Date()); // 获得系统时间
        tvTime.setText(getResources().getString(R.string.date_time) + date);

        tvCardNo.setText("654646413216543");
        try {
            InputStream inputStream = getAssets().open("receipt_ums.png");
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            iv.setImageBitmap(bitmap);
            ivSignature.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.btn_print, R.id.btn_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_print:
//                WindowManager windowManager = getWindowManager();
//                Display display = windowManager.getDefaultDisplay();
//                int screenWidth = display.getWidth();
//                int screenHeight = display.getHeight();
//                Log.i("iii", "屏幕宽度---" + screenWidth);
//                Log.i("iii", "屏幕高度---" + screenHeight);
                new Thread() {
                    @Override
                    public void run() {
                        final Bitmap bmp = PrintUtil.getViewBitmap(MainActivity.this, sl);
                        Log.i("iii", "with---" + bmp.getWidth() + "heigth---" + bmp.getHeight());
                        Bitmap bitmap = scaleWithWH(bmp, 576);
                        printBitmap(bitmap);
                    }
                }.start();

                break;
            case R.id.btn_cancel:
                print();
                break;
        }
    }


    /**
     * 开始打印
     */
    private void print() {

        new Thread() {
            @Override
            public void run() {
                Log.e(TAG, "run: " + Thread.currentThread().getName());
//                final Bitmap bmp = PrintUtil.getViewBitmap(MainActivity.this, sl);
//                HPRTPrinterHelper.PrintBitmap(bmp, (byte) 0, (byte) 0);
                try {
                    mPrinterManager.prnStatus();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    private void printBitmap(Bitmap bitmap) {
        try {
            Log.e(TAG, "开始打印...");
            mPrinterManager = PrinterManager.getInstance(this);
            //mPrinterManager.prnInit();
            Thread.sleep(1000);
            mPrinterManager.prnBitmap(bitmap);
            mPrinterManager.prnStartCut(1);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private Bitmap scaleWithWH(Bitmap src, double w) {
        double h = w / src.getWidth() * src.getHeight();
        if (w == 0 || h == 0 || src == null) {
            return src;
        } else {
            // 记录src的宽高
            int width = src.getWidth();
            int height = src.getHeight();
            // 创建一个matrix容器
            Matrix matrix = new Matrix();
            // 计算缩放比例
            float scaleWidth = (float) (w / width);
            float scaleHeight = (float) (h / height);
            // 开始缩放
            matrix.postScale(scaleWidth, scaleHeight);
            // 创建缩放后的图片
            return Bitmap.createBitmap(src, 0, 0, width, height, matrix, true);
        }
    }

}
