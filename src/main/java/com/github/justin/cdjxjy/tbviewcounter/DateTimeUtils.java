package com.github.justin.cdjxjy.tbviewcounter;

import java.text.SimpleDateFormat;

public class DateTimeUtils {

	public static String getCurrentDateTimeStr() {
		SimpleDateFormat tempDate = new SimpleDateFormat("yyyyMMdd");
		String datetime = tempDate.format(new java.util.Date());
		return datetime;
	}

	public static String getRandomDateTimeStr() {

		String dateTime = "2013" + generateMonth() + generateDay();

		return dateTime;
	}

	public static String getNumStr(int start, int end) {
		String numStr = null;
		long num = 0;

		num = Math.round(Math.random() * (end - start) + start);

		if (num < 10)
			numStr = "0" + num;
		else
			numStr = num + "";
		return numStr;
	}

	public static String generateMonth() {

		return getNumStr(1, 12);
	}

	public static String generateDay() {
		return getNumStr(1, 30);
	}
}
