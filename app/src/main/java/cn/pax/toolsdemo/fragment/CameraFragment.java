package cn.pax.toolsdemo.fragment;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import cn.pax.toolsdemo.R;
import cn.pax.toolsdemo.activity.CustomScanActivity;
import cn.pax.toolsdemo.base.BaseFragment;
import cn.pax.toolsdemo.bean.EventBusBean;

/**
 * Created by chendd on 2017/1/11.
 * 相机界面: 主要用于扫描二维码
 */

public class CameraFragment extends BaseFragment {

    private static final String TAG = "CameraFragment";

    Button btn_camera_start;
    TextView tv_camera_content;


    @Override
    protected int setView() {
        return R.layout.fragment_camera;
    }

    @Override
    protected void findView() {
        btn_camera_start = (Button) mView.findViewById(R.id.btn_camera_start);
        tv_camera_content = (TextView) mView.findViewById(R.id.tv_camera_content);
    }

    @Override
    protected void initView() {

        EventBus.getDefault().register(this);

    }

    @Override
    protected void initEvent() {

        btn_camera_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customScan();
            }
        });

    }


    @Override
    protected void init() {

    }

    /**
     * 开始扫描
     */
    private void customScan() {
        new IntentIntegrator(getActivity())
                .setOrientationLocked(false)
                .setCaptureActivity(CustomScanActivity.class)//设置扫描的activity
                .initiateScan();//初始化扫描
    }


    /**
     * 获取扫码返回的内容
     *
     * @param bean
     */
    @Subscribe
    public void onEventMainThread(EventBusBean bean) {
        if (bean != null) {
            Log.e(TAG, "onEventMainThread: " + bean.getCameraResult());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
