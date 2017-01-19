package cn.pax.toolsdemo.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pax.toolsdemo.R;
import cn.pax.toolsdemo.adapter.BasePagerAdapter;
import cn.pax.toolsdemo.base.BaseFragment;

/**
 * Created by chendd on 2017/1/11.
 * 用于管理usb设备信息
 */

public class UsbFragment extends BaseFragment {
    private static final String TAG = "UsbFragment";
    @BindView(R.id.tb_usb)
    TabLayout tb_usb;
    @BindView(R.id.vp_usb)
    ViewPager vp_usb;


    @Override
    protected int setView() {
        return R.layout.fragment_usb;
    }

    @Override
    protected void findView() {
        ButterKnife.bind(getActivity());


    }

    @Override
    protected void initView() {
        List<Fragment> mList = new ArrayList<>();
        String[] mData = {"打印机", "Usb列表"};

        mList.add(new PrintFragment());
        mList.add(new UsbListFragment());

        BasePagerAdapter mBasePagerAdapter = new BasePagerAdapter(getFragmentManager(), mList, mData);
        vp_usb.setAdapter(mBasePagerAdapter);
        //绑定ViewPager
        tb_usb.setupWithViewPager(vp_usb);

    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void init() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
}
