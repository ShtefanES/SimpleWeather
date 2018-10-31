package ru.neoanon.simpleweather.data.source.remote;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.neoanon.simpleweather.data.source.remote.model.CurrentWeatherResponse;
import ru.neoanon.simpleweather.data.source.remote.model.DailyForecastResponse;
import ru.neoanon.simpleweather.data.source.remote.model.HourlyForecastResponse;

/**
 * Created by eshtefan on  19.09.2018.
 */

public interface OpenWeatherMap {

    @GET("data/2.5/weather")
    Observable<CurrentWeatherResponse> getCurrentWeather(@Query("lat") double lat,
                                                         @Query("lon") double lon,
                                                         @Query("appid") String appId,
                                                         @Query("units") String units,
                                                         @Query("type") String type,
                                                         @Query("lang") String lang);

    @GET("data/2.5/forecast")
    Observable<HourlyForecastResponse> getHourlyForecast(@Query("lat") double lat,
                                                         @Query("lon") double lon,
                                                         @Query("appid") String appId,
                                                         @Query("units") String units,
                                                         @Query("type") String type,
                                                         @Query("lang") String lang,
                                                         @Query("cnt") int cnt);

    @GET("data/2.5/forecast/daily")
    Observable<DailyForecastResponse> getDailyForecast(@Query("lat") double lat,
                                                       @Query("lon") double lon,
                                                       @Query("appid") String appId,
                                                       @Query("units") String units,
                                                       @Query("type") String type,
                                                       @Query("lang") String lang,
                                                       @Query("cnt") int cnt);
}