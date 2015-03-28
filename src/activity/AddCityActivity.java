package activity;

import java.util.ArrayList;

import model.City;
import model.oneDay;

import util.Adaptbitmap;
import util.ParseData;

import com.rain.iweather.R;

import db.iWeatherDB;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.Display;
import android.view.WindowManager;
import android.widget.SearchView;
import android.widget.SearchView.OnCloseListener;
import android.widget.SearchView.OnQueryTextListener;

public class AddCityActivity extends Activity {
	private Adaptbitmap adapt;
	private ParseData parseData;
	private iWeatherDB iweatherdb;

	private SearchView citySearch;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addcity);

		Display display = ((WindowManager) getSystemService(WINDOW_SERVICE))
				.getDefaultDisplay();
		Bitmap back = adapt.decodeSampledBitmapFromResource(getResources(),
				R.drawable.up_title_blur, display.getWidth(),
				display.getHeight());
		Drawable background = new BitmapDrawable(back);
		AddCityActivity.this.getWindow().setBackgroundDrawable(background);
		// setBack();
		citySearch = (SearchView) findViewById(R.id.sv_search);
		iweatherdb = iWeatherDB.getInstance(this);

		citySearch.setOnCloseListener(new OnCloseListener() {

			@Override
			public boolean onClose() {
				finish();
				return false;
			}
		});
		citySearch.setOnQueryTextListener(new OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(final String query) {
				final Intent intent = new Intent();
				new Thread(new Runnable() {

					@Override
					public void run() {
						parseData = new ParseData(query, iweatherdb);
						String status = parseData.status;
						if (status.equals("success")) {

							ArrayList<oneDay> days = (ArrayList<oneDay>) iweatherdb
									.loadDays(query);
							City city = new City(days.get(0).getCity_name(),
									days.get(0).getTemp());
							intent.putExtra("cityName", query);
							intent.putExtra("acity", city);
							setResult(1, intent);
							finish();
						} else {
							intent.putExtra("cityName", "hehe");
							setResult(1, intent);
							finish();
						}
					}
				}).start();
				return true;

			}

			@Override
			public boolean onQueryTextChange(String newText) {
				// TODO Auto-generated method stub
				return false;
			}
		});

	}

}
