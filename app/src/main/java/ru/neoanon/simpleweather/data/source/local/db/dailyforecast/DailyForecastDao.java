package ru.neoanon.simpleweather.data.source.local.db.dailyforecast;

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
public interface DailyForecastDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertForecast(List<DailyForecast> forecasts);

    @Query("SELECT * FROM daily_forecast WHERE region_id=:regionId")
    List<DailyForecast> loadForecasts(long regionId);

    @Query("DELETE FROM daily_forecast")
    void clearAllForecasts();

    @Query("DELETE FROM daily_forecast WHERE region_id=:regionId")
    int deleteForecastsById(long regionId);

    @Query("SELECT * FROM daily_forecast WHERE region_id=:regionId")
    Flowable<List<DailyForecast>> loadAllForecastById(long regionId);
}
