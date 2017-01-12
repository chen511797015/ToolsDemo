package cn.pax.toolsdemo.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by chendd on 2017/1/11.
 */

public abstract class BaseFragment extends Fragment implements View.OnTouchListener {

    public String TAG = this.getClass().getSimpleName();

    private int mLayoutId;
    protected View mView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.mLayoutId = setView();

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mView = inflater.inflate(mLayoutId, container, false);
        mView.setOnTouchListener(this);

        return mView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }
}
