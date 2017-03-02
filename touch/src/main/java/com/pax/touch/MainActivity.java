package com.pax.touch;

import android.app.Presentation;
import android.content.Context;
import android.hardware.display.DisplayManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {
    Button btn_clear;
    RelativeLayout activity_main;
    private PaintView paintView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity_main = (RelativeLayout) findViewById(R.id.activity_main);
        btn_clear = (Button) findViewById(R.id.btn_clear);

        DisplayMetrics dm = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);

        paintView = new PaintView(this, dm.widthPixels, dm.heightPixels);
        activity_main.addView(paintView);

        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != paintView) {
                    paintView.clear();
                }
            }
        });


        try {
            DisplayManager displayManager = (DisplayManager) getSystemService(DISPLAY_SERVICE);
            Display[] displays = displayManager.getDisplays();
            M m = new M(this, displays[1]);
            m.show();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    static class M extends Presentation {

        public M(Context outerContext, Display display) {
            super(outerContext, display);
        }

        public M(Context outerContext, Display display, int theme) {
            super(outerContext, display, theme);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            Button btn = (Button) findViewById(R.id.btn_clear);
            RelativeLayout rl = (RelativeLayout) findViewById(R.id.activity_main);
            DisplayMetrics dm = new DisplayMetrics();
            getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
            final PaintView pv = new PaintView(getContext(), dm.widthPixels, dm.heightPixels);
            rl.addView(pv);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != pv) {
                        pv.clear();
                    }
                }
            });

        }
    }

}
