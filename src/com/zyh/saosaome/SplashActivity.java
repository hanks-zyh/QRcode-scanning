package com.zyh.saosaome;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;

import com.zyh.saosaome.service.LocationService;
import com.zyh.saosaome.util.A;
import com.zyh.saosaome.util.CommonUtils;
import com.zyh.saosaome.util.SP;

public class SplashActivity extends Activity {
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		context = this;
		checkNet();
	}

	/**
	 * 初始化
	 */
	private void init() {
		// 开启定位服务
		LocationService locationService = new LocationService(this);
		locationService.getMyLocation();
		new Handler().postDelayed(new Runnable() {
			public void run() {
				if ((boolean) SP.get(context, "first", true)) {
					A.goOtherActivityFinish(context, LoginActivity.class);
				} else {
					A.goOtherActivityFinish(context, MainActivity.class);
				}
			}
		}, 2000);
	}

	private void checkNet() {
		if (!CommonUtils.isNetworkAvailable(context)) {
			new AlertDialog.Builder(context)
					.setTitle("网络设置提示")
					.setMessage("网络连接不可用,是否进行设置?")
					.setPositiveButton("设置",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									A.startNetworkSettingActivity(context);
								}
							})
					.setNegativeButton("退出",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									onBackPressed();
								}
							}).show();
			;
		} else {
			init();
		}
	}
}
