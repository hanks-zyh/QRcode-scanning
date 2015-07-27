package com.zyh.saosaome.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Toast常用类
 * @author LeeLay 2014-9-24
 */
public class T {

	/**
	 * 默认注释
	 * @param context
	 * @param content
	 */
	public static void show(Context context, String content) {
		Toast.makeText(context, content, 0).show();
	}

	/**
	 * 长显示
	 * @param context
	 * @param content
	 */
	public static void showL(Context context, String content) {
		Toast.makeText(context, content, 1).show();
	}

	/**
	 * 网络错误Toast
	 * @param context
	 */
	public static void showNetErr(Context context) {
		Toast.makeText(context, "网络错误请重试", 0).show();
	}

}
