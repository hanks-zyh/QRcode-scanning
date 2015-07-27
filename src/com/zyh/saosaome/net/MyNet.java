package com.zyh.saosaome.net;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.util.Log;

public class MyNet {
	/**
	 * 发送信息给服务器
	 * 
	 * @param courseId  该节课 的 id
	 * @param username  学号
	 * @param address   学生的位置
	 * @return
	 */
	public static int send2Server(String courseId, String username,
			String address) {
		String url = "http://122.207.132.208:8080/register/request_getinfo.action";
		int reslut = 404;
		try {
			HttpClient client = new DefaultHttpClient();
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("courseId", courseId));
			list.add(new BasicNameValuePair("username", username));
			list.add(new BasicNameValuePair("address", address));
			HttpPost post = new HttpPost(url);
			post.setEntity(new UrlEncodedFormEntity(list, "UTF-8"));
			HttpResponse respose = client.execute(post);
			reslut = respose.getStatusLine().getStatusCode();
			Log.d("", "签到结果:"+reslut);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reslut;
	}
}
