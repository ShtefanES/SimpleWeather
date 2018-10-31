package ru.neoanon.simpleweather.utils;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by eshtefan on  24.10.2018.
 */

public class HourlyForecastHandlerTest {
    private int positiveOffset;
    private int negativeOffset;
    private int withoutOffset;

    @Before
    public void initOffset() {
        positiveOffset = 7;
        negativeOffset = -5;
        withoutOffset = 0;
    }

    @Test
    public void getNumberOfHourlyForecast() {
        int currentHour = 9;
        int actualNumberOfForecasts = HourlyForecastHandler.getNumberOfHourlyForecast(currentHour, positiveOffset);
        int expectedNumberOfForecast = 13;
        assertEquals(expectedNumberOfForecast, actualNumberOfForecasts);
    }

    @Test
    public void getNumberOfHourlyForecastWithNegativeOffset() {
        int currentHour = 17;
        int actualNumberOfForecasts = HourlyForecastHandler.getNumberOfHourlyForecast(currentHour, negativeOffset);
        int expectedNumberOfForecast = 10;
        assertEquals(expectedNumberOfForecast, actualNumberOfForecasts);
    }

    @Test
    public void getNumberOfHourlyForecastWithoutOffset() {
        int currentHour = 0;
        int actualNumberOfForecasts = HourlyForecastHandler.getNumberOfHourlyForecast(currentHour, withoutOffset);
        int expectedNumberOfForecast = 15;
        assertEquals(expectedNumberOfForecast, actualNumberOfForecasts);
    }
}