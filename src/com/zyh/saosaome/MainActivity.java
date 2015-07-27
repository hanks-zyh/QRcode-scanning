package com.zyh.saosaome;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.zyh.saosaome.net.MyNet;
import com.zyh.saosaome.service.LocationData;
import com.zyh.saosaome.service.LocationService;
import com.zyh.saosaome.util.Constant;
import com.zyh.saosaome.util.ProgressUtil;
import com.zyh.saosaome.util.SP;
import com.zyh.saosaome.util.T;

public class MainActivity extends BaseActivity implements OnClickListener {

	protected static final int MSG_COED = 0;
	private String username;
	private String lati, lon, address;
	private Button bt_state;
	private boolean isSend = false;
	private String myLocation;
	private String courseName = "未知";
	private String courseid = "XXX";
	private String courseLocationS;
	private AlertDialog dialog;
	private TextView tv_course;
	private TextView tv_course_location;
	private TextView tv_dis;
	private int dis;
	private String courseLoc = "未知";
	private ImageView iv_result;
	private int courseLocation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// 取学号
		username = (String) SP.get(context, "username", "");

		// 取经纬度
		lati = (String) SP.get(context, Constant.LATI, "");
		lon = (String) SP.get(context, Constant.LONG, "");
		address = (String) SP.get(context, Constant.ADDRESS, "");

		findViewById(R.id.bt_saosao).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				saosao();
			}
		});

		bt_state = (Button) findViewById(R.id.bt_state);
		bt_state.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showState();
			}
		});

	}

	/**
	 * 显示签到状态
	 */
	protected void showState() {

		View v = View.inflate(context, R.layout.dialog, null);
		v.findViewById(R.id.ib_del).setOnClickListener(this);
		v.findViewById(R.id.tv_ok).setOnClickListener(this);
		tv_course = (TextView) v.findViewById(R.id.tv_course);
		tv_course_location = (TextView) v.findViewById(R.id.tv_course_location);
		tv_dis = (TextView) v.findViewById(R.id.tv_dis);

		iv_result = (ImageView) v.findViewById(R.id.iv_result);
		iv_result.setImageResource(isSend ? R.drawable.pic_success
				: R.drawable.pic_failure);
		tv_course.setText(courseName);
		tv_course_location.setText(courseLoc);
		tv_dis.setText("距离上课地点 " + dis + "米");

		dialog = new AlertDialog.Builder(this).create();
		dialog.setView(v, 0, 0, 0, 0);
		dialog.show();

	}

	protected void saosao() {
		// 打开扫描界面扫描条形码或二维码
		Intent openCameraIntent = new Intent(context,
				MipcaActivityCapture.class);
		startActivityForResult(openCameraIntent, 0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && data != null) {
			// 扫描二维码返回的信息
			String result = data.getStringExtra("result");
			handleResult(result);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 处理扫描的结果
	 * 
	 * @param result
	 */
	private void handleResult(String result) {
		courseid = "错误Id";
		courseName = "错误名称";
		courseLocationS = "0";
		try {
			JSONObject json = new JSONObject(result);
			courseid = json.getString("courseNumber"); // 091010900+14+5+
			courseName = json.getString("courseName");// VC++程序设计
			courseLocation = json.getInt("courseAdress");// 17
		} catch (Exception e) {
			e.printStackTrace();
		}
//		T.showL(context, result);

		// int courseLocation = Integer.parseInt(courseLocationS);
		dis = (int) LocationService.GetDistance(Double.parseDouble(lati),
				Double.parseDouble(lon),
				LocationData.location[courseLocation][1],
				LocationData.location[courseLocation][0]);

		courseLoc = LocationData.nameOfLocation[courseLocation];
		// 我距上课地点的距离
		myLocation = "距离 " + courseLocation + "  " + dis + "米";

		ProgressUtil.showWaiting(context);
		new Thread() {
			public void run() {
				// 结果发送到服务器 并获取签到状态
				// 课程号（courseid） 学号（username） 位置（“计算机综合楼” / “未知”）
				int code = MyNet.send2Server(courseid, username, myLocation);
				Message.obtain(handler, MSG_COED, code).sendToTarget();
			};
		}.start();
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MSG_COED:
				ProgressUtil.dismiss();
				int code = (int) msg.obj;
				if (code == 200) {
					isSend = true;
					showState();
				} else {
					T.showL(context, "签到失败，请重试");
				}
				break;
			}
		};
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_del:
		case R.id.tv_ok:
			if (dialog != null && dialog.isShowing()) {
				dialog.dismiss();
				dialog = null;
			}
			break;
		}
	}

}
