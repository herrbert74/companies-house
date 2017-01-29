package com.babestudios.companyinfouk.utils;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {
	public static Date parseDate(String date) {
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
		try {
			return df.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Date parseMySqlDate(String date) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		try {
			return df.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static long convertToTimestamp(Date date) {
		return date.getTime() / 1000;
	}

	public static String formatLongDateFromTimeStampMillis(long timeStamp) {
		Date date = new Date(timeStamp);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH);
		return df.format(date);
	}

	public static String formatShortDateFromTimeStampMillis(long timeStamp) {
		Date date = new Date(timeStamp);
		DateFormat df = new SimpleDateFormat("d MMM yyyy", Locale.ENGLISH);
		return df.format(date);
	}

	public static String formatMySqlDateFromTimeStampMillis(long timeStamp) {
		Date date = new Date(timeStamp);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		return df.format(date);
	}

	public static boolean isUkBusinessDay(Calendar cal) {
		// check if weekend
		if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
			return false;
		}

		// check if New Year's Day
		if (cal.get(Calendar.MONTH) == Calendar.JANUARY
				&& cal.get(Calendar.DAY_OF_MONTH) == 1) {
			return false;
		}

		// check if Good Friday
		Date easterSundayDate = getEasterSundayDate(cal.get(Calendar.YEAR));
		Calendar calEasterFriday = Calendar.getInstance();
		calEasterFriday.setTime(easterSundayDate);
		calEasterFriday.add(Calendar.DAY_OF_MONTH, -2);
		if (cal.get(Calendar.MONTH) == calEasterFriday.get(Calendar.MONTH)
				&& cal.get(Calendar.DAY_OF_MONTH) == calEasterFriday.get(Calendar.DAY_OF_MONTH)) {
			return false;
		}

		// check if Easter Monday
		Calendar calEasterMonday = Calendar.getInstance();
		calEasterMonday.setTime(easterSundayDate);
		calEasterMonday.add(Calendar.DAY_OF_MONTH, 1);
		if (cal.get(Calendar.MONTH) == calEasterMonday.get(Calendar.MONTH)
				&& cal.get(Calendar.DAY_OF_MONTH) == calEasterMonday.get(Calendar.DAY_OF_MONTH)) {
			return false;
		}

		// check Early May Bank Holiday (first Monday of May)
		if (cal.get(Calendar.MONTH) == Calendar.MAY
				&& cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY
				&& cal.get(Calendar.DAY_OF_MONTH) < 8) {
			return false;
		}

		// check Spring Bank Holiday (last Monday of May)
		if (cal.get(Calendar.MONTH) == Calendar.MAY
				&& cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY
				&& cal.get(Calendar.DAY_OF_MONTH) > (31 - 7)) {
			return false;
		}

		// check August Bank Holiday (last Monday of August)
		if (cal.get(Calendar.MONTH) == Calendar.AUGUST
				&& cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY
				&& cal.get(Calendar.DAY_OF_MONTH) > (31 - 7)) {
			return false;
		}

		// check if Christmas
		if (cal.get(Calendar.MONTH) == Calendar.DECEMBER) {
			//Christmas Day and Boxing Day
			if (cal.get(Calendar.DAY_OF_MONTH) == 25 || cal.get(Calendar.DAY_OF_MONTH) == 26) {
				return false;
			}

			//Christmas Day and Boxing Day
			if (cal.get(Calendar.DAY_OF_MONTH) == 25 || cal.get(Calendar.DAY_OF_MONTH) == 26) {
				return false;
			}

			//Christmas Day and Boxing Day substitute days
			if ((cal.get(Calendar.DAY_OF_MONTH) == 27 || cal.get(Calendar.DAY_OF_MONTH) == 28) && (cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY)) {
				return false;
			}
		}

		// IF NOTHING ELSE, IT'S A BUSINESS DAY
		return true;
	}

	public static Date getEasterSundayDate(int year) {
		int day, month;
		// Divide y by 19 and call the remainder a. Ignore the quotient.
		int a = year % 19;

		// Divide y by 100 to get a quotient b and a remainder c.
		int b = year / 100;
		int c = year % 100;

		// Divide b by 4 to get a quotient d and a remainder e.
		int d = b / 4;
		int e = b % 4;

		// Divide 8 * b + 13 by 25 to get a quotient g. Ignore the remainder.
		int g = (8 * b + 13) / 25;

		// Divide 19 * a + b - d - g + 15 by 30 to get a remainder h. Ignore the quotient.
		int h = (19 * a + b - d - g + 15) % 30;

		// Divide c by 4 to get a quotient j and a remainder k.
		int j = c / 4;
		int k = c % 4;

		// Divide a + 11 * h by 319 to get a quotient m. Ignore the remainder.
		int m = (a + 11 * h) / 319;

		// Divide 2 * e + 2 * j - k - h + m + 32 by 7 to get a remainder r. Ignore the quotient.
		int r = (2 * e + 2 * j - k - h + m + 32) % 7;

		// Divide h - m + r + 90 by 25 to get a quotient "month". Ignore the remainder.
		month = (h - m + r + 90) / 25;

		// Divide h - m + r + n + 19 by 32 to get a remainder "day".
		day = (h - m + r + month + 19) % 32;
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month - 1, day);

		return calendar.getTime();
	}
}
