package ru.neoanon.simpleweather.utils;

/**
 * Created by eshtefan on  02.10.2018.
 */

public class Constants {
    public static final String OWM_API_KEY = "your api key";
    public static final String TYPE_OF_ACCURACY = "accurate";
    public static final int NUMBER_OF_DAILY_FORECASTS = 10;
    public static final long CACHE_LIFETIME = 300000L;//in milliseconds(5 min)

    public static final long CURRENT_LOCATION_ID = 1000000000L;

    public static final String DEGREE_CELSIUS = " \u2103";
    public static final String DEGREE_KELVIN = " \u00B0K";
    public static final String DEGREE = "\u00B0";

    private Constants() {
    }
}
