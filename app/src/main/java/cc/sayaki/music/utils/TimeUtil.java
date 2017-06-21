package cc.sayaki.music.utils;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Author: sayaki
 * Date: 2017/6/8
 */
public class TimeUtil {

    @SuppressLint("DefaultLocale")
    public static String formatDuration(int duration) {
        duration /= 1000; // milliseconds into seconds
        int minute = duration / 60;
        int hour = minute / 60;
        minute %= 60;
        int second = duration % 60;
        if (hour != 0) {
            return String.format("%2d:%02d:%02d", hour, minute, second);
        } else {
            return String.format("%02d:%02d", minute, second);
        }
    }

    public static String getCurrentTime(String format) {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CHINA);
        return sdf.format(date);
    }
}
