package ru.neoanon.simpleweather.model;

/**
 * Created by eshtefan on  11.10.2018.
 */

public class DailyForecastShortItem {
    private String dayOfWeek;
    private String dayOfMonth;
    private String temp;
    private int iconId;
    private int dayOfWeekColorId;

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(String dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public int getDayOfWeekColorId() {
        return dayOfWeekColorId;
    }

    public void setDayOfWeekColorId(int dayOfWeekColorId) {
        this.dayOfWeekColorId = dayOfWeekColorId;
    }
}
