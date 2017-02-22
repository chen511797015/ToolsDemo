package com.pax.printertest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.WindowManager;
import android.widget.ScrollView;

@SuppressLint("NewApi")
public class PrintUtil {

	/**
	 * get the bitmap of a scrollView *
	 */
	public static Bitmap getViewBitmap(Context ctx, ScrollView sv) {
		if (null == sv) {
			return null;
		}
		// enable something
		sv.setVerticalScrollBarEnabled(false);
		sv.setVerticalFadingEdgeEnabled(false);
		sv.scrollTo(0, 0);
		sv.setDrawingCacheEnabled(true);
		sv.buildDrawingCache(true);
		Bitmap b = getViewBpWithoutBottom(sv);

		/**
		 * vh : the height of the scrollView that is visible <BR>
		 * th : the total height of the scrollView <BR>
		 **/
		int vh = sv.getHeight();
		int th = sv.getChildAt(0).getHeight();

		/** the total height is more than one screen */
		Bitmap temp = null;
		if (th > vh) {
			int w = 384;
			// int w = getScreenW(ctx);
			int absVh = vh - sv.getPaddingTop() - sv.getPaddingBottom();
			do {
				int restHeight = th - vh;
				if (restHeight <= absVh) {
					sv.scrollBy(0, restHeight);
					vh += restHeight;
					temp = getViewBp(sv);
				} else {
					sv.scrollBy(0, absVh);
					vh += absVh;
					temp = getViewBpWithoutBottom(sv);
				}
				b = mergeBitmap(vh, w, temp, 0, sv.getScrollY(), b, 0, 0);
			} while (vh < th);
		}

		// restore somthing
		sv.scrollTo(0, 0);
		sv.setVerticalScrollBarEnabled(true);
		sv.setVerticalFadingEdgeEnabled(true);
		sv.setDrawingCacheEnabled(false);
		sv.destroyDrawingCache();
		return b;
	}

	public static Bitmap mergeBitmap(int newImageH, int newIamgeW,
			Bitmap background, float backX, float backY, Bitmap foreground,
			float foreX, float foreY) {
		if (null == background || null == foreground) {
			return null;
		}
		// create the new blank bitmap ����һ���µĺ�SRC���ȿ��һ���λͼ
		Bitmap newbmp = Bitmap.createBitmap(newIamgeW, newImageH,
				Config.RGB_565);
		Canvas cv = new Canvas(newbmp);
		// draw bg into
		cv.drawBitmap(background, backX, backY, null);
		// draw fg into
		cv.drawBitmap(foreground, foreX, foreY, null);
		// save all clip
		cv.save(Canvas.ALL_SAVE_FLAG);// ����
		// store
		cv.restore();// �洢

		return newbmp;
	}

	public static Bitmap getViewBp(View v) {
		if (null == v) {
			return null;
		}
		v.setDrawingCacheEnabled(true);
		v.buildDrawingCache();
		if (Build.VERSION.SDK_INT >= 11) {
			v.measure(MeasureSpec.makeMeasureSpec(v.getWidth(),
					MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(
					v.getHeight(), MeasureSpec.EXACTLY));
			v.layout((int) v.getX(), (int) v.getY(),
					(int) v.getX() + v.getMeasuredWidth(),
					(int) v.getY() + v.getMeasuredHeight());
		} else {
			v.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
					MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
			v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
		}
		Bitmap b = Bitmap.createBitmap(v.getDrawingCache(), 0, 0,
				v.getMeasuredWidth(), v.getMeasuredHeight());

		v.setDrawingCacheEnabled(false);
		v.destroyDrawingCache();
		return b;
	}

	public static Bitmap getViewBpWithoutBottom(View v) {
		if (null == v) {
			return null;
		}
		v.setDrawingCacheEnabled(true);
		v.buildDrawingCache();
		if (Build.VERSION.SDK_INT >= 11) {
			v.measure(MeasureSpec.makeMeasureSpec(v.getWidth(),
					MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(
					v.getHeight(), MeasureSpec.EXACTLY));

			v.layout((int) v.getX(), (int) v.getY(),
					(int) v.getX() + v.getMeasuredWidth(),
					(int) v.getY() + v.getMeasuredHeight());
		} else {
			v.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
					MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
			v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
		}

		Bitmap bp = Bitmap.createBitmap(v.getDrawingCache(), 0, 0,
				v.getMeasuredWidth(),
				v.getMeasuredHeight() - v.getPaddingBottom());
		v.setDrawingCacheEnabled(false);
		v.destroyDrawingCache();
		return bp;
	}

	public static int getScreenW(Context ctx) {
		int w = 0;
		if (Build.VERSION.SDK_INT > 13) {
			Point p = new Point();
			((WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE))
					.getDefaultDisplay().getSize(p);
			w = p.x;
		} else {
			w = ((WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE))
					.getDefaultDisplay().getWidth();
		}
		return w;
	}

	public static int getScrollW(ScrollView scrollView) {
		scrollView.measure(
				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		return scrollView.getMeasuredWidth();
	}

}
