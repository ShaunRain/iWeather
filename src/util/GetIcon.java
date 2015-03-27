package util;

import com.rain.iweather.R;

public class GetIcon {

	private static String[] weathers = { "未知", "晴", "多云", "阴", "阵雨", "雷阵雨",
			"雷阵雨伴有冰雹", "雨夹雪", "小雨", "中雨", "大雨", "暴雨", "大暴雨", "特大暴雨", "阵雪",
			"小雪", "中雪", "大雪", "暴雪", "雾", "冻雨", "沙尘暴", "浮尘", "扬沙", "强沙尘暴", "霾" };
	private static int[] icons = { R.drawable.nullweather, R.drawable.sunnyday,
			R.drawable.cloudyday, R.drawable.overcast, R.drawable.showerday,
			R.drawable.thundershowerday, R.drawable.hail,
			R.drawable.rainsnowday, R.drawable.lightrain, R.drawable.midrain,
			R.drawable.heavyrain, R.drawable.heavyrain, R.drawable.downpour,
			R.drawable.showerday, R.drawable.midsnow, R.drawable.lightsnow,
			R.drawable.midsnow, R.drawable.largesnow, R.drawable.heavysnow,
			R.drawable.fog, R.drawable.rainsnowday, R.drawable.dust,
			R.drawable.smoke, R.drawable.smoke, R.drawable.dust,
			R.drawable.haze, };

	public int stringToIcon(String weatherName) {
		int result = 0;
		if (weatherName.contains("转")) {
			weatherName = weatherName.replace("转", "=");
			String[] weatherNeed = weatherName.split("=");
			weatherName = weatherNeed[0];
		}else if (weatherName.contains("到")) {
			weatherName = weatherName.replace("到", "=");
			String[] weatherNeed = weatherName.split("=");
			weatherName = weatherNeed[1];
		}
		for (int i = 0; i < weathers.length; i++) {
			if (weathers[i].equals(weatherName)) {
				result = icons[i];
				return result;
			}
		}
		return result;
	}

}
