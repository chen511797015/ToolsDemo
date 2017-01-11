package cn.pax.toolsdemo.activity;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pax.toolsdemo.R;
import cn.pax.toolsdemo.base.BaseAppCompatActivity;

/**
 * Created by chendd on 2017/1/11.
 */

public class CustomScanActivity extends BaseAppCompatActivity implements DecoratedBarcodeView.TorchListener {

    private static final String TAG = "CustomScanActivity";
    @BindView(R.id.btn_switch)
    Button swichLight;
    @BindView(R.id.btn_hint1)
    Button hint1Show;
    @BindView(R.id.btn_hint2)
    Button hint2Show;
    @BindView(R.id.dbv_custom)
    DecoratedBarcodeView mDBV;


    CaptureManager captureManager;
    boolean isLightOn = false;

    @Override
    protected int setView() {
        return R.layout.activity_custom_scan;
    }

    @Override
    protected void findView() {
        ButterKnife.bind(this);

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void init() {

        mDBV.setTorchListener(this);
        // 如果没有闪光灯功能，就去掉相关按钮
        if (!hasFlash()) {
            swichLight.setVisibility(View.GONE);
        }

        //重要代码，初始化捕获
        captureManager = new CaptureManager(this, mDBV);
        Bundle savedInstanceState = new Bundle();
        captureManager.initializeFromIntent(getIntent(), savedInstanceState);
        captureManager.decode();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return mDBV.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }


    // torch 手电筒
    public void onTorchOn() {
        Toast.makeText(this, "torch on", Toast.LENGTH_LONG).show();
        isLightOn = true;
    }

    //关闭手电筒
    public void onTorchOff() {
        Toast.makeText(this, "torch off", Toast.LENGTH_LONG).show();
        isLightOn = false;
    }

    // 判断是否有闪光灯功能
    private boolean hasFlash() {
        return getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    // 点击切换闪光灯
    @OnClick(R.id.btn_switch)
    public void swichLight() {
        if (isLightOn) {
            mDBV.setTorchOff();
        } else {
            mDBV.setTorchOn();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        captureManager.onSaveInstanceState(outState);
    }


    @Override
    protected void onPause() {
        super.onPause();
        captureManager.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        captureManager.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        captureManager.onDestroy();
    }

}
