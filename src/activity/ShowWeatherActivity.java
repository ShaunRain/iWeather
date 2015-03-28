package activity;

import java.util.ArrayList;
import java.util.List;

import model.City;
import model.oneDay;

import server.AutoUpdateService;
import util.GetIcon;
import util.ParseData;

import com.rain.iweather.R;

import db.iWeatherDB;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ShowWeatherActivity extends Activity implements OnClickListener {
	public static ShowWeatherActivity instance;

	private ArrayList<View> listViews = null;
	private GetIcon getIcon;

	private ViewPager pager;

	private Button addButton;
	private Button refreshButton;
	public static String updateTime = null;

	private int count;

	private MyPageAdapter adapter;

	private iWeatherDB iweatherdb;
	private ParseData parseData;
	private ArrayList<String> citys;
	private String[] cityList;
	private int cityCount;
	private boolean flag;
	private List<oneDay> listDays;

	private ArrayList<City> cityNow = new ArrayList<>();
	// private String[][] content = new String[10][4];
	private String[][] content_weather = new String[4][4];
	private String[][] content_date = new String[4][4];
	private String[][] content_temp = new String[4][4];
	private String[][] content_wind = new String[4][4];

	private int[] showweather_layout = { R.layout.activity_showweather,
			R.layout.activity_showweather2, R.layout.activity_showweather3,
			R.layout.activity_showweather4 };
	private int[] showweather_id = { R.id.showweather1, R.id.showweather2,
			R.id.showweather3, R.id.showweather4 };

	private TextView[] tv_cityName = new TextView[4];
	private TextView[] tv_weather = new TextView[4];
	// private TextView[] tv_weather = new TextView[4];
	private TextView[][] tv_date = new TextView[4][4];
	private TextView[][] tv_temp = new TextView[4][4];
	private ImageView[][] iv_weather = new ImageView[4][3];

	// private TextView[] textViews = new TextView[10];
	public static String whatever;

	private View[] pageViews = new View[10];

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			Typeface face = Typeface.createFromAsset(getAssets(),
					"fonts/child.ttf");

			Time time = new Time();
			time.setToNow();
			int da = time.monthDay;
			int ho = time.hour;
			int mi = time.minute;
			int se = time.second;

			getIcon = new GetIcon();

			if (msg.what == 9) {

				for (int i = 0; i < count; i++) {
					listDays = iweatherdb.loadDays(cityList[i]);
					int j = 0;
					String theName = null;
					for (oneDay day : listDays) {
						// content[i][j] = day.getCity_name() + day.getDay_id()
						// + day.getDate() + day.getWeather();
						theName = day.getCity_name();
						content_weather[i][j] = day.getWeather();
						content_date[i][j] = day.getDate();
						content_temp[i][j] = day.getTemp();
						content_wind[i][j] = day.getWind();
						j++;
					}
					/*
					 * textViews[i].setText(content[i][0] + "\n" + content[i][1]
					 * + "\n" + content[i][2] + "\n" + content[i][3]);
					 * textViews[i].invalidate();
					 */
					tv_cityName[i].setText(theName);
					tv_weather[i].setText(content_weather[i][0] + " / "
							+ content_wind[i][0]);
					tv_date[i][0].setText(content_date[i][0]);
					tv_date[i][1].setText(content_date[i][1]);
					tv_date[i][2].setText(content_date[i][2]);
					tv_date[i][3].setText(content_date[i][3]);

					tv_temp[i][0].setText(content_temp[i][0]);
					tv_temp[i][1].setText(content_temp[i][1]);
					tv_temp[i][2].setText(content_temp[i][2]);
					tv_temp[i][3].setText(content_temp[i][3]);

					int i1 = getIcon.stringToIcon(content_weather[i][1]);
					int i2 = getIcon.stringToIcon(content_weather[i][2]);
					int i3 = getIcon.stringToIcon(content_weather[i][3]);
					iv_weather[i][0].setImageResource(i1);
					iv_weather[i][1].setImageResource(i2);
					iv_weather[i][2].setImageResource(i3);
					final int n = i;
					iv_weather[i][0].setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							Toast.makeText(getApplicationContext(),
									content_weather[n][1] + "", 1).show();

						}
					});
					iv_weather[i][1].setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							Toast.makeText(getApplicationContext(),
									content_weather[n][2] + "", 1).show();

						}
					});
					iv_weather[i][2].setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							Toast.makeText(getApplicationContext(),
									content_weather[n][3] + "", 1).show();

						}
					});

					listDays.clear();
				}
				/*
				 * updateTime = da + "日" + ho + ":" + mi + ":" + se + "更新";
				 * Toast.makeText(getApplicationContext(), "更新成功！", 1).show();
				 */

			} else {

				if (cityList.length == 0) {
					listDays = iweatherdb.loadDays("宝鸡");
				} else {
					listDays = iweatherdb.loadDays(cityList[msg.what]);
					/*
					 * Log.d("cityList", cityList[msg.what]);
					 * System.out.println("cityList? " + cityList[msg.what]);
					 * System.out.println("msg.what "+ msg.what);
					 */
				}
				int i = 0;
				String theName = null;
				for (oneDay day : listDays) {
					/*
					 * content[msg.what][i] = day.getCity_name() +
					 * day.getDay_id() + day.getDate() + day.getWeather();
					 */
					theName = day.getCity_name();
					content_weather[msg.what][i] = day.getWeather();
					content_date[msg.what][i] = day.getDate();
					content_temp[msg.what][i] = day.getTemp();
					content_wind[msg.what][i] = day.getWind();
					i++;
				}
				/*
				 * textViews[msg.what].setText(content[msg.what][0] + "\n" +
				 * content[msg.what][1] + "\n" + content[msg.what][2] + "\n" +
				 * content[msg.what][3]); textViews[msg.what].invalidate();
				 */
				tv_cityName[msg.what].setText(theName);
				tv_cityName[msg.what].setTypeface(face);
				tv_weather[msg.what].setText(content_weather[msg.what][0]
						+ " / " + content_wind[msg.what][0]);
				tv_date[msg.what][0].setText(content_date[msg.what][0]);
				tv_date[msg.what][1].setText(content_date[msg.what][1]);
				tv_date[msg.what][2].setText(content_date[msg.what][2]);
				tv_date[msg.what][3].setText(content_date[msg.what][3]);

				tv_temp[msg.what][0].setText(content_temp[msg.what][0]);
				tv_temp[msg.what][1].setText(content_temp[msg.what][1]);
				tv_temp[msg.what][2].setText(content_temp[msg.what][2]);
				tv_temp[msg.what][3].setText(content_temp[msg.what][3]);

				int i1 = getIcon.stringToIcon(content_weather[msg.what][1]);
				int i2 = getIcon.stringToIcon(content_weather[msg.what][2]);
				int i3 = getIcon.stringToIcon(content_weather[msg.what][3]);
				iv_weather[msg.what][0].setImageResource(i1);
				iv_weather[msg.what][1].setImageResource(i2);
				iv_weather[msg.what][2].setImageResource(i3);

				final int m = msg.what;
				iv_weather[msg.what][0]
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								Toast.makeText(getApplicationContext(),
										content_weather[m][1] + "", 1).show();

							}
						});
				iv_weather[msg.what][1]
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								Toast.makeText(getApplicationContext(),
										content_weather[m][2] + "", 1).show();

							}
						});
				iv_weather[msg.what][2]
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								Toast.makeText(getApplicationContext(),
										content_weather[m][3] + "", 1).show();

							}
						});

				listDays.clear();

			}
			if (isConnected()) {
				updateTime = da + "日" + ho + ":" + mi + ":" + se + "更新";
				Toast.makeText(getApplicationContext(), "更新成功！", 1).show();
			}

			new Handler().postDelayed(new Runnable() {

				public void run() {
					if (refreshButton.getAnimation() != null) {
						refreshButton.clearAnimation();
					}

				}
			}, 2000);

		};
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		instance = this;

		Intent intent = getIntent();
		flag = intent.getBooleanExtra("flag", true);
		citys = intent.getStringArrayListExtra("citys");
		cityList = intent.getStringArrayExtra("cityList");
		cityCount = intent.getIntExtra("cityCount", 1);
		count = cityCount;
		// System.out.println("show.count?" + count);

		LayoutInflater inflater = getLayoutInflater();
		initPageViews(inflater, cityCount);

		iweatherdb = iWeatherDB.getInstance(this);

		new Thread() {
			public void run() {
				super.run();
				if (flag) {
					handler.sendEmptyMessage(0);
				} else {
					for (int i = 0; i < citys.size(); i++) {
						handler.sendEmptyMessage(i);
					}
				}

			};
		}.start();

		addButton = (Button) findViewById(R.id.add);
		addButton.setOnClickListener(this);

		refreshButton = (Button) findViewById(R.id.refresh);
		refreshButton.setOnClickListener(this);

		pager = (ViewPager) findViewById(R.id.viewpager);
		if (flag) {
			initListViews(pageViews[0]);
		} else {
			for (int i = 0; i < citys.size(); i++) {
				initListViews(pageViews[i]);
			}
		}
		adapter = new MyPageAdapter(listViews);

		pager.setAdapter(adapter);

		Intent okService = new Intent(this, AutoUpdateService.class);
		startService(okService);

	}

	public void initPageViews(LayoutInflater inflater, int cityCount) {
		if (cityCount == 0) {
			pageViews[0] = inflater
					.inflate(R.layout.activity_showweather, null);
			/*
			 * textViews[0] = (TextView) pageViews[0].findViewById(
			 * R.id.showweather1).findViewById(R.id.tv_show);
			 */

			tv_cityName[0] = (TextView) pageViews[0].findViewById(
					R.id.showweather1).findViewById(R.id.tv_cityName);
			tv_weather[0] = (TextView) pageViews[0].findViewById(
					R.id.showweather1).findViewById(R.id.tv_weather);

			tv_date[0][0] = (TextView) pageViews[0].findViewById(
					R.id.showweather1).findViewById(R.id.tv_date0);
			tv_date[0][1] = (TextView) pageViews[0].findViewById(
					R.id.showweather1).findViewById(R.id.tv_date1);
			tv_date[0][2] = (TextView) pageViews[0].findViewById(
					R.id.showweather1).findViewById(R.id.tv_date2);
			tv_date[0][3] = (TextView) pageViews[0].findViewById(
					R.id.showweather1).findViewById(R.id.tv_date3);

			tv_temp[0][0] = (TextView) pageViews[0].findViewById(
					R.id.showweather1).findViewById(R.id.tv_temp0);
			tv_temp[0][1] = (TextView) pageViews[0].findViewById(
					R.id.showweather1).findViewById(R.id.tv_temp1);
			tv_temp[0][2] = (TextView) pageViews[0].findViewById(
					R.id.showweather1).findViewById(R.id.tv_temp2);
			tv_temp[0][3] = (TextView) pageViews[0].findViewById(
					R.id.showweather1).findViewById(R.id.tv_temp3);

			iv_weather[0][0] = (ImageView) pageViews[0].findViewById(
					R.id.showweather1).findViewById(R.id.iv_weather1);
			iv_weather[0][1] = (ImageView) pageViews[0].findViewById(
					R.id.showweather1).findViewById(R.id.iv_weather2);
			iv_weather[0][2] = (ImageView) pageViews[0].findViewById(
					R.id.showweather1).findViewById(R.id.iv_weather3);
		}
		for (int i = 0; i < cityCount; i++) {
			initPageView(inflater, i);
		}
	}

	public void initPageView(LayoutInflater inflater, int i) {
		pageViews[i] = inflater.inflate(showweather_layout[i], null);
		/*
		 * textViews[i] = (TextView)
		 * pageViews[i].findViewById(showweather_id[i])
		 * .findViewById(R.id.tv_show);
		 */
		tv_cityName[i] = (TextView) pageViews[i]
				.findViewById(showweather_id[i]).findViewById(R.id.tv_cityName);
		tv_weather[i] = (TextView) pageViews[i].findViewById(showweather_id[i])
				.findViewById(R.id.tv_weather);

		tv_date[i][0] = (TextView) pageViews[i].findViewById(showweather_id[i])
				.findViewById(R.id.tv_date0);
		tv_date[i][1] = (TextView) pageViews[i].findViewById(showweather_id[i])
				.findViewById(R.id.tv_date1);
		tv_date[i][2] = (TextView) pageViews[i].findViewById(showweather_id[i])
				.findViewById(R.id.tv_date2);
		tv_date[i][3] = (TextView) pageViews[i].findViewById(showweather_id[i])
				.findViewById(R.id.tv_date3);

		tv_temp[i][0] = (TextView) pageViews[i].findViewById(showweather_id[i])
				.findViewById(R.id.tv_temp0);
		tv_temp[i][1] = (TextView) pageViews[i].findViewById(showweather_id[i])
				.findViewById(R.id.tv_temp1);
		tv_temp[i][2] = (TextView) pageViews[i].findViewById(showweather_id[i])
				.findViewById(R.id.tv_temp2);
		tv_temp[i][3] = (TextView) pageViews[i].findViewById(showweather_id[i])
				.findViewById(R.id.tv_temp3);

		iv_weather[i][0] = (ImageView) pageViews[i].findViewById(
				showweather_id[i]).findViewById(R.id.iv_weather1);
		iv_weather[i][1] = (ImageView) pageViews[i].findViewById(
				showweather_id[i]).findViewById(R.id.iv_weather2);
		iv_weather[i][2] = (ImageView) pageViews[i].findViewById(
				showweather_id[i]).findViewById(R.id.iv_weather3);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case 1:
			count++;
			LayoutInflater inflater = getLayoutInflater();
			initPageView(inflater, count - 1);
			cityList = data.getStringArrayExtra("cityList");
			new Thread() {
				public void run() {
					super.run();
					handler.sendEmptyMessage(count - 1);
				};
			}.start();
			initListViews(pageViews[count - 1]);
			adapter.setListViews(listViews);
			adapter.notifyDataSetChanged();
			break;

		case 2:
			if (count > 1) {
				int deleteIndex = data.getIntExtra("deleteIndex", 0);
				adapter.destroyItem(pager, deleteIndex, null);
				listViews.remove(deleteIndex);

				adapter.setListViews(listViews);
				// count--;
				adapter.notifyDataSetChanged();

				Log.d("ListSize", listViews.size() + "," + adapter.size + ","
						+ pager.getChildCount());

			}
			break;
		}
	}

	/**
	 * listViews���view����
	 * 
	 * @param count
	 */
	private void initListViews(View view) {
		if (listViews == null)
			listViews = new ArrayList<View>();
		listViews.add(view);
	}

	/**
	 * 
	 * 
	 */
	class MyPageAdapter extends PagerAdapter {

		private ArrayList<View> listViews;// content

		private int size;

		public MyPageAdapter(ArrayList<View> listViews) {

			this.listViews = listViews;
			size = listViews == null ? 0 : listViews.size();
		}

		public void setListViews(ArrayList<View> listViews) {
			this.listViews = listViews;
			size = listViews == null ? 0 : listViews.size();
		}

		@Override
		public int getItemPosition(Object object) {
			// TODO Auto-generated method stub
			return PagerAdapter.POSITION_NONE;
		}

		public int getCount() {
			return size;
		}

		@Override
		public void destroyItem(View view, int position, Object arg2) {
			if (position < size)
				((ViewPager) view).removeView(listViews.get(position));
		}

		@Override
		public void finishUpdate(View arg0) {
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			try {
				((ViewPager) arg0).addView(listViews.get(arg1 % size), 0);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return listViews.get(arg1 % size);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.add:
			cityNow = new ArrayList<City>();
			count = iweatherdb.findAllCity().size();
			for (int i = 0; i < count; i++) {
				int n = 4;
				listDays = iweatherdb.loadDays(cityList[i]);
				for (oneDay day : listDays) {
					// City city = new City(day.getCity_name(), day.getTemp());
					// cityNow.add(city);
					if (n % 4 == 0) {
						City city = new City(day.getCity_name(), day.getTemp());
						cityNow.add(city);
					}
					n++;
				}
				listDays.clear();
			}
			Intent intent = new Intent(ShowWeatherActivity.this,
					ListOfCityActivity.class);
			intent.putExtra("count", count);
			intent.putExtra("cityNow", cityNow);
			intent.putExtra("updateTime", updateTime);

			startActivityForResult(intent, 10);
			break;
		case R.id.refresh:
			if (isConnected()) {
				Animation operatingAnim = AnimationUtils.loadAnimation(this,
						R.anim.tip);
				operatingAnim.setFillAfter(true);
				LinearInterpolator lin = new LinearInterpolator();
				operatingAnim.setInterpolator(lin);
				if (operatingAnim != null) {
					refreshButton.startAnimation(operatingAnim);
				}
				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						if (cityList.length > 1) {
							for (int i = 0; i < cityList.length; i++) {
								parseData = new ParseData(cityList[i],
										iweatherdb);
							}
							handler.sendEmptyMessage(9);
						} else {
							parseData = new ParseData("宝鸡", iweatherdb);
							handler.sendEmptyMessage(0);
						}
					}
				}).start();
			} else {
				Toast.makeText(this, "无法刷新，检查网络连接！", 1).show();
			}
			break;
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
