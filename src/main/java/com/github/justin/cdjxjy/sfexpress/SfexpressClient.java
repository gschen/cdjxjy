package com.github.justin.cdjxjy.sfexpress;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import com.github.justin.cdjxjy.sfexpress.bean.Address;
import com.github.justin.cdjxjy.sfexpress.bean.ServiceTime;
import com.github.justin.cdjxjy.sfexpress.utils.AreaListUtils;
import com.github.justin.cdjxjy.sfexpress.utils.CityListUtils;
import com.github.justin.cdjxjy.sfexpress.utils.CountryListUtils;
import com.github.justin.cdjxjy.sfexpress.utils.DBUtils;
import com.github.justin.cdjxjy.sfexpress.utils.HttpClientUtils;
import com.github.justin.cdjxjy.sfexpress.utils.PropUtils;
import com.github.justin.cdjxjy.sfexpress.utils.ServiceTimeUtils;
import com.github.justin.cdjxjy.sfexpress.utils.SrcProvinceListUtils;

public class SfexpressClient {
	private static final Logger log = Logger.getLogger(SfexpressClient.class);
	private List<String> provinceList;
	private List<String> cityList;
	private List<String> areaList;
	private List<String> countryList;

	private Date startTime;
	private Date endTime;

	private void getSrcProvince() {
		provinceList = SrcProvinceListUtils.getSrcProvinceList();
		for (String ss : provinceList) {
			DBUtils.saveProvince(ss);
		}

	}

	private void getCity() {

		for (String p : provinceList) {
			List<String> cl = CityListUtils.getCityList(p);
			cityList.addAll(cl);
			for (String ss : cl) {
				DBUtils.saveCity(ss);
			}
		}
	}

	private void getArea() {
		for (String s : cityList) {
			List<String> list = AreaListUtils.getAreaList(s.split(" - ")[0],
					s.split(" - ")[1]);
			areaList.addAll(list);
			for (String ss : list) {
				DBUtils.saveArea(ss);
			}

		}
	}

	private void getCountry() {
		for (String s : areaList) {
			List<String> list = CountryListUtils.getCountryList(
					s.split(" - ")[0], s.split(" - ")[1], s.split(" - ")[2]);
			countryList.addAll(list);
			for (String ss : list) {
				DBUtils.saveCountry(ss);
			}
		}
	}

	// private void queryServiceTime1() {
	// for (String src : countryList)
	// for (String dst : countryList) {
	// Address srcAddr = new Address();
	// Address dstAddr = new Address();
	// srcAddr.setProvince(src.split(" - ")[0]);
	// srcAddr.setProvinceNo(provinceList.indexOf(src.split(" - ")[0]) + 1);
	// srcAddr.setCity(src.split(" - ")[1]);
	// srcAddr.setCityNo(cityList.indexOf(src.split(" - ")[0] + " - "
	// + src.split(" - ")[1]) + 1);
	// srcAddr.setArea(src.split(" - ")[2]);
	// srcAddr.setAreaNo(areaList.indexOf(src.split(" - ")[0] + " - "
	// + src.split(" - ")[1] + " - " + src.split(" - ")[2]) + 1);
	//
	// dstAddr.setProvince(dst.split(" - ")[0]);
	// dstAddr.setProvinceNo(provinceList.indexOf(dst.split(" - ")[0]) + 1);
	// dstAddr.setCity(dst.split(" - ")[1]);
	// dstAddr.setCityNo(cityList.indexOf(dst.split(" - ")[0] + " - "
	// + dst.split(" - ")[1]) + 1);
	// dstAddr.setArea(dst.split(" - ")[2]);
	// dstAddr.setAreaNo(areaList.indexOf(dst.split(" - ")[0] + " - "
	// + dst.split(" - ")[1] + " - " + dst.split(" - ")[2]) + 1);
	//
	// if (src.split(" - ").length == 3) {
	// srcAddr.setCountry("");
	// srcAddr.setCountryNo(-1);
	// } else {
	// srcAddr.setCountry(src.split(" - ")[3]);
	// srcAddr.setCountryNo(countryList.indexOf(src));
	// }
	//
	// if (dst.split(" - ").length == 3) {
	// dstAddr.setCountry("");
	// dstAddr.setCountryNo(-1);
	// } else {
	// dstAddr.setCountry(dst.split(" - ")[3]);
	// dstAddr.setCountryNo(countryList.indexOf(dst));
	// }
	// ServiceTimeUtils.getServiceTime(srcAddr, dstAddr);
	// DBUtils.saveServiceTime(ServiceTimeUtils.getServiceTime(
	// srcAddr, dstAddr));
	// }
	// }

	private void queryServiceTime() {
		for (String src : countryList) {
			Address srcAddr = new Address(src);
			srcAddr.setProvinceNo(provinceList);
			srcAddr.setCityNo(cityList);
			srcAddr.setAreaNo(areaList);
			srcAddr.setCountryNo(countryList);
			for (String dst : countryList) {
				Address dstAddr = new Address(dst);
				dstAddr.setProvinceNo(provinceList);
				dstAddr.setCityNo(cityList);
				dstAddr.setAreaNo(areaList);
				dstAddr.setCountryNo(countryList);

				ServiceTime st = ServiceTimeUtils.getServiceTime(srcAddr,
						dstAddr);
				DBUtils.saveServiceTime(st);
			}
		}

	}

	private void execute() {
		init();
		getAddressInfo();
		queryServiceTime();
		shutdown();
	}

	private void getAddressInfo() {
		getSrcProvince();
		getCity();
		getArea();
		getCountry();
	}

	private void shutdown() {
		DBUtils.close();
		endTime = new Date(System.currentTimeMillis());

		log.info("complete all tasks!");
		log.info("--------------------------METRICS INFO--------------------------");
		HttpClientUtils.printMetrics();
		log.info("Program starts time: " + startTime);
		log.info("Program ends time: " + endTime);
		log.info("Program runs time: " + getRunTime() + " h");
	}

	private double getRunTime() {
		double runTime = 0.0;
		DecimalFormat dclFmt = new DecimalFormat("0.00 ");
		double diff = endTime.getTime() - startTime.getTime();
		runTime = diff / (1000 * 60 * 60);
		runTime = Double.parseDouble(dclFmt.format(runTime));
		return runTime;
	}

	private void init() {
		startTime = new Date(System.currentTimeMillis());
		DOMConfigurator.configure(this.getClass().getClassLoader()
				.getResource("log4j.xml"));
		DBUtils.createDb(PropUtils.getValue("dbPath").replaceAll("\\\\",
				"\\\\\\\\"));
		DBUtils.connect();
		DBUtils.createTable();
		cityList = new ArrayList<String>();
		areaList = new ArrayList<String>();
		countryList = new ArrayList<String>();
	}

	public static void main(String[] args) {
		new SfexpressClient().execute();
	}
}
