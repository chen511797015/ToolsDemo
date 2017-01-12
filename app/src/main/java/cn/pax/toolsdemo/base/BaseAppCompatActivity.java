package cn.pax.toolsdemo.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.List;

import static android.R.attr.handle;

/**
 * Created by chendd on 2017/1/11.
 */

public abstract class BaseAppCompatActivity extends AppCompatActivity {


    public String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setView());

        findView();

        initView();

        initEvent();

        init();
    }


    protected abstract int setView();

    protected abstract void findView();

    protected abstract void initView();

    protected abstract void initEvent();

    protected abstract void init();


    /**
     * 重写onActivityResult,使二个或者多个Fragment嵌套使用时能收到onActivityResult回调
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        FragmentManager fm = getSupportFragmentManager();
        int index = requestCode >> 16;
        if (index != 0) {
            index--;
            if (fm.getFragments() == null || index < 0 || index >= fm.getFragments().size()) {
                Log.e(TAG, "onActivityResult: ");
                return;
            }
            Fragment frag = fm.getFragments().get(index);
            if (frag != null) {
                handleResult(frag, requestCode, resultCode, data);

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 递归调用,对所有子Fragment生效
     *
     * @param frag
     * @param requestCode
     * @param resultCode
     * @param data
     */
    private void handleResult(Fragment frag, int requestCode, int resultCode, Intent data) {
        frag.onActivityResult(requestCode & 0xffff, resultCode, data);
        List<Fragment> frags = frag.getChildFragmentManager().getFragments();
        if (frags != null) {
            for (Fragment f : frags) {
                if (f != null)
                    handleResult(f, requestCode, resultCode, data);
            }
        }
    }
}
