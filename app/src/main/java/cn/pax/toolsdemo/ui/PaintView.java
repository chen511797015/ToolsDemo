package cn.pax.toolsdemo.ui;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import cn.pax.toolsdemo.R;

public class PaintView extends View {

    private Paint mPaint;
    private Path mPath;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private int screenWidth, screenHeight;
    private String text;
    private float currentX, currentY;
    private Context context;

    public PaintView(Context context, int screenWidth, int screenHeight) {
        super(context);
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.context = context;
        init(context);
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    private void init(Context context) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true); // 去锯齿
        mPaint.setStrokeWidth(13);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.BLACK);
        mPath = new Path();
        mBitmap = Bitmap.createBitmap(screenWidth, screenHeight,
                Config.RGB_565);
        mCanvas = new Canvas(mBitmap);
        mCanvas.drawColor(Color.WHITE);
    }

    public static Bitmap scaleWithWH(Bitmap src, double w, double h) {
        if (w == 0 || h == 0 || src == null) {
            return src;
        } else {
            // ��¼src�Ŀ��
            int width = src.getWidth();
            int height = src.getHeight();
            // ����һ��matrix����
            Matrix matrix = new Matrix();
            // �������ű���
            float scaleWidth = (float) (w / width);
            float scaleHeight = (float) (h / height);
            // ��ʼ����
            matrix.postScale(scaleWidth, scaleHeight);
            // �������ź��ͼƬ
            return Bitmap.createBitmap(src, 0, 0, width, height, matrix, true);
        }
    }

    public Bitmap drawTextToBitmap(Context gContext, Bitmap bm, String gText) {
        Resources resources = gContext.getResources();
        float scale = resources.getDisplayMetrics().density;
        Bitmap bitmap = bm;

        bitmap = scaleWithWH(bitmap, 380, 200);
        Config bitmapConfig = bitmap.getConfig();
        // set default bitmap config if none
        if (bitmapConfig == null) {
            bitmapConfig = Config.RGB_565;
        }
        // resource bitmaps are imutable,
        // so we need to convert it to mutable one
        bitmap = bitmap.copy(bitmapConfig, true);
        Canvas canvas = new Canvas(bitmap);
        // new antialised Paint
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // text color - #3D3D3D
        paint.setColor(context.getResources().getColor(R.color.black));
        paint.setTextSize((int) (18 * scale));
        paint.setDither(true); // ��ȡ�������ͼ�����
        paint.setFilterBitmap(true);// ����һЩ
        Rect bounds = new Rect();
        paint.getTextBounds("", 0, "".length(), bounds);
        int x = 90;
        int y = 80;
        canvas.drawText("", x * scale, y * scale, paint);
        return bitmap;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(mBitmap, 0, 0, null);
        canvas.drawPath(mPath, mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                currentX = x;
                currentY = y;
                mPath.moveTo(currentX, currentY);
                break;
            case MotionEvent.ACTION_MOVE:
                currentX = x;
                currentY = y;
                mPath.quadTo(currentX, currentY, x, y); // 移动位置
                break;
            case MotionEvent.ACTION_UP:
                mCanvas.drawPath(mPath, mPaint);
                break;
        }

        invalidate();
        return true;
    }

    public Bitmap getPaintBitmap() {
        return resizeImage(mBitmap, 320, 480);
    }

    public Path getPath() {
        return mPath;
    }

    public Bitmap resizeImage(Bitmap bitmap, int width, int height) {
        int originWidth = bitmap.getWidth();
        int originHeight = bitmap.getHeight();

        float scaleWidth = ((float) width) / originWidth;
        float scaleHeight = ((float) height) / originHeight;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, originWidth,
                originHeight, matrix, true);
        Bitmap bm = drawTextToBitmap(context, resizedBitmap, text);
        return bm;
    }

    //清除
    public void clear() {

        if (mCanvas != null) {
            init(context);
            // mPath.reset();
            // mCanvas.drawColor(Color.WHITE);
            invalidate();
        }
    }

}
