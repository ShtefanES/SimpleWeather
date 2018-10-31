package ru.neoanon.simpleweather.data.source.local.db;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import ru.neoanon.simpleweather.data.source.local.db.currentweather.CurrentWeather;
import ru.neoanon.simpleweather.data.source.local.db.dailyforecast.DailyForecast;
import ru.neoanon.simpleweather.data.source.local.db.hourlyforecast.HourlyForecast;
import ru.neoanon.simpleweather.data.source.local.db.location.RegionLocation;


/**
 * Created by eshtefan on  20.09.2018.
 */

public class DbSource implements IDbSource {
    private AppDatabase appDatabase;

    @Inject
    public DbSource(AppDatabase appDatabase) {
        this.appDatabase = appDatabase;
    }

    @Override
    public long saveLocation(RegionLocation regionLocation) {
        return appDatabase.locationDao().insertLocation(regionLocation);
    }

    @Override
    public RegionLocation getLocation(long id) {
        return appDatabase.locationDao().loadLocation(id);
    }

    @Override
    public RegionLocation getLocation(String locationName) {
        return appDatabase.locationDao().loadLocation(locationName);
    }

    @Override
    public Flowable<List<RegionLocation>> loadAllLocations() {
        return appDatabase.locationDao().loadAllLocations();
    }

    @Override
    public Flowable<List<DailyForecast>> loadAllDailyForecastForRegion(long regionId) {
        return appDatabase.dailyForecastDao().loadAllForecastById(regionId);
    }

    @Override
    public Flowable<List<HourlyForecast>> loadAllHourlyForecastForRegion(long regionId) {
        return appDatabase.hourlyForecastDao().loadAllForecastById(regionId);
    }

    @Override
    public Flowable<List<CurrentWeather>> loadWeatherById(long regionId) {
        return appDatabase.currentWeatherDao().loadWeatherById(regionId);
    }

    @Override
    public List<DailyForecast> loadAllDailyForecasts(long regionId) {
        return appDatabase.dailyForecastDao().loadForecasts(regionId);
    }

    @Override
    public int deleteLocation(long id) {
        return appDatabase.locationDao().deleteRegionById(id);
    }

    @Override
    public long saveWeather(CurrentWeather weather) {
        return appDatabase.currentWeatherDao().insertWeather(weather);
    }

    @Override
    public CurrentWeather getWeather(long regionId) {
        return appDatabase.currentWeatherDao().loadWeather(regionId);
    }

    @Override
    public void deleteAllWeathers() {
        appDatabase.currentWeatherDao().clearAllWeathers();
    }

    @Override
    public void deleteAllHourlyForecasts() {
        appDatabase.hourlyForecastDao().clearAllForecasts();
    }

    @Override
    public void deleteAllDailyForecasts() {
        appDatabase.dailyForecastDao().clearAllForecasts();
    }

    @Override
    public int deleteWeather(long regionId) {
        return appDatabase.currentWeatherDao().deleteWeatherById(regionId);
    }

    @Override
    public void saveHourlyForecasts(List<HourlyForecast> forecasts) {
        appDatabase.hourlyForecastDao().insertForecast(forecasts);
    }

    @Override
    public int deleteHourlyForecasts(long regionId) {
        return appDatabase.hourlyForecastDao().deleteForecastsById(regionId);
    }

    @Override
    public void saveDailyForecasts(List<DailyForecast> forecasts) {
        appDatabase.dailyForecastDao().insertForecast(forecasts);
    }

    @Override
    public int deleteDailyForecasts(long regionId) {
        return appDatabase.dailyForecastDao().deleteForecastsById(regionId);
    }
}
