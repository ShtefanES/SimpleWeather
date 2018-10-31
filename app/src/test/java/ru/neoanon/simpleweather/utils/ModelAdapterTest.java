package ru.neoanon.simpleweather.utils;

import android.app.Application;
import android.content.Context;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import ru.neoanon.simpleweather.R;
import ru.neoanon.simpleweather.data.source.local.db.currentweather.CurrentWeather;
import ru.neoanon.simpleweather.data.source.local.db.dailyforecast.DailyForecast;
import ru.neoanon.simpleweather.model.CurrentWeatherItem;
import ru.neoanon.simpleweather.model.DailyForecastItem;
import ru.neoanon.simpleweather.model.enumerations.CompassValue;
import ru.neoanon.simpleweather.model.enumerations.PressureType;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by eshtefan on  24.10.2018.
 */

public class ModelAdapterTest {
    @Mock
    Application application;
    @Mock
    Context context;
    @Mock
    DateHandler dateHandler;
    @Mock
    TemperatureHandler temperatureHandler;
    private ModelAdapter modelAdapter;
    private long timestamp = 1540375200000L;//(GMT): Wednesday, 24 October 2018 г., 10:00:00
    private String iconId = "13d";
    private String windDirect = CompassValue.S.name();
    private String description = "simple description";
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();


    @Before
    public void initModelAdapter() {
        modelAdapter = new ModelAdapter(application, dateHandler, temperatureHandler);
    }

    @Test
    public void getDailyForecastItem() {
        when(application.getApplicationContext()).thenReturn(context);
        DailyForecast dailyForecast = new DailyForecast();
        dailyForecast.setIconId(iconId);
        dailyForecast.setWindDirect(windDirect);
        modelAdapter.getDailyForecastItem(dailyForecast, PressureType.hectoPascal.name());
        verify(dateHandler, times(1)).getDayOfMonthAndWeek(anyLong());
        verify(temperatureHandler, times(4)).getTemp(anyInt());
    }


    @Test
    public void getDailyForecastItemCheckTemperatures() {
        int morningTemp = 10;
        int dayTemp = 17;
        int eveningTemp = 15;
        int nightTemp = 7;

        when(application.getApplicationContext()).thenReturn(context);
        when(temperatureHandler.getTemp(morningTemp)).thenReturn(String.valueOf(morningTemp));
        when(temperatureHandler.getTemp(dayTemp)).thenReturn(String.valueOf(dayTemp));
        when(temperatureHandler.getTemp(eveningTemp)).thenReturn(String.valueOf(eveningTemp));
        when(temperatureHandler.getTemp(nightTemp)).thenReturn(String.valueOf(nightTemp));

        DailyForecast dailyForecast = new DailyForecast();
        dailyForecast.setIconId(iconId);
        dailyForecast.setWindDirect(windDirect);
        dailyForecast.setTempMorning(morningTemp);
        dailyForecast.setTempDay(dayTemp);
        dailyForecast.setTempEvening(eveningTemp);
        dailyForecast.setTempNight(nightTemp);
        DailyForecastItem item = modelAdapter.getDailyForecastItem(dailyForecast, PressureType.hectoPascal.name());

        String expectedMorningTemp = String.valueOf(morningTemp);
        String expectedDayTemp = String.valueOf(dayTemp);
        String expectedEveningTemp = String.valueOf(eveningTemp);
        String expectedNightTemp = String.valueOf(nightTemp);

        assertEquals(expectedMorningTemp, item.getMorningTemp());
        assertEquals(expectedDayTemp, item.getDayTemp());
        assertEquals(expectedEveningTemp, item.getEveningTemp());
        assertEquals(expectedNightTemp, item.getNightTemp());
    }

    @Test
    public void getDailyForecastItemCheckDate() {
        String expectedHoursAndMinutes = "10:00";
        when(application.getApplicationContext()).thenReturn(context);
        when(dateHandler.getDayOfMonthAndWeek(timestamp)).thenReturn(expectedHoursAndMinutes);
        DailyForecast dailyForecast = new DailyForecast();
        dailyForecast.setIconId(iconId);
        dailyForecast.setWindDirect(windDirect);
        dailyForecast.setTimestamp(timestamp);
        DailyForecastItem item = modelAdapter.getDailyForecastItem(dailyForecast, PressureType.hectoPascal.name());

        assertEquals(expectedHoursAndMinutes, item.getTitle());
    }

    @Test
    public void getDailyForecastItemCheckWeather() {
        int expectedWeatherIconId = R.drawable.d31;

        when(application.getApplicationContext()).thenReturn(context);
        DailyForecast dailyForecast = new DailyForecast();
        dailyForecast.setIconId(iconId);
        dailyForecast.setWindDirect(windDirect);
        dailyForecast.setDescription(description);
        DailyForecastItem item = modelAdapter.getDailyForecastItem(dailyForecast, PressureType.hectoPascal.name());

        assertEquals(expectedWeatherIconId, item.getWeatherIconId());
        assertEquals(description, item.getWeatherDescription());
    }

    @Test
    public void getDailyForecastItemCheckWind() {
        int windSpeedValue = 7;
        String windDirectStr = "s";
        String meterInSec = "7 m/s";
        int expectedWindDirectIconId = R.drawable.ic_s_middle;
        when(application.getApplicationContext()).thenReturn(context);
        when(context.getString(R.string.m_sec, String.valueOf(windSpeedValue))).thenReturn(meterInSec);
        when(context.getString(R.string.south)).thenReturn(windDirectStr);
        DailyForecast dailyForecast = new DailyForecast();
        dailyForecast.setIconId(iconId);
        dailyForecast.setWindDirect(windDirect);
        dailyForecast.setWindSpeed(windSpeedValue);
        DailyForecastItem item = modelAdapter.getDailyForecastItem(dailyForecast, PressureType.hectoPascal.name());
        assertEquals(String.format("%s %s", meterInSec, windDirectStr), item.getWindDescription());
        assertEquals(expectedWindDirectIconId, item.getWindDirectIconId());
    }

    @Test
    public void getDailyForecastItemCheckPrecipitation() {
        int rainValue = 2;
        int snowValue = 0;
        String expectedSnowStr = null;
        String expectedRainStr = String.format("%s mm", rainValue);
        boolean expectedIsWithoutPrecipitation = false;

        when(application.getApplicationContext()).thenReturn(context);
        when(context.getString(R.string.millimeters_of_precipitation, String.valueOf(rainValue))).thenReturn(expectedRainStr);
        DailyForecast dailyForecast = new DailyForecast();
        dailyForecast.setIconId(iconId);
        dailyForecast.setWindDirect(windDirect);
        dailyForecast.setRain(rainValue);
        dailyForecast.setSnow(snowValue);
        DailyForecastItem item = modelAdapter.getDailyForecastItem(dailyForecast, PressureType.hectoPascal.name());

        assertEquals(expectedIsWithoutPrecipitation, item.isWithoutPrecipitation());
        assertEquals(expectedSnowStr, item.getSnow());
        assertEquals(expectedRainStr, item.getRain());
    }

    @Test
    public void getDailyForecastItemCheckPressure() {
        int pressureValue = 753;
        String expectedPressure = pressureValue + " mm";
        when(application.getApplicationContext()).thenReturn(context);
        when(context.getString(R.string.pressure_mm_hg, String.valueOf(pressureValue))).thenReturn(expectedPressure);
        DailyForecast dailyForecast = new DailyForecast();
        dailyForecast.setIconId(iconId);
        dailyForecast.setWindDirect(windDirect);
        dailyForecast.setPressure(pressureValue);
        DailyForecastItem item = modelAdapter.getDailyForecastItem(dailyForecast, PressureType.torr.name());

        assertEquals(expectedPressure, item.getPressure());
    }

    @Test
    public void getDailyForecastItemCheckHumidity() {
        int humidityValue = 75;
        String expectedHumidity = humidityValue + " %";
        when(application.getApplicationContext()).thenReturn(context);

        DailyForecast dailyForecast = new DailyForecast();
        dailyForecast.setIconId(iconId);
        dailyForecast.setWindDirect(windDirect);
        dailyForecast.setHumidity(humidityValue);
        DailyForecastItem item = modelAdapter.getDailyForecastItem(dailyForecast, PressureType.torr.name());

        assertEquals(expectedHumidity, item.getHumidity());
    }

    @Test
    public void getWeatherItem() {
        int tempValue = -20;
        String tempStr = "temp mock";

        when(temperatureHandler.getTemp(tempValue)).thenReturn(tempStr);

        CurrentWeather currentWeather = new CurrentWeather();
        currentWeather.setTemp(tempValue);
        currentWeather.setIconId(iconId);
        currentWeather.setDescription(description);
        CurrentWeatherItem item = modelAdapter.getWeatherItem(currentWeather);

        assertEquals(R.drawable.big_d31, item.getIconId());
        assertEquals(R.drawable.ic_background_snow, item.getBackgroundIconId());
        assertEquals(tempStr, item.getTemp());
        assertEquals(description, item.getWeatherDescription());

        verify(temperatureHandler, times(1)).getTemp(anyInt());
    }
}