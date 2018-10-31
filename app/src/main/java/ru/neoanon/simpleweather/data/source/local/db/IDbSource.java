package ru.neoanon.simpleweather.data.source.local.db;

import java.util.List;

import io.reactivex.Flowable;
import ru.neoanon.simpleweather.data.source.local.db.currentweather.CurrentWeather;
import ru.neoanon.simpleweather.data.source.local.db.dailyforecast.DailyForecast;
import ru.neoanon.simpleweather.data.source.local.db.hourlyforecast.HourlyForecast;
import ru.neoanon.simpleweather.data.source.local.db.location.RegionLocation;

/**
 * Created by eshtefan on  20.09.2018.
 */

public interface IDbSource {
   long saveLocation(RegionLocation regionLocation);

    RegionLocation getLocation(long id);

    RegionLocation getLocation(String locationName);

    Flowable<List<RegionLocation>> loadAllLocations();

    Flowable<List<DailyForecast>> loadAllDailyForecastForRegion(long regionId);

    Flowable<List<HourlyForecast>> loadAllHourlyForecastForRegion(long regionId);

    Flowable<List<CurrentWeather>> loadWeatherById(long regionId);

    List<DailyForecast> loadAllDailyForecasts(long regionId);

    int deleteLocation(long id);

    long saveWeather(CurrentWeather weather);

    CurrentWeather getWeather(long regionId);

    void deleteAllWeathers();

    void deleteAllHourlyForecasts();

    void deleteAllDailyForecasts();

    int deleteWeather(long regionId);

    void saveHourlyForecasts(List<HourlyForecast> forecasts);

    int deleteHourlyForecasts(long regionId);

    void saveDailyForecasts(List<DailyForecast> forecasts);

    int deleteDailyForecasts(long regionId);
}