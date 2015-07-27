package com.zyh.saosaome.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;

import com.zyh.saosaome.R;

public class A {

	public static void goOtherActivity(Context context, Class<?> cls) {
		context.startActivity(new Intent(context, cls));
		((Activity) context).overridePendingTransition(R.anim.push_right_in,
				R.anim.push_left_out);
	}

	public static void goOtherActivityFinish(Context context, Class<?> cls) {
		context.startActivity(new Intent(context, cls));
		((Activity) context).finish();
		((Activity) context).overridePendingTransition(R.anim.push_right_in,
				R.anim.push_left_out);
	}

	public static void goOtherActivity(Context context, Intent intent) {
		context.startActivity(intent);
		((Activity) context).overridePendingTransition(R.anim.push_right_in,
				R.anim.push_left_out);
	}

	public static void goOtherActivityFinish(Context context, Intent intent) {
		context.startActivity(intent);
		((Activity) context).finish();
		((Activity) context).overridePendingTransition(R.anim.push_right_in,
				R.anim.push_left_out);
	}

	public static void finishSelf(Context context) {
		((Activity) context).finish();
		// ((Activity)
		// context).overridePendingTransition(R.anim.activity_left_enter,
		// R.anim.activity_right_exit);
	}

	public static void goOtherActivityBottomIn(Context context, Intent intent) {
		context.startActivity(intent);
		// ((Activity) context).overridePendingTransition(0,
		// R.anim.slide_in_from_bottom);
	}

	public static void goOtherActivityNoAnim(Context context, Intent intent) {
		context.startActivity(intent);
	}

	public static void finishSelfNoAnim(Context context) {
		((Activity) context).finish();
		((Activity) context).overridePendingTransition(0, 0);
	}

	public static void startNetworkSettingActivity(Context context) {
		Intent intent = new Intent();
		int sdkVersion = VERSION.SDK_INT;
		if (sdkVersion >= 14) {
			intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
		} else {
			intent.setClassName("com.android.settings",
					"com.android.settings.WirelessSettings");// android4.0系统找不到此activity。
		}
		context.startActivity(intent);
		 ((Activity) context).overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
	}

}
