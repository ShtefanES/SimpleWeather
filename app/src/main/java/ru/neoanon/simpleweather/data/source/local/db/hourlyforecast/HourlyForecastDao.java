package ru.neoanon.simpleweather.data.source.local.db.hourlyforecast;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by eshtefan on  25.09.2018.
 */

@Dao
public interface HourlyForecastDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertForecast(List<HourlyForecast> forecasts);

    @Query("SELECT * FROM hourly_forecast WHERE region_id=:regionId")
    List<HourlyForecast> loadForecasts(long regionId);

    @Query("DELETE FROM hourly_forecast")
    void clearAllForecasts();

    @Query("DELETE FROM hourly_forecast WHERE region_id=:regionId")
    int deleteForecastsById(long regionId);

    @Query("SELECT * FROM hourly_forecast WHERE region_id=:regionId")
    Flowable<List<HourlyForecast>> loadAllForecastById(long regionId);
}
