package model;

import java.io.Serializable;

public class City implements Serializable{
	private String cityName;
	private String Temp;

	public City(String cityName, String temp) {
		super();
		this.cityName = cityName;
		Temp = temp;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getTemp() {
		return Temp;
	}

	public void setTemp(String temp) {
		Temp = temp;
	}

}
