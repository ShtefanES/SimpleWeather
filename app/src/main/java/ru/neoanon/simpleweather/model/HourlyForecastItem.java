package ru.neoanon.simpleweather.model;

/**
 * Created by eshtefan on  12.10.2018.
 */

public class HourlyForecastItem implements CommonHourlyItem {
    private String time;
    private String temp;
    private int iconId;

    public HourlyForecastItem(String time, String temp, int iconId) {
        this.time = time;
        this.temp = temp;
        this.iconId = iconId;
    }

    public String getTemp() {
        return temp;
    }

    public int getIconId() {
        return iconId;
    }

    public String getTime() {
        return time;
    }
}
