package ru.neoanon.simpleweather.data.source.local.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import ru.neoanon.simpleweather.data.source.local.db.currentweather.CurrentWeather;
import ru.neoanon.simpleweather.data.source.local.db.currentweather.CurrentWeatherDao;
import ru.neoanon.simpleweather.data.source.local.db.dailyforecast.DailyForecast;
import ru.neoanon.simpleweather.data.source.local.db.dailyforecast.DailyForecastDao;
import ru.neoanon.simpleweather.data.source.local.db.hourlyforecast.HourlyForecast;
import ru.neoanon.simpleweather.data.source.local.db.hourlyforecast.HourlyForecastDao;
import ru.neoanon.simpleweather.data.source.local.db.location.LocationDao;
import ru.neoanon.simpleweather.data.source.local.db.location.RegionLocation;

/**
 * Created by eshtefan on  20.09.2018.
 */

@Database(entities = {
        CurrentWeather.class,
        HourlyForecast.class,
        DailyForecast.class,
        RegionLocation.class
}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CurrentWeatherDao currentWeatherDao();

    public abstract HourlyForecastDao hourlyForecastDao();

    public abstract DailyForecastDao dailyForecastDao();

    public abstract LocationDao locationDao();
}