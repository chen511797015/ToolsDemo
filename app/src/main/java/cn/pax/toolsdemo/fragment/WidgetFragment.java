package cn.pax.toolsdemo.fragment;

import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import cn.pax.toolsdemo.R;
import cn.pax.toolsdemo.base.BaseFragment;
import cn.pax.toolsdemo.ui.PaintView;

/**
 * Created by chendd on 2017/1/11.
 * 控件
 */

public class WidgetFragment extends BaseFragment {
    RelativeLayout mRlContaint;
    Button btn_clear;
    private PaintView mPaintView;

    @Override
    protected int setView() {
        return R.layout.fragment_widget;
    }

    @Override
    protected void findView() {
        mRlContaint = (RelativeLayout) mView.findViewById(R.id.m_rl_containt);
        btn_clear = (Button) mView.findViewById(R.id.btn_clear);

    }

    @Override
    protected void initView() {
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);

        mPaintView = new PaintView(getContext(), dm.widthPixels, dm.heightPixels);
        mRlContaint.addView(mPaintView);
    }

    @Override
    protected void initEvent() {
        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPaintView.clear();
            }
        });
    }

    @Override
    protected void init() {

    }

}
