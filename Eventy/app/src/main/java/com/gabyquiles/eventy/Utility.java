package com.gabyquiles.eventy;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility class
 *
 * @author gabrielquiles-perez
 */
public class Utility {
    private final String LOG_TAG = Utility.class.getSimpleName();

    static final String full_date_format = "EEEE MMM dd, yyyy hh:mm a";

    public static String formatDate(Date date) {
        SimpleDateFormat dayFormat = new SimpleDateFormat(full_date_format);
        return dayFormat.format(date);
    }
}
