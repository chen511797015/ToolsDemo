package cn.pax.toolsdemo.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by chendd on 2017/1/12.
 */

public class BasePagerAdapter extends FragmentPagerAdapter {
    List<Fragment> mList;
    String[] mData;
    FragmentManager fm;

    public BasePagerAdapter(FragmentManager fm, List<Fragment> mList, String[] mData) {
        super(fm);
        this.mList = mList;
        this.mData = mData;
        this.fm = fm;
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
        //super.destroyItem(container, position, object);
        fm.beginTransaction().hide(mList.get(position)).commit();

    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mData[position];
    }
}
