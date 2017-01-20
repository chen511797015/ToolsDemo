package cn.pax.toolsdemo.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pax.toolsdemo.R;
import cn.pax.toolsdemo.adapter.MainPagerAdapter;
import cn.pax.toolsdemo.base.BaseAppCompatActivity;
import cn.pax.toolsdemo.bean.EventBusBean;
import cn.pax.toolsdemo.fragment.CustomFragment;
import cn.pax.toolsdemo.fragment.FrameFragment;
import cn.pax.toolsdemo.fragment.SystemFragment;
import cn.pax.toolsdemo.fragment.UsbFragment;
import cn.pax.toolsdemo.fragment.WidgetFragment;
import cn.pax.toolsdemo.util.ScanGunKeyEventHelper;

public class MainActivity extends BaseAppCompatActivity {


    private static final String TAG = "MainActivity";

    @BindView(R.id.tb_main)
    TabLayout mTabLayout;
    @BindView(R.id.vp_main)
    ViewPager mViewPager;

    List<Fragment> mFragmentList;
    MainPagerAdapter mAdapter;

    ScanGunKeyEventHelper mScanGunKey;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        initView();

        initScanner();


    }

    private void initScanner() {
        mScanGunKey = new ScanGunKeyEventHelper();
        mScanGunKey.setOnBarCodeCatchListener(new ScanGunKeyEventHelper.OnScanSuccessListener() {
            @Override
            public void onScanSuccess(String barcode) {
                Log.e(TAG, "获取读取的内容: " + barcode);
            }
        });
    }

    @Override
    protected int setView() {
        getSupportActionBar().hide();//隐藏掉整个ActionBar
        return R.layout.activity_main;
    }

    @Override
    protected void findView() {

    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);

        mFragmentList = new ArrayList<>();
        SystemFragment mSystemFragment = new SystemFragment();
        WidgetFragment mWidgetFragment = new WidgetFragment();
        UsbFragment mUsbFragment = new UsbFragment();
        FrameFragment mFrameFragment = new FrameFragment();
        CustomFragment mCustomFragment = new CustomFragment();

        mFragmentList.add(mSystemFragment);
        mFragmentList.add(mWidgetFragment);
        mFragmentList.add(mUsbFragment);
        mFragmentList.add(mFrameFragment);
        mFragmentList.add(mCustomFragment);

        mAdapter = new MainPagerAdapter(getSupportFragmentManager(), mFragmentList);
        mViewPager.setAdapter(mAdapter);
        //将TabLayout与ViewPager绑定在一起
        mTabLayout.setupWithViewPager(mViewPager);

    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void init() {


    }

    /**
     * 获取扫描二维码的返回的扫描值
     *
     * @param requestCode 请求码
     * @param resultCode  返回码
     * @param data        intent对象
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            String msg = "";
            if (intentResult.getContents() == null) {
                Toast.makeText(this, "内容为空", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "扫描成功", Toast.LENGTH_LONG).show();
                // ScanResult 为 获取到的字符串
                String ScanResult = intentResult.getContents();
                msg = ScanResult;
            }
            EventBus.getDefault().post(new EventBusBean(msg));
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        mScanGunKey.analysisKeyEvent(event);
        return super.dispatchKeyEvent(event);
    }
}
