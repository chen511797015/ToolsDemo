package cn.pax.toolsdemo.fragment;


import android.content.Intent;
import android.view.View;
import android.widget.Button;

import cn.pax.toolsdemo.R;
import cn.pax.toolsdemo.activity.UsbTestActivity;
import cn.pax.toolsdemo.base.BaseFragment;

/**
 * Created by chendd on 2017/1/12.
 */

public class PrintFragment extends BaseFragment {

    private static final String TAG = "PrintFragment";

    Button btn_print_init;//初始化打印机

    @Override
    protected int setView() {
        return R.layout.fragment_print;

    }

    @Override
    protected void findView() {
        btn_print_init = (Button) mView.findViewById(R.id.btn_print_init);

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initEvent() {

        btn_print_init.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), UsbTestActivity.class));
            }
        });

    }

    @Override
    protected void init() {

    }
}
