package br.com.curubodenga.pimfeeder.utils;

import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateUtils {

    public static String formatDateTime(Context context, String timeToFormat) {

        String finalDateTime = "";

        SimpleDateFormat iso8601Format = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");

        Date date = null;
        if (timeToFormat != null) {
            try {
                date = iso8601Format.parse(timeToFormat);
            } catch (ParseException e) {
                date = null;
            }

            if (date != null) {
                long when = date.getTime();
                int flags = 0;
                flags |= android.text.format.DateUtils.FORMAT_SHOW_TIME;
                flags |= android.text.format.DateUtils.FORMAT_SHOW_DATE;
                flags |= android.text.format.DateUtils.FORMAT_ABBREV_MONTH;
                flags |= android.text.format.DateUtils.FORMAT_SHOW_YEAR;

                finalDateTime = android.text.format.DateUtils.formatDateTime(context,
                        when + TimeZone.getDefault().getOffset(when), flags);
            }
        }
        return finalDateTime;
    }

    public static String getHHmm(String timeToFormat) {

        String finalDateTime = "";

        SimpleDateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date date = null;
        if (timeToFormat != null) {
            try {
                date = iso8601Format.parse(timeToFormat);
            } catch (ParseException e) {
                date = null;
            }

            if (date != null) {
                SimpleDateFormat HHmmformat = new SimpleDateFormat("HH:mm");
                finalDateTime = HHmmformat.format(date);
            }
        }
        return finalDateTime;
    }

    public static String getCompleteDay(String timeToFormat) {

        String finalDateTime = "";

        SimpleDateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date date = null;
        if (timeToFormat != null) {
            try {
                date = iso8601Format.parse(timeToFormat);
            } catch (ParseException e) {
                date = null;
            }

            if (date != null) {
                SimpleDateFormat HHmmformat = new SimpleDateFormat("E, dd MMM");
                finalDateTime = HHmmformat.format(date);
            }
        }
        return toPortuguese(finalDateTime);
    }

    public static String getCompleteDay(Date date){
        if (date != null) {
            SimpleDateFormat HHmmformat = new SimpleDateFormat("E, dd MMM");
            return toPortuguese(HHmmformat.format(date));
        }
        return null;
    }

    private static String toPortuguese(String date) {
        String portugueseDate = date;

        portugueseDate = portugueseDate.replace("Mon", "Seg");
        portugueseDate = portugueseDate.replace("Tue", "Ter");
        portugueseDate = portugueseDate.replace("Wed", "Qua");
        portugueseDate = portugueseDate.replace("Thu", "Qui");
        portugueseDate = portugueseDate.replace("Fri", "Sex");
        portugueseDate = portugueseDate.replace("Sat", "Sab");
        portugueseDate = portugueseDate.replace("Sun", "Dom");

        portugueseDate = portugueseDate.replace("Jul", "de Jul");
        portugueseDate = portugueseDate.replace("Aug", "de Ago");

        return portugueseDate;
    }
}
