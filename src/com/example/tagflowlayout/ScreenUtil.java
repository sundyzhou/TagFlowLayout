package com.example.tagflowlayout;

import android.content.Context;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

public class ScreenUtil {

	/**
	 * 
	 * @param context
	 * @return
	 */
	public static int getScreenWidth(Context context) {
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		return display.getWidth();
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	public static int getScreenHeight(Context context) {
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		return display.getHeight();
	}

	/**
	 * 
	 * @return
	 */
	public static int getScreenViewTopHeight(View view) {
		return view.getTop();
	}

	/**
	 * 
	 * @return
	 */
	public static int getScreenViewBottomHeight(View view) {
		return view.getBottom();
	}

	/**
	 * 
	 * @return
	 */
	public static int getScreenViewLeftHeight(View view) {
		return view.getLeft();
	}

	/**
	 * 
	 * @return
	 */
	public static int getScreenViewRightHeight(View view) {
		return view.getRight();
	}

}