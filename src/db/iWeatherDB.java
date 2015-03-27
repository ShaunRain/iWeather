package db;

import java.util.ArrayList;
import java.util.List;

import model.oneDay;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class iWeatherDB {

	public static final String DB_NAME = "iweather";
	public static final int VERSION = 1;
	private static iWeatherDB iweatherDB;
	private SQLiteDatabase db;

	private iWeatherDB(Context context) {
		iWeatherOpenHelper helper = new iWeatherOpenHelper(context, DB_NAME,
				null, VERSION);
		db = helper.getWritableDatabase();
	}

	public static iWeatherDB getInstance(Context context) {
		if (iweatherDB == null) {
			iweatherDB = new iWeatherDB(context);
		}
		return iweatherDB;
	}

	public void saveDays(oneDay day) {
		if (day != null) {

			Cursor cursor = db.rawQuery(
					"select * from Days where city_name=? and day_id=?",
					new String[] { day.getCity_name(), day.getDay_id() + "" });
			
			boolean result = cursor.moveToNext();

			if (result) {
				updateDays(day);
			} else {

				ContentValues values = new ContentValues();
				values.put("city_name", day.getCity_name());
				values.put("date", day.getDate());
				values.put("day_id", day.getDay_id());
				values.put("weather", day.getWeather());
				values.put("wind", day.getWind());
				values.put("temp", day.getTemp());

				db.insert("Days", null, values);
			}
			cursor.close();
		}
	}

	public void updateDays(oneDay day) {
		int id = day.getDay_id();
		db.execSQL(
				"update Days set date=?,weather=?,wind=?,temp=? where city_name=? and day_id=?",
				new Object[] { day.getDate(), day.getWeather(), day.getTemp(),
						day.getCity_name(), id });
		//Log.d("day_id??", day.getCity_name() + day.getDay_id());
		// db.close();

	}

	public List<oneDay> loadDays(String cityName) {
		List<oneDay> list = new ArrayList<oneDay>();
		Cursor cursor = db.query("Days", null, "city_name=?",
				new String[] { String.valueOf(cityName) }, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				oneDay oneday = new oneDay();

				oneday.setCity_name(cityName);
				oneday.setDate(cursor.getString(cursor.getColumnIndex("date")));
				oneday.setDay_id(cursor.getColumnIndex("day_id"));
				//Log.d("day_id?", cityName + cursor.getString(cursor.getColumnIndex("date")) + cursor.getColumnIndex("day_id"));
				oneday.setWeather(cursor.getString(cursor
						.getColumnIndex("weather")));
				oneday.setWind(cursor.getString(cursor.getColumnIndex("wind")));
				oneday.setTemp(cursor.getString(cursor.getColumnIndex("temp")));

				list.add(oneday);

			} while (cursor.moveToNext());
		}
		cursor.close();
		return list;
	}
	

	public void deleteCity(String cityName) {
		db.execSQL("delete from Days where city_name=?",
				new Object[] { cityName });
	}

	public ArrayList<String> findAllCity() {
		ArrayList<String> allCity = new ArrayList<String>();
		Cursor cursor = db
				.rawQuery("select * from Days where day_id = 0", null);
		while (cursor.moveToNext()) {
			String cityName = cursor.getString(cursor
					.getColumnIndex("city_name"));
			allCity.add(cityName);
		}
		cursor.close();
		return allCity;
	}

}
