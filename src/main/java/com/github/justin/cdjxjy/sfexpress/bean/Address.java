package com.github.justin.cdjxjy.sfexpress.bean;

import java.util.List;

public class Address {
	public boolean hasCountry = false;

	public Address() {

	}

	public void setProvinceNo(List<String> provinceList) {
		setProvinceNo(provinceList.indexOf(province) + 1);
	}

	public void setCityNo(List<String> cityList) {
		setCityNo(cityList.indexOf(getCityStr()) + 1);
	}

	public void setAreaNo(List<String> areaList) {
		setAreaNo(areaList.indexOf(getAreaStr()) + 1);

	}

	public void setCountryNo(List<String> countryList) {
		if (hasCountry) {
			setCountryNo(countryList.indexOf(getCountryStr()) + 1);
		}
	}

	// 根据"北京-北京市-昌平区-沙河地区"构造address对象实例
	public Address(String address) {
		int size = address.split(" - ").length;
		this.province = address.split(" - ")[0];
		this.city = address.split(" - ")[1];
		this.area = address.split(" - ")[2];
		if (size == 3) {
			this.country = "";
			this.hasCountry = false;
		} else {
			this.country = address.split(" - ")[3];
			this.hasCountry = true;
		}

	}

	@Override
	public String toString() {
		return province + " - " + city + " - " + area + " - " + country;
	}

	private String province;
	private String city;
	private String area;
	private String country;

	private int provinceNo = -1;
	private int cityNo = -1;
	private int areaNo = -1;
	private int countryNo = -1;

	public String getProvinceStr() {
		return province;
	}

	public String getCityStr() {
		return province + " - " + city;
	}

	public String getAreaStr() {
		return province + " - " + city + " - " + area;
	}

	public String getCountryStr() {
		return toString();
	}

	public int getProvinceNo() {
		return provinceNo;
	}

	public void setProvinceNo(int provinceNo) {
		this.provinceNo = provinceNo;
	}

	public int getCityNo() {
		return cityNo;
	}

	public void setCityNo(int cityNo) {
		this.cityNo = cityNo;
	}

	public int getAreaNo() {
		return areaNo;
	}

	public void setAreaNo(int areaNo) {
		this.areaNo = areaNo;
	}

	public int getCountryNo() {
		return countryNo;
	}

	public void setCountryNo(int countryNo) {
		this.countryNo = countryNo;
	}

	public Address(String province, String city, String area, String country) {
		this.province = province;
		this.city = city;
		this.area = area;
		this.country = country;
		if ("".equals(country)) {
			this.hasCountry = false;
		} else {
			this.hasCountry = true;
		}
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
}
