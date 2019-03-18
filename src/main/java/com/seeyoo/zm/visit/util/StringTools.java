package com.seeyoo.zm.visit.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class StringTools {
	public static boolean isEmptyString(String str) {
		return str == null || str.trim().isEmpty();
	}

	public static String dateToString(Date date) {
		return dateToString(date, "yyyy-MM-dd");
	}

	public static String dateToString(Date date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
	
	public static Date stringToDate(String time) {
		return stringToDate(time, "yyyy-MM-dd");
	}

	public static Date stringToDate(String time, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date date = new Date();
		try {
			date = sdf.parse(time);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}
	
	public static String uuid() {
		String uuid = UUID.randomUUID().toString();
		return uuid.replace("-", "");
	}

	public static String timeStapm2Str(Timestamp timestamp){
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return  sdf.format(timestamp);
	}
}
