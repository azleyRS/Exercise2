package com.example.rus.exercise2_2;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimesUtil {
        private static final int SECOND_MILLIS = 1000;
        private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
        private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
        private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

        public static String getTimeAgo(Date date) {

            long time = date.getTime();

            SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a");
            String dayTime = formatter.format(date);

            if (time < 1000000000000L) {
                time *= 1000;
            }

            long now = System.currentTimeMillis();
            if (time > now || time <= 0) {
                return null;
            }


            final long diff = now - time;
            if (diff < MINUTE_MILLIS) {
                return "Just now, " + dayTime;
            } else if (diff < 2 * MINUTE_MILLIS) {
                return "A minute ago, " + dayTime;
            } else if (diff < 50 * MINUTE_MILLIS) {
                return diff / MINUTE_MILLIS + " minutes ago, " + dayTime;
            } else if (diff < 90 * MINUTE_MILLIS) {
                return "An hour ago, " + dayTime;
            } else if (diff < 24 * HOUR_MILLIS) {
                return diff / HOUR_MILLIS + " hr. ago, " + dayTime;
            } else if (diff < 48 * HOUR_MILLIS) {
                return "Yesterday, " + dayTime;
            } else {
                return diff / DAY_MILLIS + " days ago, " + dayTime;
            }
        }
}
