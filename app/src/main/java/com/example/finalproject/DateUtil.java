package com.example.finalproject;

import android.util.Log;

import java.text.DateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {

    public DateUtil() { }

    public static LocalDate getMondayOfWeek() {
        LocalDate today = LocalDate.now();
        LocalDate monday = today;

        while (monday.getDayOfWeek() != DayOfWeek.MONDAY) {
            monday = monday.minusDays(1);
        }
        return monday;
    }

    public static LocalDate getSundayOfWeek() {
        LocalDate today = LocalDate.now();
        LocalDate sunday = today;

        while (sunday.getDayOfWeek() != DayOfWeek.SUNDAY) {
            sunday = sunday.plusDays(1);
        }
        return sunday;
    }

    public static Calendar getCalendar() {
        long dateTime = System.currentTimeMillis();
        Date date = DateUtil.stringToDate(String.valueOf(dateTime));
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        cal.setTime(date);
        return cal;
    }

    public static Date stringToDate(String string) {
        DateFormat format = DateFormat.getDateInstance();
        Date date = new Date();

        try {
            date = format.parse(string);
        } catch (Exception e) {
            Log.e("Date parse error", e.toString());
        }

        return date;
    }

    public static String getMonthString(int month) {
        String monthString = "";
        switch (month) {
            case 0:
                monthString = "January";
                break;
            case 1:
                monthString = "February";
                break;
            case 2:
                monthString = "March";
                break;
            case 3:
                monthString = "April";
                break;
            case 4:
                monthString = "May";
                break;
            case 5:
                monthString = "June";
                break;
            case 6:
                monthString = "July";
                break;
            case 7:
                monthString = "August";
                break;
            case 8:
                monthString = "September";
                break;
            case 9:
                monthString = "October";
                break;
            case 10:
                monthString = "November";
                break;
            case 11:
                monthString = "December";
                break;
        }
        return monthString;
    }
}
