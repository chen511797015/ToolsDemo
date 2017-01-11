package cn.pax.toolsdemo.activity;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pax.toolsdemo.R;
import cn.pax.toolsdemo.adapter.MainPagerAdapter;
import cn.pax.toolsdemo.fragment.CustomFragment;
import cn.pax.toolsdemo.fragment.FrameFragment;
import cn.pax.toolsdemo.fragment.UsbFragment;
import cn.pax.toolsdemo.fragment.WidgetFragment;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.tb_main)
    TabLayout mTabLayout;
    @BindView(R.id.vp_main)
    ViewPager mViewPager;


    List<Fragment> mFragmentList;
    MainPagerAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();//隐藏掉整个ActionBar
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);


        initView();


    }

    private void initView() {

        mFragmentList = new ArrayList<>();
        WidgetFragment mWidgetFragment = new WidgetFragment();
        UsbFragment mUsbFragment = new UsbFragment();
        FrameFragment mFrameFragment = new FrameFragment();
        CustomFragment mCustomFragment = new CustomFragment();
        mFragmentList.add(mWidgetFragment);
        mFragmentList.add(mUsbFragment);
        mFragmentList.add(mFrameFragment);
        mFragmentList.add(mCustomFragment);

        mAdapter = new MainPagerAdapter(getSupportFragmentManager(), mFragmentList);
        mViewPager.setAdapter(mAdapter);
        //将TabLayout与ViewPager绑定在一起
        mTabLayout.setupWithViewPager(mViewPager);
    }
}
