package cn.pax.toolsdemo.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeEncoder;

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
    ImageView iv_camera_content;


    @Override
    protected int setView() {
        return R.layout.fragment_camera;
    }

    @Override
    protected void findView() {
        btn_camera_start = (Button) mView.findViewById(R.id.btn_camera_start);
        tv_camera_content = (TextView) mView.findViewById(R.id.tv_camera_content);
        iv_camera_content = (ImageView) mView.findViewById(R.id.iv_camera_content);
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
            if (!TextUtils.isEmpty(bean.getCameraResult())) {
                //扫码内容不为空,创建二维码
                Bitmap bitmap = encodeAsBitmap(bean.getCameraResult(), 200, 200);
                iv_camera_content.setImageBitmap(bitmap);
                tv_camera_content.setText(bean.getCameraResult());

            } else {
                Toast.makeText(getActivity(), "扫码内容为空", Toast.LENGTH_LONG).show();
                //扫码内容为空
            }
        }
    }

    /**
     * 生成二维码
     *
     * @param mStr
     */
    private Bitmap encodeAsBitmap(String mStr, int mWidth, int mHieght) {
        Bitmap mBitmap = null;
        BitMatrix mResult = null;
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            mResult = multiFormatWriter.encode(mStr, BarcodeFormat.QR_CODE, mWidth, mHieght);
            // 使用 ZXing Android Embedded 要写的代码
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            mBitmap = barcodeEncoder.createBitmap(mResult);
            return mBitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException iae) { // ?
            return null;
        }

        // 如果不使用 ZXing Android Embedded 的话，要写的代码
//        int w = result.getWidth();
//        int h = result.getHeight();
//        int[] pixels = new int[w * h];
//        for (int y = 0; y < h; y++) {
//            int offset = y * w;
//            for (int x = 0; x < w; x++) {
//                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
//            }
//        }
//        bitmap = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);
//        bitmap.setPixels(pixels,0,100,0,0,w,h);

        return mBitmap;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
