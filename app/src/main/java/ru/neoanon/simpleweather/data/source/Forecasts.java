package ru.neoanon.simpleweather.data.source;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import ru.neoanon.simpleweather.data.source.local.db.location.RegionLocation;
import ru.neoanon.simpleweather.model.CommonHourlyItem;
import ru.neoanon.simpleweather.model.CurrentWeatherItem;
import ru.neoanon.simpleweather.model.DailyForecastItem;
import ru.neoanon.simpleweather.model.DailyForecastShortItem;
import ru.neoanon.simpleweather.model.Unit;

/**
 * Created by eshtefan on  19.09.2018.
 */

public interface Forecasts {

    Observable<RegionLocation> getRegionLocation(long regionId);

    RegionLocation getRegionLocation(String regionName);

    long addRegionLocation(RegionLocation regionLocation);

    Observable<RegionLocation> updateData(RegionLocation regionLocation);

    Flowable<List<RegionLocation>> getAllRegionLocations();

    Completable deleteAllInfoForThisRegion(long id);

    Flowable<List<DailyForecastShortItem>> getAllShortDailyForecasts(long regionId);

    Flowable<List<CommonHourlyItem>> getAllCommonHourlyForecasts(long regionId);

    Flowable<CurrentWeatherItem> getCurrentWeather(long regionId);

    Observable<List<DailyForecastItem>> getAllDailyForecasts(long regionId);

    Observable<Unit> getUnits();

    void setUnits(Unit unit);

    void deleteAllForecasts();

    void makeCacheDirty();
}
