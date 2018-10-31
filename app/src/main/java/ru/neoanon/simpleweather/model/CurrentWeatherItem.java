package ru.neoanon.simpleweather.model;

/**
 * Created by eshtefan on  12.10.2018.
 */

public class CurrentWeatherItem {
    private String temp;
    private int iconId;
    private String weatherDescription;
    private int backgroundIconId;

    public CurrentWeatherItem(String temp, int iconId, String weatherDescription, int backgroundIconId) {
        this.temp = temp;
        this.iconId = iconId;
        this.weatherDescription = weatherDescription;
        this.backgroundIconId = backgroundIconId;
    }

    public String getTemp() {
        return temp;
    }

    public int getIconId() {
        return iconId;
    }

    public String getWeatherDescription() {
        return weatherDescription;
    }

    public int getBackgroundIconId() {
        return backgroundIconId;
    }
}
