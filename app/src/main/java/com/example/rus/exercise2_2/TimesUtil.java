package com.example.rus.exercise2_2;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TimesUtil {
        //after pr recommendation
        private static final int MINUTE_MILLIS = (int) TimeUnit.MINUTES.toMillis(1);
        private static final int HOUR_MILLIS = (int) TimeUnit.HOURS.toMillis(1);
        private static final int DAY_MILLIS = (int) TimeUnit.DAYS.toMillis(1);
        private static final long TIME_IN_SECONDS = 1000000000000L;

        public static String getTimeAgo(Date date) {

            long time = date.getTime();

            SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a");
            String dayTime = formatter.format(date);

            //if time was given in seconds multiply by 1000, can i use SECONDS.toMillis?
            if (time < TIME_IN_SECONDS) {
                time = TimeUnit.SECONDS.toMillis(time);
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
