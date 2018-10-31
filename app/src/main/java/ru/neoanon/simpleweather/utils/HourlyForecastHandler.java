package ru.neoanon.simpleweather.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eshtefan on  25.09.2018.
 */

public class HourlyForecastHandler {
    private static final int INTERVAL = 3;//interval for hourly forecast. Do not modify, look at api response
    private static final int NUMBER_OF_DAY = 2;// no more than 5 days(according to the OpenWeatherMap documentation)
    private static final int DAY = 24;

    private HourlyForecastHandler() {
    }

    /**
     * Calculates the number of forecasts for today and the next day.
     *
     * @param currentHour hour of current time
     * @param timeOffset  offset of local time relative to UTC in hours
     * @return number of forecast
     */
    public static int getNumberOfHourlyForecast(int currentHour, int timeOffset) {
        int amount = DAY / INTERVAL;//amount of forecasts for one day
        List<Integer> hours = new ArrayList<>();
        int hour = 0;
        while (hour < DAY) {
            hours.add(hour);
            hour = hour + INTERVAL;
        }

        int todayForecasts = 0;
        for (int i = 0; i < hours.size(); i++) {
            int temp = (hours.get(i) + timeOffset + DAY) % DAY;
            if (temp > currentHour) {
                todayForecasts++;
            }
        }

        int nextDaysForecasts = amount * (NUMBER_OF_DAY - 1);
        return todayForecasts + nextDaysForecasts;
    }
}
