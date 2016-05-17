package com.gabyquiles.eventy;

import android.content.Context;

import com.firebase.client.AuthData;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Utility class
 *
 * @author gabrielquiles-perez
 */
public class Utility {
    private final String LOG_TAG = Utility.class.getSimpleName();

    static final String full_date_format = "EEEE MMM dd, yyyy hh:mm a";
    static final String short_date_format = "MMM dd, yyyy";
    static final String time_format = "hh:mm a";

    public static String formatFullDate(long timestamp) {
        SimpleDateFormat dayFormat = new SimpleDateFormat(full_date_format);
        return dayFormat.format(timestamp);
    }

    public static String formatShortDate(long timestamp) {
        SimpleDateFormat dayFormat = new SimpleDateFormat(short_date_format);
        return dayFormat.format(timestamp);
    }
    public static String formatTime(long timestamp) {
        SimpleDateFormat dayFormat = new SimpleDateFormat(time_format);
        return dayFormat.format(timestamp);
    }

    public static long createDate(long baseTimestamp, int year, int month, int date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(baseTimestamp);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DATE, date);

        return calendar.getTimeInMillis();
    }

    public static long createTime(long baseTimestamp, int hour, int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(baseTimestamp);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minutes);

        return calendar.getTimeInMillis();
    }

    public static String getFirebaseBaseUrl(Context context, AuthData authData) {
        String baseUrl = context.getString(R.string.firebase_base_url)
                + context.getString(R.string.firebase_users_path);
        if(authData != null) {
            baseUrl += authData.getUid();
        }
        return baseUrl;
    }


}
