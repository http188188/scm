package com.sina.scm.data.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateUtils {
	public static Date getMaxDateOfDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		return calendar.getTime();
	}
	
	public static Date getMinDateOfDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTime();
	}
	
	public static Date getAfterDate(Date date) {
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int day = calendar.get(Calendar.DATE);
		calendar.set(Calendar.DATE, day+1);
		
		return calendar.getTime();		
	}
	
	public static Date[] getWorkDate(Date beginDate, Date endDate) {
		List<Date> dates = new ArrayList<Date>();
		if (beginDate == null || endDate == null) {
			return new Date[0];
		}
		Calendar calendar = Calendar.getInstance();
		while (!endDate.before(beginDate)) {
			calendar.setTime(endDate);
			int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
			if (!(dayOfWeek == Calendar.SUNDAY || dayOfWeek == Calendar.SATURDAY)) {
				dates.add(endDate);
			}
			calendar.add(Calendar.DAY_OF_MONTH, -1);
			endDate = calendar.getTime();
		}
		Date[] dateInArray = new Date[dates.size()];
		for (int i = 0; i < dates.size(); i++) {
			dateInArray[i] = dates.get(i);
		}
		return dateInArray;
	}
	
	public static Date[] getAllDate(Date beginDate, Date endDate) {
		List<Date> dates = new ArrayList<Date>();
		if (beginDate == null || endDate == null) {
			return new Date[0];
		}
		Calendar calendar = Calendar.getInstance();
		while (!endDate.before(beginDate)) {
			dates.add(endDate);
			calendar.setTime(endDate);
			calendar.add(Calendar.DAY_OF_MONTH, -1);
			endDate = calendar.getTime();
		}
		
		Date[] dateInArray = dates.toArray(new Date[dates.size()]);
		return dateInArray;
	}
	
	public static String formatDate(Date date, String format) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(date);
	}
	
	public static Date parseDate(String dateInString) {
		try {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date = dateFormat.parse(dateInString);
			return date;
		} catch (Exception ex) {
			return new Date();
		}
	}
	
	public static Date getRelativeDate(int days) {
		try {
			Date now = new Date();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(now);
			calendar.add(Calendar.DAY_OF_MONTH, days);
			Date date = calendar.getTime();
			return date;
		} catch (Exception ex) {
			return new Date();
		}
	}
	
	
	public static Date getRelativeWorkDate(int days) {
		Date now = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);
		int i = 0;
		while (i < days) {
			calendar.add(Calendar.DAY_OF_MONTH, -1);
			int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
			if (dayOfWeek == Calendar.SUNDAY || dayOfWeek == Calendar.SATURDAY) {
				continue;
			} else {
				i++;
			}
		}
		Date date = calendar.getTime();
		return date;
	}
	
	public static String getJobBeginDate(String date){
		
		String ymd = date.split("T")[0];
		String hms = date.split("T")[1].split("\\.")[0];
		return ymd + " " +hms;
		
		
	}
}
