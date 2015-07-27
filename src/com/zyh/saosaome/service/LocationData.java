package com.zyh.saosaome.service;

import android.text.TextUtils;

public class LocationData {
	public static int getBuilding(String longitude, String latitude) {
		if (TextUtils.isEmpty(longitude) || TextUtils.isEmpty(latitude)) {
			return 0;
		}
		int near = -1;
		int len = location.length;
		double dis = Double.MAX_VALUE;
		for (int i = 0; i < len; i++) {
			double tmp = LocationService.GetDistance(Double.parseDouble(latitude), Double.parseDouble(longitude),
					LocationData.location[i][1], LocationData.location[i][0]);
			if (tmp < dis) {
				dis = tmp;
				near = i;
			}
		}
		return near;
	}

	// 经度,纬度
	public static Double[][] location = { { 113.270217, 35.194415 }, // "1号教学楼"
			{ 113.268308, 35.194142 }, // "2号教学楼"
			{ 113.275719, 35.194315 }, // "3号教学楼",
			{ 113.268546, 35.191026 },// "电气综合楼",
			{ 113.267787, 35.191812 }, // 机械学院
			{ 113.267248, 35.192239 },// "能源综合楼"
			{ 113.267185, 35.192921 },// "资环综合楼",
			{ 113.265586, 35.192619 },// 测绘综合楼"
			{ 113.269072, 35.19162 },// 理化综合楼
			{ 113.268631, 35.192885 },// "语音室",
			{ 113.267185, 35.192936 },// 设计专家
			{ 113.26573, 35.191767 },// 土木综合楼
			{ 113.278347, 35.194562 },// , "经管综合楼"
			{ 113.278473, 35.193563 },// "音乐系"
			{ 113.279497, 35.194736 },// 材料综合楼
			{ 113.278598, 35.196376 },// 体育系
			{ 113.278472, 35.193574 },// 文科综合楼
			{ 113.27951, 35.193718 },// 计算机综合楼
			{ 113.279501, 35.199591 },// 实践教学
			{ 113.270208, 35.193098 } // "1号实验楼"
	};
	public static String[] nameOfLocation = { "1号教学楼", "2号教学楼", "3号教学楼", "电气综合楼", "机械综合楼", "能源综合楼", "资环综合楼", "测绘综合楼", "理化综合楼", "语音室",
			"设计专教", "土木综合楼", "经管综合楼", "音乐系", "材料综合楼", "体育系", "文科综合楼", "计算机综合楼", "实践教学", "1号实验楼" };
}
