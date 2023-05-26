package com.org.weathertestapp.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class MyTimeStamp {
    public static String epochToDateTimeAppFormat(long time) {
        //String format = "dd/MM/yyyy HH:mm a";
        String format = "MMM dd, yyyy 'at' hh:mm a";
        //String format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        sdf.setTimeZone(TimeZone.getDefault());
        return sdf.format(new Date(time * 1000)).replace("am", "AM").replace("pm", "PM");
    }

    public static String epochToTime(long time) {
        String format = "hh:mm a";
        //String format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        sdf.setTimeZone(TimeZone.getDefault());
        return sdf.format(new Date(time * 1000)).replace("am", "AM").replace("pm", "PM");
    }
}
