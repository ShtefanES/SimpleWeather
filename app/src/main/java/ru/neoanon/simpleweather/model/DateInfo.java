package ru.neoanon.simpleweather.model;

/**
 * Created by eshtefan on  04.10.2018.
 */

public class DateInfo {
    private String dayOfWeek;
    private String dayOfMonth;
    private boolean isWeekend;

    public DateInfo(String dayOfWeek, String dayOfMonth, boolean isWeekend) {
        this.dayOfWeek = dayOfWeek;
        this.dayOfMonth = dayOfMonth;
        this.isWeekend = isWeekend;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public String getDayOfMonth() {
        return dayOfMonth;
    }

    public boolean isWeekend() {
        return isWeekend;
    }
}
