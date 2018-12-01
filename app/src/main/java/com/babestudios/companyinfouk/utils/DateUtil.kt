@file:Suppress("unused")

package com.babestudios.companyinfouk.utils


import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateUtil {
	fun parseDate(date: String): Date? {
		val df = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
		return try {
			df.parse(date)
		} catch (e: ParseException) {
			e.printStackTrace()
			null
		}

	}

	fun parseMySqlDate(date: String): Date? {
		val df = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
		return try {
			df.parse(date)
		} catch (e: ParseException) {
			e.printStackTrace()
			null
		}
	}

	fun convertToTimestamp(date: Date): Long {
		return date.time / 1000
	}

	fun formatLongDateFromTimeStampMillis(timeStamp: Long): String {
		val date = Date(timeStamp)
		val df = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH)
		return df.format(date)
	}

	fun formatShortDateFromTimeStampMillis(timeStamp: Long): String {
		val date = Date(timeStamp)
		val df = SimpleDateFormat("d MMM yyyy", Locale.ENGLISH)
		return df.format(date)
	}

	fun formatMySqlDateFromTimeStampMillis(timeStamp: Long): String {
		val date = Date(timeStamp)
		val df = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
		return df.format(date)
	}

	fun isUkBusinessDay(cal: Calendar): Boolean {
		// check if weekend
		if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
			return false
		}

		// check if New Year's Day
		if (cal.get(Calendar.MONTH) == Calendar.JANUARY && cal.get(Calendar.DAY_OF_MONTH) == 1) {
			return false
		}

		// check if Good Friday
		val easterSundayDate = getEasterSundayDate(cal.get(Calendar.YEAR))
		val calEasterFriday = Calendar.getInstance()
		calEasterFriday.time = easterSundayDate
		calEasterFriday.add(Calendar.DAY_OF_MONTH, -2)
		if (cal.get(Calendar.MONTH) == calEasterFriday.get(Calendar.MONTH) && cal.get(Calendar.DAY_OF_MONTH) == calEasterFriday.get(Calendar.DAY_OF_MONTH)) {
			return false
		}

		// check if Easter Monday
		val calEasterMonday = Calendar.getInstance()
		calEasterMonday.time = easterSundayDate
		calEasterMonday.add(Calendar.DAY_OF_MONTH, 1)
		if (cal.get(Calendar.MONTH) == calEasterMonday.get(Calendar.MONTH) && cal.get(Calendar.DAY_OF_MONTH) == calEasterMonday.get(Calendar.DAY_OF_MONTH)) {
			return false
		}

		// check Early May Bank Holiday (first Monday of May)
		if (cal.get(Calendar.MONTH) == Calendar.MAY
				&& cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY
				&& cal.get(Calendar.DAY_OF_MONTH) < 8) {
			return false
		}

		// check Spring Bank Holiday (last Monday of May)
		if (cal.get(Calendar.MONTH) == Calendar.MAY
				&& cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY
				&& cal.get(Calendar.DAY_OF_MONTH) > 31 - 7) {
			return false
		}

		// check August Bank Holiday (last Monday of August)
		if (cal.get(Calendar.MONTH) == Calendar.AUGUST
				&& cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY
				&& cal.get(Calendar.DAY_OF_MONTH) > 31 - 7) {
			return false
		}

		// check if Christmas
		if (cal.get(Calendar.MONTH) == Calendar.DECEMBER) {
			//Christmas Day and Boxing Day
			if (cal.get(Calendar.DAY_OF_MONTH) == 25 || cal.get(Calendar.DAY_OF_MONTH) == 26) {
				return false
			}

			//Christmas Day and Boxing Day
			if (cal.get(Calendar.DAY_OF_MONTH) == 25 || cal.get(Calendar.DAY_OF_MONTH) == 26) {
				return false
			}

			//Christmas Day and Boxing Day substitute days
			if ((cal.get(Calendar.DAY_OF_MONTH) == 27 || cal.get(Calendar.DAY_OF_MONTH) == 28) && (cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY)) {
				return false
			}
		}

		// IF NOTHING ELSE, IT'S A BUSINESS DAY
		return true
	}

	private fun getEasterSundayDate(year: Int): Date {
		val day: Int
		val month: Int
		// Divide y by 19 and call the remainder a. Ignore the quotient.
		val a = year % 19

		// Divide y by 100 to get a quotient b and a remainder c.
		val b = year / 100
		val c = year % 100

		// Divide b by 4 to get a quotient d and a remainder e.
		val d = b / 4
		val e = b % 4

		// Divide 8 * b + 13 by 25 to get a quotient g. Ignore the remainder.
		val g = (8 * b + 13) / 25

		// Divide 19 * a + b - d - g + 15 by 30 to get a remainder h. Ignore the quotient.
		val h = (19 * a + b - d - g + 15) % 30

		// Divide c by 4 to get a quotient j and a remainder k.
		val j = c / 4
		val k = c % 4

		// Divide a + 11 * h by 319 to get a quotient m. Ignore the remainder.
		val m = (a + 11 * h) / 319

		// Divide 2 * e + 2 * j - k - h + m + 32 by 7 to get a remainder r. Ignore the quotient.
		val r = (2 * e + 2 * j - k - h + m + 32) % 7

		// Divide h - m + r + 90 by 25 to get a quotient "month". Ignore the remainder.
		month = (h - m + r + 90) / 25

		// Divide h - m + r + n + 19 by 32 to get a remainder "day".
		day = (h - m + r + month + 19) % 32
		val calendar = Calendar.getInstance()
		calendar.set(year, month - 1, day)

		return calendar.time
	}
}
