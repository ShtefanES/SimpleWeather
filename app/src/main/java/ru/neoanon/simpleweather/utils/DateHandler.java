package ru.neoanon.simpleweather.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import javax.inject.Inject;

import ru.neoanon.simpleweather.model.DateInfo;

/**
 * Created by eshtefan on  25.09.2018.
 */

public class DateHandler {
    private Locale locale;

    @Inject
    public DateHandler(Locale locale) {
        this.locale = locale;
    }

    public long getCurrentTimestamp() {
        return Calendar.getInstance().getTimeInMillis();
    }

    public int getCurrentHour() {
        return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    }

    public int getLocalOffset() {
        return Calendar.getInstance().getTimeZone().getRawOffset() / 3600000;
    }

    public String getHoursAndMinutes(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", locale);
        return sdf.format(calendar.getTime());
    }

    public DateInfo getDataInfo(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        return new DateInfo(
                getDayOfWeek(locale, calendar),
                getDayOfMonth(locale, calendar),
                isWeekend(calendar)
        );
    }

    public String getDayOfMonthAndWeek(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat("EEE\nd", locale);
        return sdf.format(calendar.getTime());
    }

    private String getDayOfWeek(Locale locale, Calendar calendar) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE", locale);
        return capitalizeFirstLater(sdf.format(calendar.getTime()));
    }

    private String getDayOfMonth(Locale locale, Calendar calendar) {
        SimpleDateFormat sdf = new SimpleDateFormat("d MMMM", locale);
        return sdf.format(calendar.getTime());
    }

    private boolean isWeekend(Calendar calendar) {
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        return (day == 1 || day == 7);
    }

    private String capitalizeFirstLater(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
