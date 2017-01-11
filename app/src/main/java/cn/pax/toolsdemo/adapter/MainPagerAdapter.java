package cn.pax.toolsdemo.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by chendd on 2017/1/11.
 */

public class MainPagerAdapter extends FragmentPagerAdapter {
    private static final String TAG = "MainPagerAdapter";

    List<Fragment> mList;

    String[] mData = {"控件", "Usb设备", "框架", "自定义"};


    public MainPagerAdapter(FragmentManager fm, List<Fragment> mList) {
        super(fm);
        this.mList = mList;
    }


    @Override
    public Fragment getItem(int position) {
        return mList.get(position);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    /**
     * 与绑定的TabLayout搭配使用
     *
     * @param position
     * @return
     */
    @Override
    public CharSequence getPageTitle(int position) {
        return mData[position];
    }
}
