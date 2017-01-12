package cn.pax.toolsdemo.base;


import android.app.Activity;
import android.os.Bundle;

/**
 * Created by chendd on 2017/1/10.
 */

public abstract class BaseActivity extends Activity {

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
}
