package com.zyh.saosaome;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.zyh.saosaome.service.JwcServer;
import com.zyh.saosaome.util.A;
import com.zyh.saosaome.util.CommonUtils;
import com.zyh.saosaome.util.Constant;
import com.zyh.saosaome.util.L;
import com.zyh.saosaome.util.ProgressUtil;
import com.zyh.saosaome.util.SP;
import com.zyh.saosaome.util.T;

public class LoginActivity extends BaseActivity {

	private EditText et_username;
	private EditText et_password;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			ProgressUtil.dismiss();
			switch (msg.what) {
				case Constant.NUM_ERROR:
					T.show(context, "学号不存在");
					break;
				case Constant.PWD_ERROR:
					T.show(context, "密码不正确");
					break;
				case Constant.NET_ERROR:
					T.showNetErr(context);
					break;
				case Constant.LOGIN_OK:
					L.i("*********应经跳过来了");
					SP.put(context, "username", username);
					SP.put(context, "password", password);
					SP.put(context, "first", false);
					A.goOtherActivityFinish(context,  MainActivity.class);
					break;
			}
		};
	};
	private String username;
	private String password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		init();
	}

	/**
	 * 初始化
	 */
	private void init() {
		et_username = (EditText) findViewById(R.id.et_username);
		et_password = (EditText) findViewById(R.id.et_password);
		et_username.setText((String) SP.get(context, "username", ""));
		et_password.setText((String) SP.get(context, "password", ""));
		findViewById(R.id.et_login).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				login();
			}
		});;

	}

	protected void login() {
		username = et_username.getText().toString().trim();
		password = et_password.getText().toString().trim();
		if (TextUtils.isEmpty(username)) {
			T.show(context, "学号不能为空");
			return;
		}
		if (TextUtils.isEmpty(password)) {
			T.show(context, "密码不能为空");
			return;
		}
		if (!CommonUtils.isNetworkAvailable(context)) {
			T.show(context, "网络不可用,请检查网络");
			return;
		}

		ProgressUtil.showWaiting(context);
		new Thread() {
			public void run() {
				try {
					String code = JwcServer.getVcode();
					if (code == null) {
						T.show(context, "验证码错误");
						ProgressUtil.dismiss();
						return;
					}
					int reslut = JwcServer.login(username, password, code);
					Message.obtain(handler, reslut).sendToTarget();
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		}.start();
	}
}
