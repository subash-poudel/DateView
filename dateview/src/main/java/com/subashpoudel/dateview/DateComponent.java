package com.subashpoudel.dateview;

import android.text.TextUtils;

// A helper class to convert a string date like 2012/12/12 to corresponding date components
class DateComponent {
    int year;
    int month;
    int day;

    // Assumes date to be of yyyy/mm/dd format
    DateComponent(String date, String separator) throws InvalidDateException {
        if (TextUtils.isEmpty(date)) {
            throw new InvalidDateException("Date cannot be null or empty.");
        }
        if (TextUtils.isEmpty(separator)) {
            throw new InvalidDateException("Separator cannot be null or empty.");
        }
        String[] dateComponents = date.split(separator);
        if (dateComponents.length != 3) {
            throw new InvalidDateException("Date is missing required component year, month or day.");
        }
        try {
            year = Integer.parseInt(dateComponents[0]);
            month = Integer.parseInt(dateComponents[1]);
            day = Integer.parseInt(dateComponents[2]);
        } catch (NumberFormatException e) {
            throw new InvalidDateException(e.getLocalizedMessage());
        }
    }
}
