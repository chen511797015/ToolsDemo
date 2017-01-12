package cn.pax.toolsdemo.fragment;

import android.graphics.Camera;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.List;

import cn.pax.toolsdemo.R;
import cn.pax.toolsdemo.adapter.SystemPagerAdapter;
import cn.pax.toolsdemo.base.BaseFragment;

import static android.R.attr.tag;

/**
 * Created by chendd on 2017/1/11.
 * 用于管理系统设备
 */

public class SystemFragment extends BaseFragment {
    private static final String TAG = "SystemFragment";

    ViewPager mViewPager;
    TabLayout mTabLayout;

    List<Fragment> mList;


    @Override
    protected int setView() {
        return R.layout.fragment_system;
    }

    @Override
    protected void findView() {

        mViewPager = (ViewPager) mView.findViewById(R.id.vp_system);
        mTabLayout = (TabLayout) mView.findViewById(R.id.tb_system);

    }

    @Override
    protected void initView() {

        mList = new ArrayList<>();
        CameraFragment mCameraFragment = new CameraFragment();
        RouterFragment mRouterFragment = new RouterFragment();
        mList.add(mCameraFragment);
        mList.add(mRouterFragment);


        SystemPagerAdapter mSystemPagerAdapter = new SystemPagerAdapter(getFragmentManager(), mList);
        mViewPager.setAdapter(mSystemPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void init() {

    }
}
