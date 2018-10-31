package ru.neoanon.simpleweather.data.source.local.db.currentweather;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by eshtefan on  20.09.2018.
 */

@Dao
public interface CurrentWeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertWeather(CurrentWeather currentWeather);

    @Query("SELECT * FROM current_weather WHERE region_id=:regionId")
    CurrentWeather loadWeather(long regionId);

    @Query("DELETE FROM current_weather")
    void clearAllWeathers();

    @Query("DELETE FROM current_weather WHERE region_id=:regionId")
    int deleteWeatherById(long regionId);

    @Query("SELECT * FROM current_weather WHERE region_id=:regionId")
    Flowable<List<CurrentWeather>> loadWeatherById(long regionId);
}
