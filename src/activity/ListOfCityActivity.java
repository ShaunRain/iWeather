package activity;

import java.util.ArrayList;

import model.City;

import swipemenulistview.SwipeMenu;
import swipemenulistview.SwipeMenuCreator;
import swipemenulistview.SwipeMenuItem;
import swipemenulistview.SwipeMenuListView;
import swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import swipemenulistview.SwipeMenuListView.OnSwipeListener;
import util.ParseData;

import com.rain.iweather.R;

import db.iWeatherDB;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;

public class ListOfCityActivity extends Activity implements OnClickListener {

	private itemAdapter mAdapter;
	private SwipeMenuListView mListView;

	private ParseData parseData;
	private iWeatherDB iweatherdb;

	private Button chooseButton;
	private Button confirmButton;
	private TextView updateText;

	private ArrayList<City> mitemList = new ArrayList<>();
	private String[] cityList;

	private int count;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_citylist);

		updateText = (TextView) findViewById(R.id.update_time);

		Intent intent = getIntent();
		String updateTime = intent.getStringExtra("updateTime");
		updateText.setText(updateTime);
		count = intent.getIntExtra("count", 1);
		ArrayList<City> citys = (ArrayList<City>) intent
				.getSerializableExtra("cityNow");
		mitemList = citys;

		iweatherdb = iWeatherDB.getInstance(this);

		mListView = (SwipeMenuListView) findViewById(R.id.swip_list_city);
		mAdapter = new itemAdapter();
		mListView.setAdapter(mAdapter);

		// step 1. create a MenuCreator
		SwipeMenuCreator creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {
				// create "open" item
				SwipeMenuItem openItem = new SwipeMenuItem(
						getApplicationContext());
				// set item background
				openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
						0xCE)));
				// set item width
				openItem.setWidth(dp2px(90));
				// set item title
				openItem.setTitle("Open");
				// set item title fontsize
				openItem.setTitleSize(18);
				// set item title font color
				openItem.setTitleColor(Color.WHITE);
				// add to menu
				menu.addMenuItem(openItem);

				// create "delete" item
				SwipeMenuItem deleteItem = new SwipeMenuItem(
						getApplicationContext());
				// set item background
				deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
						0x3F, 0x25)));
				// set item width
				deleteItem.setWidth(dp2px(90));
				// set a icon
				deleteItem.setIcon(R.drawable.ic_delete);
				// add to menu
				menu.addMenuItem(deleteItem);
			}
		};
		// set creator
		mListView.setMenuCreator(creator);

		// step 2. listener item click event
		mListView.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(int position, SwipeMenu menu,
					int index) {
				City item = mitemList.get(position);
				switch (index) {
				case 0:
					// open
					open(item);
					break;
				case 1:
					// delete
					// delete(item);
					if (mitemList.size() > 1) {
						mitemList.remove(position);
						mAdapter.notifyDataSetChanged();
						delete(item, position);
					}
					break;
				}
				return false;
			}
		});

		// set SwipeListener
		mListView.setOnSwipeListener(new OnSwipeListener() {

			@Override
			public void onSwipeStart(int position) {
				// swipe start
			}

			@Override
			public void onSwipeEnd(int position) {
				// swipe end
			}
		});

		// other setting
		// listView.setCloseInterpolator(new BounceInterpolator());

		// test item long click
		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(getApplicationContext(),
						position + " long click", 0).show();
				return false;
			}
		});

		chooseButton = (Button) findViewById(R.id.bt_choose);
		chooseButton.setOnClickListener(this);
		confirmButton = (Button) findViewById(R.id.bt_confirm);
		confirmButton.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.bt_choose:
			if (mitemList.size() == 4) {
				Toast.makeText(getApplicationContext(), "到达添加城市上限!", 1).show();
			} else {
				Intent intent2 = new Intent(ListOfCityActivity.this,
						AddCityActivity.class);
				startActivityForResult(intent2, 20);
			}

			break;
		case R.id.bt_confirm:
			confirmList();
			break;
		}

	}

	public void confirmList() {
		ArrayList<String> citys = iweatherdb.findAllCity();

		if (citys.size() != count) {
			cityList = (String[]) citys.toArray();
			Intent intent3 = new Intent(ListOfCityActivity.this,
					ShowWeatherActivity.class);
			intent3.putExtra("cityList", cityList);
			intent3.putExtra("flag_add", false);
			setResult(1, intent3);
			finish();
		} else {
			finish();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case 1:
			String cityName = data.getStringExtra("cityName");
			mitemList.add((City) data.getSerializableExtra("acity"));
			mAdapter.notifyDataSetChanged();
			Toast.makeText(this, "cityName" + cityName, 1).show();
			break;
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub

	}

	/*--------------------------------------------------------------------*/
	private void delete(final City item, int position) {
		iweatherdb.deleteCity(item.getCityName());
		Intent intent = new Intent(ListOfCityActivity.this,
				ShowWeatherActivity.class);
		intent.putExtra("deleteIndex", position);
		Log.d("position?", position + "");
		setResult(2, intent);
		finish();

	}

	private void open(City item) {
		confirmList();

	}

	class itemAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mitemList.size();
		}

		@Override
		public City getItem(int position) {
			return mitemList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = View.inflate(getApplicationContext(),
						R.layout.item_list_app, null);
				new ViewHolder(convertView);
			}
			ViewHolder holder = (ViewHolder) convertView.getTag();
			City item = getItem(position);
			holder.cityName.setText(item.getCityName());
			holder.tempNow.setText(item.getTemp());
			return convertView;
		}

		class ViewHolder {
			TextView cityName;
			TextView tempNow;

			public ViewHolder(View view) {
				cityName = (TextView) view.findViewById(R.id.listitem_city);
				tempNow = (TextView) view.findViewById(R.id.listitem_temp);
				view.setTag(this);
			}
		}
	}

	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getResources().getDisplayMetrics());
	}

}
