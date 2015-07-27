package com.zyh.saosaome.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.graphics.BitmapFactory;

import com.crack.CrackTools;
import com.zyh.saosaome.util.Constant;
import com.zyh.saosaome.util.L;
import com.zyh.saosaome.util.StreamToString;

/**
 * 获取教务系统，抓包数据
 * 
 * @author LeeLay
 * 
 *         2014-9-25
 */
public class JwcServer {

	public JwcServer() {
	}

	private static CookieStore cookies = null;

	public static CookieStore getCookie() {
		return cookies;
	}

	/**
	 * 获取课表的第一部分(此方法已淘汰)
	 * 
	 * @return
	 * @throws Exception
	 * @throws IOException
	 */
	public static String getOldTable() throws Exception, IOException {

		DefaultHttpClient httpclient = new DefaultHttpClient();
		setConnTime(httpclient);
		httpclient.setCookieStore(cookies);

		// get请求课程表
		HttpGet get = new HttpGet(
				"http://218.196.240.97/xkAction.do?actionType=6");
		HttpResponse response = httpclient.execute(get);

		// 得到网页源码
		String html = StreamToString.InputStreamToString(response.getEntity()
				.getContent(), "GBK");

		String info = "";
		Document doc = Jsoup.parse(html);
		Elements element1 = doc.getElementsByTag("table").select("#user");
		Elements rows = element1.select("tr");
		for (Element element : rows) {
			Elements jcs = element.select("td");
			for (Element jc : jcs) {
				info += jc.text().replaceAll(Jsoup.parse("&nbsp;").text(), " ")
						+ "#";
			}
			info += "\n";
		}
		return info;
	}

	/**
	 * 设置超时时间
	 * 
	 * @param httpclient
	 */
	private static void setConnTime(DefaultHttpClient httpclient) {
		httpclient.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, 3000);// 连接时间20s
		 httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
		 5000);// 数据传输时间60s
	}

	/**
	 * 获取课表的第二部分
	 * 
	 * @return
	 * @throws Exception
	 * @throws IOException
	 */
	public static String getClassTable() throws Exception, IOException {
		DefaultHttpClient localDefaultHttpClient = new DefaultHttpClient();
		setConnTime(localDefaultHttpClient);
		localDefaultHttpClient.setCookieStore(cookies);
		String html = StreamToString
				.InputStreamToString(
						localDefaultHttpClient
								.execute(
										new HttpGet(
												"http://218.196.240.97/xkAction.do?actionType=6"))
								.getEntity().getContent(), "GBK");
		String info = "";
		Document doc = Jsoup.parse(html);
		Element element1 = doc.getElementsByTag("table").select("#user").get(1);
		Elements rows = element1.select("tr");
		for (Element element : rows) {
			Elements jcs = element.select("td");
			for (Element jc : jcs) {
				info += jc.text().replaceAll(Jsoup.parse("&nbsp;").text(), " ")
						+ "#";
			}
			info += "\n";
		}

		return info;
	}

	/**
	 * 获取验证码输入流，可以转换成BitMap对象
	 * 
	 * @return 返回的是验证码图片的InputStream
	 * @throws Exception
	 * @throws IOException
	 */
	public static InputStream getVcode2() throws Exception, IOException {
		DefaultHttpClient localDefaultHttpClient = new DefaultHttpClient();
		setConnTime(localDefaultHttpClient);
		HttpResponse localHttpResponse = localDefaultHttpClient
				.execute(new HttpGet(
						"http://218.196.240.97/validateCodeAction.do?random="
								+ Math.random()));
		cookies = localDefaultHttpClient.getCookieStore();
		return localHttpResponse.getEntity().getContent();
	}

	/**
	 * 获取验证码的内容
	 * 
	 * @return 返回的是验证码内容
	 * @throws Exception
	 * @throws IOException
	 */
	public static String getVcode() throws Exception, IOException {
		DefaultHttpClient localDefaultHttpClient = new DefaultHttpClient();
		setConnTime(localDefaultHttpClient);
		HttpResponse localHttpResponse = localDefaultHttpClient
				.execute(new HttpGet(
						"http://218.196.240.97/validateCodeAction.do?random="
								+ Math.random()));
		cookies = localDefaultHttpClient.getCookieStore();
		InputStream inputStream = localHttpResponse.getEntity().getContent();
		return CrackTools.recognize(BitmapFactory.decodeStream(inputStream));
	}

	/**
	 * 登陆
	 * 
	 * @param stu_num
	 *            学号
	 * @param password
	 *            密码
	 * @param v_code
	 *            验证码
	 * @return int 1:验证码错误 2:证件号不存在 3:密码错误 4:连接失败
	 * @throws Exception
	 * @throws IOException
	 */
	public static int login(String stu_num, String password, String v_code)
			throws Exception, IOException {
		DefaultHttpClient localDefaultHttpClient = new DefaultHttpClient();
		setConnTime(localDefaultHttpClient);
		localDefaultHttpClient.setCookieStore(cookies);
		HttpPost localHttpPost = new HttpPost(
				"http://218.196.240.97/loginAction.do");
		ArrayList<BasicNameValuePair> localArrayList = new ArrayList<BasicNameValuePair>();
		localArrayList.add(new BasicNameValuePair("zjh", stu_num));
		localArrayList.add(new BasicNameValuePair("mm", password));
		localArrayList.add(new BasicNameValuePair("v_yzm", v_code));
		localHttpPost.setEntity(new UrlEncodedFormEntity(localArrayList,
				"UTF-8"));
		HttpResponse localHttpResponse = localDefaultHttpClient
				.execute(localHttpPost);
		if (localHttpResponse.getStatusLine().getStatusCode() == 200) {
			String str = StreamToString.InputStreamToString(localHttpResponse
					.getEntity().getContent(), "GBK");
			// System.out.println(str);
			if (str.contains("验证码错误"))
				return Constant.VCODE_ERROR;
			if (str.contains("证件号不存在"))
				return Constant.NUM_ERROR;
			if (str.contains("密码不正确"))
				return Constant.PWD_ERROR;

			return Constant.LOGIN_OK;
		}
		return Constant.NET_ERROR;
	}

	/**
	 * 获取学生个人信息
	 * 
	 * @return 返回的是封装学生信息的Map对象，其中键为中文
	 * @throws Exception
	 */
	public static String getMainInfo() throws Exception {
		DefaultHttpClient localDefaultHttpClient = new DefaultHttpClient();
		setConnTime(localDefaultHttpClient);
		localDefaultHttpClient.setCookieStore(cookies);
		String str1 = StreamToString
				.InputStreamToString(
						localDefaultHttpClient
								.execute(
										new HttpGet(
												"http://218.196.240.97/xjInfoAction.do?oper=xjxx"))
								.getEntity().getContent(), "GBK");
		return str1;

	}

	/**
	 * 获取成绩
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String getChengji() throws Exception {

		DefaultHttpClient httpclient = new DefaultHttpClient();
		setConnTime(httpclient);
		httpclient.setCookieStore(cookies);

		// get请求课程表
		HttpGet get = new HttpGet(
				"http://218.196.240.97/gradeLnAllAction.do?type=ln&oper=qbinfo&lnxndm=2013-2014学年秋(两学期)");
		HttpResponse response = httpclient.execute(get);

		// 得到网页源码
		String html = StreamToString.InputStreamToString(response.getEntity()
				.getContent(), "GBK");
		Document doc = Jsoup.parse(html);
		String resString = "";
		Elements element1 = doc.getElementsByTag("table").select(
				"#user,#tblHead");
		L.i(element1.select("th").text());
		Elements rows = element1.select("tr");
		for (Element element : rows) {
			resString += element.select("td").text()
					.replaceAll(Jsoup.parse("&nbsp;").text(), " ");
			resString += "\n";
		}
		return resString;
	}

	/**
	 * 查询空闲教室前必须先get此页面
	 * 
	 * @return get请求成功返回true
	 * @throws Exception
	 * @throws IOException
	 */
	public static boolean findQueryPage() throws Exception, IOException {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		setConnTime(httpclient);
		httpclient.setCookieStore(cookies);
		HttpGet get = new HttpGet(
				"http://218.196.240.97/xszxcxAction.do?oper=xszxcx_lb");
		HttpResponse response = httpclient.execute(get);
		if (response.getStatusLine().getStatusCode() == 200)
			return true;
		else
			return false;
	}

	public static String getClassRoom(String jxl, String zc, String xq,
			String jc) throws Exception, IOException {

		DefaultHttpClient httpclient = new DefaultHttpClient();
		httpclient.setCookieStore(cookies);

		String path4 = "http://218.196.240.97/xszxcxAction.do?oper=tjcx";
		HttpPost post2 = new HttpPost(path4);

		List<NameValuePair> list2 = new ArrayList<NameValuePair>();
		list2.add(new BasicNameValuePair("currentPage", "1")); // 當前頁數
		list2.add(new BasicNameValuePair("page", "1")); // 總頁數
		list2.add(new BasicNameValuePair("pageNo", ""));
		list2.add(new BasicNameValuePair("pageSize", "100")); // 頁面記錄條數　　
		list2.add(new BasicNameValuePair("zxJc", jc)); // 節次
		list2.add(new BasicNameValuePair("zxxnxq", "2014-2015-1-1")); // 学年学期
		list2.add(new BasicNameValuePair("zxJxl", jxl)); // 教學樓
		list2.add(new BasicNameValuePair("zxXaq", "01")); // 校本部
		list2.add(new BasicNameValuePair("zxxq", xq)); // 星期
		list2.add(new BasicNameValuePair("zxZc", zc)); // 周次

		post2.setEntity(new UrlEncodedFormEntity(list2, HTTP.UTF_8));

		HttpResponse response = httpclient.execute(post2);

		L.i("" + response.getStatusLine().getStatusCode());
		String html3 = EntityUtils.toString(response.getEntity());

		// System.out.println(html3);

		String info = "";
		Document doc = Jsoup.parse(html3);
		Elements element1 = doc.getElementsByTag("tbody").select("#user");
		// System.out.println(element1.select("th").text());
		Elements rows = element1.select("tr");
		for (Element element : rows) {
			info += element.select("td").text()
					.replaceAll(Jsoup.parse("&nbsp;").text(), " ");
			info += "\n";
		}
		// System.out.println(info);
		// 关闭浏览器
		httpclient.getConnectionManager().shutdown();
		return info;

	}

}
