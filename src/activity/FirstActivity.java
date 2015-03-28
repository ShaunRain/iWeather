package activity;

import java.util.ArrayList;

import model.oneDay;

import util.Adaptbitmap;
import util.ParseData;

import com.rain.iweather.R;

import db.iWeatherDB;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.audiofx.BassBoost.Settings;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

public class FirstActivity extends Activity {
	private final int SPLASH_DISPLY_LENGTH = 2500;
	private Adaptbitmap adapt;

	private iWeatherDB iweatherdb;
	private ParseData parseData;

	private boolean flag = false;
	private String[] cityList = null;
	private ArrayList<String> allCity;
	private int cityCount;

	public static NotificationManager manager;

	private Thread thread = new Thread(new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			if (!allCity.isEmpty()) {
				int i = 0;
				for (String cityName : allCity) {
					parseData = new ParseData(cityName, iweatherdb);
					cityList[i] = cityName;
					i++;
				}
			} else {
				parseData = new ParseData("宝鸡", iweatherdb);
				cityList[0] = "宝鸡";
				flag = true;
			}
			Log.d("index??", iweatherdb.loadDays(cityList[0]).size()+","+cityList.length);
			oneDay oneday = iweatherdb.loadDays(cityList[0]).get(0);
			

			Intent foreground = new Intent(FirstActivity.this,
					FirstActivity.class);
			manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			PendingIntent pi = PendingIntent.getActivity(
					getApplicationContext(), 0, foreground,
					PendingIntent.FLAG_UPDATE_CURRENT);
			Notification noti = new Notification.Builder(
					getApplicationContext())
					.setContentIntent(pi)
					.setContentTitle(oneday.getCity_name())
					.setContentText(
							oneday.getWeather() + "," + oneday.getTemp() + ","
									+ oneday.getWind())
					.setSmallIcon(R.drawable.ic_launcher).setTicker("iWeather")
					.build();
			noti.flags = Notification.FLAG_ONGOING_EVENT;
			manager.notify(1, noti);

			Intent mainIntent = new Intent(FirstActivity.this,
					ShowWeatherActivity.class);
			mainIntent.putStringArrayListExtra("citys", allCity);
			mainIntent.putExtra("cityList", cityList);
			mainIntent.putExtra("flag", flag);
			mainIntent.putExtra("cityCount", cityCount);
			FirstActivity.this.startActivity(mainIntent);
			FirstActivity.this.finish();
		}

	});

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_first);

		iweatherdb = iWeatherDB.getInstance(this);
		allCity = iweatherdb.findAllCity();
		cityCount = allCity.size();
		if (cityCount == 0) {
			cityCount++;
		}
		cityList = new String[cityCount];

		if (!isConnected() && cityCount == 0) {
			new AlertDialog.Builder(this).setTitle("网络连接失败！")
					.setMessage("首次进入程式需要网络连接，请检查网络设置后再试。")
					.setPositiveButton("确定", new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							startActivity(new Intent(
									android.provider.Settings.ACTION_SETTINGS));
							finish();
						}
					}).setNegativeButton("取消", new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							finish();
						}
					}).show();
		} else {

			new Handler().postDelayed(new Runnable() {

				public void run() { // TODO Auto-generated method stub Intent
					/*
					 * Intent delay = new Intent(FirstActivity.this,
					 * ShowWeatherActivity.class);
					 * delay.putStringArrayListExtra("citys", allCity);
					 * delay.putExtra("cityList", cityList);
					 * delay.putExtra("flag", flag); delay.putExtra("cityCount",
					 * cityCount); FirstActivity.this.startActivity(delay);
					 * FirstActivity.this.finish();
					 */
					thread.setPriority(10);
					thread.start();
				}
			}, SPLASH_DISPLY_LENGTH);

		}

	}

	public boolean isConnected() {
		Context context = getApplicationContext();
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(CONNECTIVITY_SERVICE);
		if (manager != null) {
			NetworkInfo info = manager.getActiveNetworkInfo();
			if (info != null && info.isConnected()) {
				return true;
			}
			return false;
		}
		return false;
	}
}
