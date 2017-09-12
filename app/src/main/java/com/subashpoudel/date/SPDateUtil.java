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
