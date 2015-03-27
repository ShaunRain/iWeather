package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import model.oneDay;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import db.iWeatherDB;

public class ParseData {

	String weatherUrl = "http://api.map.baidu.com/telematics/v3/weather?location=";

	public static String[][] results = new String[10][4];
	public static String pm25;
	public static String currentCity;

	private static String city;
	private static iWeatherDB iweatherdb;

	public static String status;

	public ParseData(String city, iWeatherDB iweatherdb) {

		try {
			String weatherJson = fetchWeatherInfo(city);
			JSONObject object = stringToJson(weatherJson);
			setWeatherData(object, iweatherdb);
		} catch (IOException e) {
			Log.d("ErrorType", 0 + "");
			e.printStackTrace();
		} catch (JSONException e) {
			Log.d("ErrorType", 1 + "");
			e.printStackTrace();
		}
	}

	/*
	 * 获取json数据
	 */
	public String fetchWeatherInfo(String cityName) throws IOException {
		String requestUrl = weatherUrl + URLEncoder.encode(cityName, "utf-8")
				+ "&output=json&ak=pzqB3P6ZceCFO1LZRwfYoDLy";
		URL url = new URL(requestUrl);
		URLConnection conn = url.openConnection();
		InputStream is = conn.getInputStream();
		InputStreamReader isr = new InputStreamReader(is, "utf-8");
		BufferedReader br = new BufferedReader(isr);
		StringBuilder sb = new StringBuilder();

		String line = null;
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}
		br.close();
		System.out.println(sb.toString());
		return sb.toString();
	}

	/*
	 * 将json数据转换为json对象
	 */
	public JSONObject stringToJson(String str) throws JSONException {
		JSONObject jsonObject = new JSONObject(str);
		return jsonObject;
	}

	/*
	 * 解析json对象
	 */
	public void setWeatherData(JSONObject object, iWeatherDB iweatherdb)
			throws JSONException {

		try {
			status = object.getString("status");

			JSONArray result = object.getJSONArray("results");
			JSONObject resultObj = result.getJSONObject(0);

			currentCity = resultObj.getString("currentCity");

			JSONArray index = resultObj.getJSONArray("index");
			if (index.length() != 0) {
				for (int i = 0; i < 6; i++) {
					JSONObject indexInfo = index.getJSONObject(i);
					results[i][0] = indexInfo.getString("title");
					results[i][1] = indexInfo.getString("zs");
					results[i][2] = indexInfo.getString("tipt");
					results[i][3] = indexInfo.getString("des");
				}
			}

			JSONArray weather = resultObj.getJSONArray("weather_data");
			for (int i = 0; i < 4; i++) {
				JSONObject weatherInfo = weather.getJSONObject(i);
				results[i + 6][0] = weatherInfo.getString("date");
				results[i + 6][1] = weatherInfo.getString("weather");
				results[i + 6][2] = weatherInfo.getString("wind");
				results[i + 6][3] = weatherInfo.getString("temperature");

				oneDay oneday = new oneDay();
				oneday.setCity_name(currentCity);
				oneday.setDate(weatherInfo.getString("date"));
				oneday.setDay_id(i);
				oneday.setWeather(weatherInfo.getString("weather"));
				oneday.setWind(weatherInfo.getString("wind"));
				oneday.setTemp(weatherInfo.getString("temperature"));

				iweatherdb.saveDays(oneday);
			}

			pm25 = resultObj.getString("pm25");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * 打印数据
	 */
	public void printData() {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 4; j++) {
				System.out.println(results[i][j]);
			}
		}
		System.out.println(pm25);
		System.out.println(currentCity);
	}

}
