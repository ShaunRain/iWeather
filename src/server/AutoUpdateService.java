package server;

import java.util.ArrayList;

import com.rain.iweather.R;

import receiver.AutoUpdateReceiver;

import util.ParseData;
import db.iWeatherDB;
import activity.FirstActivity;
import activity.ShowWeatherActivity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.os.SystemClock;
import android.text.format.Time;
import android.util.Log;

public class AutoUpdateService extends Service {
	private static iWeatherDB iweatherdb;
	private static ParseData parseData;
	private ArrayList<String> allCity;
	private String[] cityList;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		iweatherdb = iWeatherDB.getInstance(this);
		allCity = iweatherdb.findAllCity();
		cityList = allCity.toArray(new String[allCity.size()]);
		new Thread(new Runnable() {

			@Override
			public void run() {
				updateWeather();

			}
		}).start();
		AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
		int space = 6 * 60 * 60 * 1000;
		long triggerAtTime = SystemClock.elapsedRealtime() + space;
		Intent i = new Intent(this, AutoUpdateReceiver.class);
		PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
		manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
		return super.onStartCommand(intent, flags, startId);
	}

	private void updateWeather() {
		if (isConnected()) {
			for (String cityName : allCity)
				parseData = new ParseData(cityName, iweatherdb);
			Time time = new Time();
			time.setToNow();
			int da = time.monthDay;
			int ho = time.hour;
			int mi = time.minute;
			int se = time.second;
			ShowWeatherActivity.updateTime = da + "日" + ho + ":" + mi + ":"
					+ se + "更新";
			Log.d("update!", "" + ShowWeatherActivity.updateTime);
		} else {
			FirstActivity.manager.cancel(1);
			stopSelf();
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
