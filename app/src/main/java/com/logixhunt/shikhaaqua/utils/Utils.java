package com.logixhunt.shikhaaqua.utils;

import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Utils {

    public static String changeDateFormat(String fromFormat, String toFormat, String dateStr) {

        SimpleDateFormat sdfIn = new SimpleDateFormat(fromFormat, Locale.US);
        Date date = null;
        try {
            date = sdfIn.parse(dateStr);
        } catch (ParseException e) {

            e.printStackTrace();
        }
        SimpleDateFormat sdfOut = new SimpleDateFormat(toFormat, Locale.US);
        String formattedTime = sdfOut.format(date);

        return formattedTime;

    }

    public static String getCurrentYear() {
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        String yearInString = String.valueOf(year);
        return yearInString;
    }

    public static boolean checkBetweenDates() {
        SimpleDateFormat sdf = new SimpleDateFormat(Constant.yyyyMMdd, Locale.US);
        SimpleDateFormat sdf2 = new SimpleDateFormat(Constant.yyyyMMddHHmmss, Locale.US);
        Date d1 = new Date();
        Date d2 = new Date();
        String dateFrom = sdf.format(d1);
        String dateTo = sdf.format(d2);


        Date min = null, max = null;   // assume these are set to something
        Date d = new Date();
        try {
            min = sdf2.parse(dateFrom + " 09:00:00");
            max = sdf2.parse(dateTo + " 20:00:00");
        } catch (ParseException e) {

            e.printStackTrace();
        }
        // the date in question

        return d.after(min) && d.before(max);
    }

    public static int getDaysDifference(Date fromDate, Date toDate) {
        if (fromDate == null || toDate == null)
            return 0;

        return (int) ((toDate.getTime() - fromDate.getTime()) / (1000 * 60 * 60 * 24));
    }

    public static String getDate(long milliSeconds, String outputFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(outputFormat);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public static String indianCurrencyFormatText(String number) {
        try {
            double num = Double.parseDouble(number);
            DecimalFormat dmf = new DecimalFormat("##,##,###.##");
            number = dmf.format(num);
        } catch (Exception e) {
            number = "0";
        }
        return "\u20B9" + number;
    }


    public static boolean isCurrentTimeBetween(String startTime, String endTime) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.US);
            Date currentTime = Calendar.getInstance().getTime();
            Date startTimeParsed = sdf.parse(startTime);
            Date endTimeParsed = sdf.parse(endTime);


            return currentTime.after(startTimeParsed) && currentTime.before(endTimeParsed);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

    }


    public static boolean isCurrentTimeGreaterOrEqual(String tabEndTime) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.US);

        try {

            String currentTime = timeFormat.format(new Date());
            Date currentTimeDate = timeFormat.parse(currentTime);
            Date tabEndTimeDate = timeFormat.parse(tabEndTime);

            return currentTimeDate.compareTo(tabEndTimeDate) >= 0;
        } catch (ParseException e) {
            e.printStackTrace();
            return false; // In case of parse error, return false
        }
    }

    public static List<String> getRemainingDaysOfMonth() {
        Calendar mCalendar = Calendar.getInstance();
// Calculate remaining days in month
        int remainingDay = mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH) - mCalendar.get(Calendar.DAY_OF_MONTH) + 1;
        ArrayList<String> allDays = new ArrayList<>();
        SimpleDateFormat mFormat = new SimpleDateFormat(Constant.EEddMMM, Locale.US);
        for (int i = 0; i < remainingDay; i++) {
            allDays.add(mFormat.format(mCalendar.getTime()));
            mCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return allDays;
    }

    public static List<String> getNext30Days() {
        Calendar mCalendar = Calendar.getInstance();
        ArrayList<String> next30Days = new ArrayList<>();
        SimpleDateFormat mFormat = new SimpleDateFormat(Constant.EEddMMM, Locale.US);

        for (int i = 0; i < 30; i++) {
            next30Days.add(mFormat.format(mCalendar.getTime()));
            mCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        return next30Days;
    }

    public static List<String> getNext30Days(String startingDate) {
        ArrayList<String> next30Days = new ArrayList<>();
        SimpleDateFormat inputFormat = new SimpleDateFormat(Constant.yyyyMMdd, Locale.US);
        SimpleDateFormat outputFormat = new SimpleDateFormat(Constant.EEddMMM, Locale.US);

        try {
            Date startDate = inputFormat.parse(startingDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);

            for (int i = 0; i < 30; i++) {
                if (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                    next30Days.add(outputFormat.format(calendar.getTime()));
                }
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return next30Days;
    }

    public static List<String> getDaysOfMonth() {
        Calendar mCalendar = Calendar.getInstance();
        int daysInMonth = mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        ArrayList<String> allDays = new ArrayList<String>();
        SimpleDateFormat mFormat = new SimpleDateFormat(Constant.EEddMMM, Locale.US);
        for (int i = 0; i < daysInMonth; i++) {
            // Add day to list
            allDays.add(mFormat.format(mCalendar.getTime()));
            // Move next day
            mCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return allDays;
    }

    public static String getSha256Hash(String input) {
        try {
            MessageDigest digest = null;
            try {
                digest = MessageDigest.getInstance("SHA-256");
            } catch (NoSuchAlgorithmException e1) {
                e1.printStackTrace();
            }
            digest.reset();
            return bin2hex(digest.digest(input.getBytes()));
        } catch (Exception ignored) {
            return null;
        }
    }

    private static String bin2hex(byte[] data) {
        StringBuilder hex = new StringBuilder(data.length * 2);
        for (byte b : data)
            hex.append(String.format("%02x", b & 0xFF));
        return hex.toString();
    }

}
