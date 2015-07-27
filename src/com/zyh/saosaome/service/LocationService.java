package com.zyh.saosaome.service;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.zyh.saosaome.util.CommonUtils;
import com.zyh.saosaome.util.Constant;
import com.zyh.saosaome.util.L;
import com.zyh.saosaome.util.SP;
import com.zyh.saosaome.util.T;

public class LocationService {

	private final static double EARTH_RADIUS = 6378.137;
	public LocationClient mlocationClient = null;
	public BDLocationListener mListener;
	private double longitude, latitude;
	private String address;

	private Context mContext;

	private static double rad(double d) {
		return d * Math.PI / 180.0;
	}

	/**
	 * 根据两点间经纬度坐标（double值），计算两点间距离，单位为米
	 */
	public static double GetDistance(double lat1, double lng1, double lat2, double lng2) {
		double radLat1 = rad(lat1);
		double radLat2 = rad(lat2);
		double a = radLat1 - radLat2;
		double b = rad(lng1) - rad(lng2);
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2)
				* Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		// s = Math.round(s * 10000) / 10000;
		return s * 1000;
	}

	public double getLongitude() {
		return longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public LocationService(Context context) {
		mListener = new mBDLocationListener();
		mContext = context;
		mlocationClient = new LocationClient(mContext.getApplicationContext());// 声明LocationClient类
		mlocationClient.registerLocationListener(mListener); // 注册监听函数
	}

	public boolean getMyLocation() {
		if (CommonUtils.isNetworkAvailable(mContext.getApplicationContext())) {
			send();
			return true;
		} else {
			T.show(mContext, "当前无网络连接,定位失败");
		}
		return false;
	}

	// 设置定位参数
	public void InitLocation() {
		try {
			LocationClientOption option = new LocationClientOption();
			option.setLocationMode(LocationMode.Hight_Accuracy);
			option.setOpenGps(true);
			option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度，默认值gcj02
			// option.setScanSpan(1000);// 设置发起定位请求的间隔时间为1000ms
			option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
			option.setNeedDeviceDirect(true);// 返回的定位结果包含手机机头的方向
			mlocationClient.setLocOption(option);
			// System.out.println("初始化完毕");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void send() {
		InitLocation();
		// System.out.println("准备开启");
		// 发起定位请求。请求过程是异步的，定位结果在上面的监听函数onReceiveLocation中获取。
		mlocationClient.start();
		if (mlocationClient.isStarted()) {
			// System.out.println("开启成功");
			mlocationClient.requestLocation();
			// System.out.println("请求成功");
		}
	}

	// BDLocationListener接口有2个方法需要实现：
	// 1.接收异步返回的定位结果，参数是BDLocation类型参数。
	// 2.接收异步返回的POI查询结果，参数是BDLocation类型参数。
	public class mBDLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null) {
				return;
			}
			for (int i = 0; i < 100; i++) {
				L.i("" + location.getLocType());
				latitude = location.getLatitude();
				longitude = location.getLongitude();
				address = location.getAddrStr();
				L.i(latitude + "," + longitude + "," + location.getLocType());
				if (location.getLocType() == 161 || location.getLocType() == 61) {
					SP.put(mContext, Constant.LATI, latitude + "");
					SP.put(mContext, Constant.LONG, longitude + "");
					SP.put(mContext, Constant.ADDRESS, address + "");
					break;
				}
				send();
			}
		}
	}

}
