package com.hitao.codegen.util;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.apache.log4j.Logger;

/**
 * Date Util Library<br>
 * Utility methods to perform various date operations. At this point all public
 * methods are static and final.<br>
 * 
 * @author zhangjun.ht
 * @created 2010-11-10
 * @version $Id: DateUtils.java 12 2011-02-20 10:50:23Z guest $
 */
public final class DateUtils {
	private static final Logger LOG = Logger.getLogger(DateUtils.class);
	private static final boolean DEBUG_ENABLED = LOG.isDebugEnabled();

	private static final TimeZone TIME_ZONE_LOCAL = TimeZone.getDefault();

	/** the number of milliseconds in one hour */
	public static final BigDecimal MILLIS_PER_HOUR = BigDecimal
			.valueOf(3600000);

	public static final BigDecimal MILLIS_PER_DAY = BigDecimal
			.valueOf(86400000);

	private static ThreadLocal<Calendar> CAL = new ThreadLocal<Calendar>() {
		@Override
		protected synchronized Calendar initialValue() {
			return Calendar.getInstance();
		}
	};

	private static ThreadLocal<Calendar> CAL2 = new ThreadLocal<Calendar>() {
		@Override
		protected synchronized Calendar initialValue() {
			return Calendar.getInstance();
		}
	};

	// TIME_INT_FORMAT
	private static ThreadLocal<SimpleDateFormat> TIME_INT_FORMAT = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected synchronized SimpleDateFormat initialValue() {
			return new java.text.SimpleDateFormat("HHmmssSSS");
		}
	};

	// DATE_INT_FORMAT
	private static ThreadLocal<SimpleDateFormat> DATE_INT_FORMAT = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected synchronized SimpleDateFormat initialValue() {
			return new java.text.SimpleDateFormat("yyyyMMdd");
		}
	};

	// SIMPLE_DATE_FORMAT
	private static ThreadLocal<SimpleDateFormat> SIMPLE_DATE_FORMAT = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected synchronized SimpleDateFormat initialValue() {
			return new java.text.SimpleDateFormat("yyyy-MM-dd");
		}
	};

	// ISO_DATE_FORMAT
	private static ThreadLocal<SimpleDateFormat> ISO_DATE_FORMAT = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected synchronized SimpleDateFormat initialValue() {
			return new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		}
	};

	private static final long START_OF_DAY;
	private static final long END_OF_DAY;
	static {
		START_OF_DAY = clearTime(clearDate(0));
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.setTimeZone(TIME_ZONE_LOCAL);
		cal.setTimeInMillis(START_OF_DAY);
		cal.add(Calendar.DATE, 1);
		cal.add(Calendar.SECOND, -1);
		END_OF_DAY = cal.getTimeInMillis();
	}

	private DateUtils() {
		/* prevent instantiation */
	}

	/**
	 * All date creation within the application should take advantage of one of
	 * the <code>getNewDate</code> maker methods. This is responsible for
	 * creating a date compatible with timezone differences.
	 * 
	 * @return a new Date
	 */
	public static final Date getNewDate() {
		return new Date();
	}

	/**
	 * All date creation within the application should take advantage of one of
	 * the <code>getNewDate</code> maker methods. This is responsible for
	 * creating a date compatible with timezone differences.
	 * 
	 * @param argMillis
	 *            millisecond representation of a time
	 * @return a new Date
	 */
	public static final Date getNewDate(long argMillis) {
		return new Date(argMillis);
	}

	/**
	 * Given a year, and the day of that year, returns a Date
	 * 
	 * @param argYear
	 *            the year
	 * @param argDayOfYear
	 *            the day of the year
	 * @return the requested date (time will be midnight local time)
	 */
	public static final Date getJulianDate(int argYear, int argDayOfYear) {
		Calendar cal = CAL.get();

		cal.clear();
		cal.setTimeZone(TIME_ZONE_LOCAL);
		cal.set(Calendar.YEAR, argYear);
		cal.set(Calendar.DAY_OF_YEAR, argDayOfYear);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	/**
	 * Changes the time zone to local and removes the time portion of a date.
	 * 
	 * @param argDate
	 *            the date to be changed
	 * @return a new date with only the date portion (time will be midnight
	 *         local time)
	 * @throws NullPointerException
	 *             if <tt>argDate</tt> is null
	 */
	public static final Date clearTime(Date argDate) {
		return new Date(clearTime(argDate.getTime()));
	}

	/**
	 * Changes the time zone to local and removes the time portion of a date.
	 * 
	 * @param argDate
	 *            the date to be changed
	 * @return a new date with only the date portion (time will be midnight
	 *         local time)
	 */
	public static final long clearTime(long argDate) {
		Calendar cal = CAL.get();
		cal.clear();
		cal.setTimeInMillis(argDate);
		cal.setTimeZone(TIME_ZONE_LOCAL);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTimeInMillis();
	}

	/**
	 * Changes the time zone to local and removes the time portion of a date.
	 * 
	 * @param argDate
	 *            the date to be changed
	 * @return a new date with only the date portion (time will be midnight
	 *         local time)
	 */
	public static final Date clearTimeNearest(Date argDate) {
		return new Date(clearTimeNearest(argDate.getTime()));
	}

	/**
	 * Changes the time zone to local and removes the time portion of a date.
	 * 
	 * @param argDate
	 *            the date to be changed
	 * @return a new date with only the date portion (time will be midnight
	 *         local time)
	 */
	public static final long clearTimeNearest(long argDate) {
		Calendar cal = CAL.get();
		cal.clear();
		cal.setTimeInMillis(argDate);
		cal.setTimeZone(TIME_ZONE_LOCAL);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		if (hour >= 12) {
			int newDay = cal.get(Calendar.DAY_OF_YEAR) + 1;
			cal.set(Calendar.DAY_OF_YEAR, newDay);
		}
		return cal.getTimeInMillis();
	}

	/**
	 * Removes the time portion of a date. (Sets the date to January 1, 1970.)
	 * 
	 * @param argDate
	 *            the date to be changed
	 * @return a new date with no date component (date will be January 1, 1970.)
	 * @throws NullPointerException
	 *             if <tt>argDate</tt> is null
	 */
	public static final Date clearDate(Date argDate) {
		return new Date(clearDate(argDate.getTime()));
	}

	/**
	 * Removes the time portion of a date. (Sets the date to January 1, 1970.)
	 * 
	 * @param argDate
	 *            the date to be changed
	 * @return a new date with no date component (date will be January 1, 1970.)
	 */
	public static final long clearDate(long argDate) {
		Calendar cal = CAL.get();

		cal.clear();
		cal.setTimeZone(TIME_ZONE_LOCAL);
		cal.setTimeInMillis(argDate);
		cal.set(Calendar.MONTH, Calendar.JANUARY);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.YEAR, 1970);
		return cal.getTimeInMillis();
	}

	/**
	 * Converts a string into a date.
	 * 
	 * @param argDateString
	 *            a date string in the format "yyyy-mm-dd"
	 * @return the date represented by the string
	 */
	public static final Date parseDate(String argDateString) {
		try {
			return SIMPLE_DATE_FORMAT.get().parse(argDateString);
		} catch (Exception ex) {
			if (DEBUG_ENABLED) {
				LOG.debug("parseDate exception for argDateString: '"
						+ argDateString + "'", ex);
			}

			return null;
		}
	}

	/**
	 * Formats a date in "yyyy-MM-dd" format.
	 * 
	 * @param argDate
	 *            a date to format
	 * @return the formatted date string
	 */
	public static final String format(Date argDate) {
		return SIMPLE_DATE_FORMAT.get().format(argDate);
	}

	/**
	 * Converts a string in the format used for expiration dates found in credit
	 * card track data (YYMM) into a java date.
	 * 
	 * @param argMsrDateString
	 *            the date string to parse
	 * @return null if there is any problem parsing the date
	 */
	public static final Date parseMsrDate(String argMsrDateString) {
		try {
			int year = Integer.parseInt(argMsrDateString.substring(0, 2));
			int month = Integer.parseInt(argMsrDateString.substring(2, 4));
			return makeExpirationDate(year, month);
		} catch (Exception ex) {
			if (DEBUG_ENABLED) {
				LOG.debug("parseMsrDate exception for argMsrDateString: '"
						+ argMsrDateString + "'", ex);
			}

			return null;
		}
	}

	/**
	 * Converts a string in the format used for messages to other Datavantage
	 * processes and stored in the database (MMYY) into a java date.
	 * 
	 * @param argDateString
	 *            the date string to parse
	 * @return null if there is any problem parsing the date
	 */
	public static final Date parseDtvExpirationDate(String argDateString) {
		try {
			int month = Integer.parseInt(argDateString.substring(0, 2));
			int year = Integer.parseInt(argDateString.substring(2, 4));
			return makeExpirationDate(year, month);
		} catch (Exception ex) {
			if (DEBUG_ENABLED) {
				LOG.debug(
						"parseDtvExpirationDate exception for argDateString: '"
								+ argDateString + "'", ex);
			}

			return null;
		}
	}

	/**
	 * Creates an expiration date for the specified year and month.
	 * 
	 * @param argYear
	 *            year of expiration date
	 * @param argMonth
	 *            month of expiration date
	 * @return the date/time correspoding to the last millisecond of the
	 *         specified month and year, or <code>null</code> if
	 *         <code>argMonth</code> is &gt; 12
	 */
	public static final Date makeExpirationDate(int argYear, int argMonth) {
		if ((argMonth < 1) || (argMonth > 12)) {
			return null;
		}

		Calendar cal = CAL.get();

		cal.clear();
		cal.setTimeZone(TIME_ZONE_LOCAL);
		cal.set(Calendar.YEAR, argYear + 2000);
		cal.set(Calendar.MONTH, argMonth);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.add(Calendar.MILLISECOND, -1);
		return cal.getTime();
	}

	/**
	 * Adds the date from <code>argDate</code> to the time from
	 * <code>argTime</code>.
	 * 
	 * @param argDate
	 *            the date to add
	 * @param argTime
	 *            the time to add
	 * @return the resultant date/time
	 */
	public static final Date add(Date argDate, Date argTime) {
		Calendar cal = CAL.get();
		Calendar cal2 = CAL2.get();

		cal.clear();
		cal.setTime(argDate);
		cal2.clear();
		cal2.setTime(argTime);
		cal2.setTimeZone(TIME_ZONE_LOCAL);

		matchField(Calendar.HOUR, cal, cal2);
		matchField(Calendar.AM_PM, cal, cal2);
		matchField(Calendar.MINUTE, cal, cal2);
		matchField(Calendar.SECOND, cal, cal2);
		matchField(Calendar.MILLISECOND, cal, cal2);

		return cal.getTime();
	}

	private static final void matchField(int argField, Calendar toCalendar,
			Calendar fromCalendar) {
		toCalendar.set(argField, fromCalendar.get(argField));
	}

	/**
	 * Determines if two date times are in the same day.
	 * 
	 * @param argDateTime1
	 *            first date
	 * @param argDateTime2
	 *            second date
	 * @return true if the date times are in the same day
	 */
	public static final boolean isSameDay(Date argDateTime1, Date argDateTime2) {
		Date d1 = clearTime(argDateTime1);
		Date d2 = clearTime(argDateTime2);
		return 0 == d1.compareTo(d2);
	}

	/**
	 * Formats a date in ISO format, with no time portion.
	 * 
	 * @param argDate
	 *            a date string in the format "yyyy-mm-dd'T'HH:mm:ss"
	 * @return the string in the format "yyyy-MM-dd'T'HH:mm:ss", or
	 *         <code>null</code> if <code>argDate</code> is <code>null</code>
	 * @see #parseIso(String)
	 */
	public static final String formatIso(Date argDate) {
		if (argDate == null) {
			return null;
		}
		return ISO_DATE_FORMAT.get().format(clearTime(argDate));
	}

	/**
	 * Formats a date to ISO date/time standards. The ISO date format is
	 * yyyy-MM-ddTHH:mm:ss, so "December 31st, 2005 at 8 PM" would be formatted
	 * "2005-12-31T20:00:00".
	 * 
	 * @param argDate
	 *            the date to format
	 * @return the string respresentation of the date in ISO format
	 * @see #parseIso(String)
	 * @throws NullPointerException
	 *             if <code>argDate</code> is <code>null</code>
	 */
	public static final String formatIsoDateTime(Date argDate) {
		return ISO_DATE_FORMAT.get().format(argDate);
	}

	/**
	 * Converts a string into a date.
	 * 
	 * @param argDateString
	 *            a date string in the format "yyyy-MM-dd'T'HH:mm:ss"
	 * @return the date represented by the string
	 * @see #formatIso(java.util.Date)
	 */
	public static final Date parseIso(String argDateString) {
		try {
			return ISO_DATE_FORMAT.get().parse(argDateString);
		} catch (java.text.ParseException ex) {
			if (DEBUG_ENABLED) {
				LOG.debug("parseIso exception for argDateString: '"
						+ argDateString + "'", ex);
			}

			return null;
		}
	}

	/**
	 * Gets the time portion of the first second that is a new day.
	 * 
	 * @return the time portion of the first second that is a new day.
	 */
	public static final Date getStartOfDay() {
		return new Date(START_OF_DAY);
	}

	/**
	 * Gets the time portion of the first second that is a new day in
	 * milliseconds.
	 * 
	 * @return the time portion of the first second that is a new day.
	 */
	public static final long getStartOfDayInMillis() {
		return START_OF_DAY;
	}

	/**
	 * Gets the time portion of the last second that is the same day.
	 * 
	 * @return the time portion of the last second that is the same day.
	 */
	public static final Date getEndOfDay() {
		return new Date(END_OF_DAY);
	}

	/**
	 * Gets the time portion of the last second that is the same day in
	 * milliseconds.
	 * 
	 * @return the time portion of the last second that is the same day.
	 */
	public static final long getEndOfDayInMillis() {
		return END_OF_DAY;
	}

	/**
	 * Calculates the last millisecond of the last second of the given
	 * date/time.
	 * 
	 * @param argDate
	 *            the date to use
	 * @return the last millisecond of the last second of the specified minute
	 */
	public static final Date makeEndOfMinute(Date argDate) {
		return new Date(makeEndOfMinute(argDate.getTime()));
	}

	/**
	 * Calculates the last millisecond of the last second of the given
	 * date/time.
	 * 
	 * @param argDate
	 *            the date to use
	 * @return the last millisecond of the last second of the specified minute
	 */
	public static final long makeEndOfMinute(long argDate) {
		Calendar cal = CAL.get();

		cal.clear();
		cal.setTimeZone(TIME_ZONE_LOCAL);
		cal.setTimeInMillis(argDate);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
		return cal.getTimeInMillis();
	}

	/**
	 * Calculates the last millisecond of the given date/time.
	 * 
	 * @param argDate
	 *            the date to use
	 * @return the last millisecond of the specified second
	 */
	public static final Date makeEndOfSecond(Date argDate) {
		return new Date(makeEndOfSecond(argDate.getTime()));
	}

	/**
	 * Calculates the last millisecond of the given date/time.
	 * 
	 * @param argDate
	 *            the date to use
	 * @return the last millisecond of the specified second
	 */
	public static final long makeEndOfSecond(long argDate) {
		Calendar cal = CAL.get();

		cal.clear();
		cal.setTimeZone(TIME_ZONE_LOCAL);
		cal.setTimeInMillis(argDate);
		cal.set(Calendar.MILLISECOND, 999);
		return cal.getTimeInMillis();
	}

	/**
	 * Given a year, and the day of that year, returns a Date
	 * 
	 * @param argYear
	 *            the year
	 * @param argDayOfYear
	 *            the day of the year
	 * @return the requested date
	 */
	public static final Date dateForYearAndDayOfYear(int argYear,
			int argDayOfYear) {
		Calendar cal = CAL.get();

		cal.clear();
		cal.setTimeZone(TIME_ZONE_LOCAL);
		cal.set(Calendar.YEAR, argYear);
		cal.set(Calendar.DAY_OF_YEAR, argDayOfYear);
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		return clearTime(cal.getTime());
	}

	private static Date farPastDate_;

	/**
	 * Returns an arbitrary date from the distant past to serve as an open ended
	 * begin date for reporting purposes. The current implementation returns the
	 * digital millinium (January 1, 1970), but this is subject to change.
	 * 
	 * @return January 1st, 1970
	 */
	public static final Date getFarPastDate() {
		if (farPastDate_ == null) {
			farPastDate_ = getNewDate(0);
		}
		return getNewDate(farPastDate_.getTime());
	}

	private static Date farFutureDate_;

	/**
	 * Returns an arbitrary date from the distant future to serve as an open
	 * ended end date for reporting purposes. The current implementation returns
	 * December 31, 2999, but this is subject to change.
	 * 
	 * @return December 31, 2999
	 */
	public static final Date getFarFutureDate() {
		if (farFutureDate_ == null) {
			farFutureDate_ = parseDate("2999-12-31");
		}
		return getNewDate(farFutureDate_.getTime());
	}

	/**
	 * Gets an integer that represents a time. The format is HHmmssSSS. (Hour is
	 * 0-24, minute is 00-59, seconds is 0-59, and milliseconds is 000-999.)
	 * 
	 * @param argDate
	 *            a date object to get the time component of
	 * @return an integer representing the time
	 */
	public static final int date2TimeInt(Date argDate) {
		String s = TIME_INT_FORMAT.get().format(argDate);
		return Integer.parseInt(s);
	}

	/**
	 * Gets a java date with the date portion set to 01/01/1970 for the given
	 * time.
	 * 
	 * @param argTimeInt
	 *            a time in the format HHmmssSSS
	 * @return the date for the time
	 */
	public static final Date timeInt2Date(int argTimeInt) {
		int hours = argTimeInt / 10000000;
		argTimeInt = argTimeInt - (10000000 * hours);
		int minutes = argTimeInt / 100000;
		argTimeInt = argTimeInt - (100000 * minutes);
		int seconds = argTimeInt;
		argTimeInt = argTimeInt - (1000 * seconds);
		int milliseconds = argTimeInt;

		Calendar cal = CAL.get();

		cal.clear();
		cal.setTimeZone(TIME_ZONE_LOCAL);
		cal.set(Calendar.YEAR, 1970);
		cal.set(Calendar.MONTH, Calendar.JANUARY);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, hours);
		cal.set(Calendar.MINUTE, minutes);
		cal.set(Calendar.SECOND, seconds);
		cal.set(Calendar.MILLISECOND, milliseconds);

		return cal.getTime();
	}

	/**
	 * Gets an integer that represents a date with no time. The format is
	 * yyyyMMdd. (Year is 0000-9999, month is 00-12, and day is 00-31.)
	 * 
	 * @param argDate
	 *            a date object to get the time component of
	 * @return an integer representing the time
	 */
	public static final int date2DateInt(Date argDate) {
		if (argDate == null) {
			return 0;
		}
		String s = DATE_INT_FORMAT.get().format(argDate);
		return Integer.parseInt(s);
	}

	/**
	 * Gets a java date with the date portion set to 01/01/1970 for the given
	 * time.
	 * 
	 * @param argDateInt
	 *            a time in the format yyyyMMdd
	 * @return the date for the int
	 */
	public static final Date dateInt2Date(int argDateInt) {
		if (argDateInt <= 0) {
			return null;
		}
		try {
			return DATE_INT_FORMAT.get().parse(String.valueOf(argDateInt));
		} catch (Exception ex) {
			if (DEBUG_ENABLED) {
				LOG.debug("dateInt2Date exception for argDateInt: '"
						+ argDateInt + "'", ex);
			}

			return null;
		}
	}

	/**
	 * Calculates an age for someone born on <code>argBirthDate</code> as of
	 * <code>argTargetDate</code>.
	 * 
	 * @param argTargetDate
	 *            the "as of" date
	 * @param argBirthDate
	 *            the birth date
	 * @return the age
	 */
	public static final int getAge(Date argTargetDate, Date argBirthDate) {
		Calendar cal = CAL.get();
		Calendar cal2 = CAL2.get();

		cal.setTime(argBirthDate);
		cal2.setTime(argTargetDate);
		int diff = cal2.get(Calendar.YEAR) - cal.get(Calendar.YEAR);
		int targetMonth = cal2.get(Calendar.MONTH);
		int birthMonth = cal.get(Calendar.MONTH);
		if (targetMonth < birthMonth) {
			return --diff;
		} else if (targetMonth == birthMonth) {
			int day2 = cal2.get(Calendar.DAY_OF_MONTH);
			int day1 = cal.get(Calendar.DAY_OF_MONTH);
			if (day2 < day1) {
				return --diff;
			}
		}
		return diff;
	}

	/**
	 * Gets the day of week for a given date. (Sunday=1, Monday=2, Tuesday=3,
	 * Wednesday=4, Thursday=5, Friday=6, and Saturday=7)
	 * 
	 * @param argDate
	 *            the date of interest
	 * @return the day of week
	 */
	public static final int getDayOfWeek(Date argDate) {
		Calendar cal = CAL.get();

		cal.clear();
		cal.setTimeInMillis(argDate.getTime());
		cal.setTimeZone(TIME_ZONE_LOCAL);

		return cal.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * Gets the day of year or Julian date for a given date. (Jan 1st=1, Feb
	 * 1st=32, etc.)
	 * 
	 * @param argDate
	 *            the date of interest
	 * @return the day of year
	 */
	public static final int getDayOfYear(Date argDate) {
		Calendar cal = CAL.get();

		cal.clear();
		cal.setTimeInMillis(argDate.getTime());
		cal.setTimeZone(TIME_ZONE_LOCAL);

		return cal.get(Calendar.DAY_OF_YEAR);
	}

}
