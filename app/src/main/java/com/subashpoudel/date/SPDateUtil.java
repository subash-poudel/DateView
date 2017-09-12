package com.subashpoudel.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SPDateUtil {

    public static final String YYYY_MM_DD = "yyyy-MM-dd";

    private SPDateUtil() {
    }

    /*
    * Copied from
    * https://stackoverflow.com/questions/1021324/java-code-for-calculating-leap-year/1021373#1021373
    * :)
    * */
    public static boolean isLeapYear(int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        return cal.getActualMaximum(Calendar.DAY_OF_YEAR) > 365;
    }

    /*
    * User can input 9999-99-99 and the date formatter will treat it as a valid date
    * validateUserData prevent's above scenario
    * */
    public static boolean validateUserData(CharSequence year, CharSequence month, CharSequence day) {
        if (year == null || month == null || day == null) {
            return false;
        }
        try {
            int numericYear = Integer.parseInt(year.toString());
            int numericMonth = Integer.parseInt(month.toString());
            int numericDay = Integer.parseInt(day.toString());
            return validateUserData(numericYear, numericMonth, numericDay);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static boolean validateUserData(int year, int month, int day) {
        if (!isYearValid(year)) {
            return false;
        }
        if (!isMonthValid(month)) {
            return false;
        }
        if (!isDayValid(year, month, day)) {
            return false;
        }
        return true;
    }

    public static boolean validateUserData(DateComponent dateComponent) {
        return validateUserData(dateComponent.year, dateComponent.month, dateComponent.day);
    }

    private static boolean isYearValid(int year) {
        return year > 0;
    }

    private static boolean isMonthValid(int month) {
        return month > 0 && month < 13;
    }

    private static boolean isDayValid(int year, int month, int day) {
        if (day <= 0 || day > 31) {
            return false;
        }
        // Calendar user 0 index month so January is 0 February is 2
        month--;
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        int noOfDaysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        return day <= noOfDaysInMonth;
    }

    public static Date getDate(String dateStr, String format) {
        try {
            SimpleDateFormat fmt = new SimpleDateFormat(format, Locale.getDefault());
            return fmt.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

}
