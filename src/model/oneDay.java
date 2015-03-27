package model;

import java.io.Serializable;

public class oneDay implements Serializable{

	private String city_name;
	private String date;
	private int day_id;
	private String weather;
	private String wind;
	private String temp;

	public oneDay() {

	}

	public oneDay(String city_name, String date, int day_id, String weather,
			String wind, String temp) {
		super();
		this.city_name = city_name;
		this.date = date;
		this.day_id = day_id;
		this.weather = weather;
		this.wind = wind;
		this.temp = temp;
	}

	public String getCity_name() {
		return city_name;
	}

	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getDay_id() {
		return day_id;
	}

	public void setDay_id(int day_id) {
		this.day_id = day_id;
	}

	public String getWeather() {
		return weather;
	}

	public void setWeather(String weather) {
		this.weather = weather;
	}

	public String getWind() {
		return wind;
	}

	public void setWind(String wind) {
		this.wind = wind;
	}

	public String getTemp() {
		return temp;
	}

	public void setTemp(String temp) {
		this.temp = temp;
	}

}
