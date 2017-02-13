package com.pax.screenadapter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DisplayMetrics dm = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
        Log.e(TAG, "widthPixels: " + dm.widthPixels);
        Log.e(TAG, "heightPixels: " + dm.heightPixels);
        Log.e(TAG, "densityDpi: " + dm.densityDpi);

    }
}
