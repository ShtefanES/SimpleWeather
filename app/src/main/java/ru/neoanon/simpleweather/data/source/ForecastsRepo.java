package ru.neoanon.simpleweather.data.source;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import ru.neoanon.simpleweather.data.source.local.db.IDbSource;
import ru.neoanon.simpleweather.data.source.local.db.currentweather.CurrentWeather;
import ru.neoanon.simpleweather.data.source.local.db.dailyforecast.DailyForecast;
import ru.neoanon.simpleweather.data.source.local.db.hourlyforecast.HourlyForecast;
import ru.neoanon.simpleweather.data.source.local.db.location.RegionLocation;
import ru.neoanon.simpleweather.data.source.local.preference.ISettings;
import ru.neoanon.simpleweather.data.source.remote.OpenWeatherMap;
import ru.neoanon.simpleweather.model.CommonHourlyItem;
import ru.neoanon.simpleweather.model.CurrentWeatherItem;
import ru.neoanon.simpleweather.model.DailyForecastItem;
import ru.neoanon.simpleweather.model.DailyForecastShortItem;
import ru.neoanon.simpleweather.model.Unit;
import ru.neoanon.simpleweather.model.enumerations.TempType;
import ru.neoanon.simpleweather.model.enumerations.UnitsType;
import ru.neoanon.simpleweather.utils.Constants;
import ru.neoanon.simpleweather.utils.DateHandler;
import ru.neoanon.simpleweather.utils.HourlyForecastHandler;
import ru.neoanon.simpleweather.utils.ModelAdapter;
import ru.neoanon.simpleweather.utils.ModelTransformer;

/**
 * Created by eshtefan on  19.09.2018.
 */

public class ForecastsRepo implements Forecasts {
    private OpenWeatherMap owm;
    private ISettings iSettings;
    private IDbSource iDbSource;
    private ModelTransformer modelTransformer;
    private DateHandler dateHandler;
    private ModelAdapter modelAdapter;
    private Locale locale;

    @Inject
    public ForecastsRepo(OpenWeatherMap owm, IDbSource iDbSource, ISettings iSettings, ModelTransformer modelTransformer, DateHandler dateHandler, ModelAdapter modelAdapter, Locale locale) {
        this.owm = owm;
        this.iDbSource = iDbSource;
        this.iSettings = iSettings;
        this.modelTransformer = modelTransformer;
        this.dateHandler = dateHandler;
        this.modelAdapter = modelAdapter;
        this.locale = locale;
    }

    @Override
    public Observable<RegionLocation> getRegionLocation(long regionId) {
        return Observable.fromCallable(() -> iDbSource.getLocation(regionId));
    }

    @Override
    public RegionLocation getRegionLocation(String regionName) {
        return iDbSource.getLocation(regionName);
    }

    @Override
    public long addRegionLocation(RegionLocation regionLocation) {
        return iDbSource.saveLocation(regionLocation);
    }

    @Override
    public Observable<RegionLocation> updateData(RegionLocation regionLocation) {
        return Observable.fromCallable(() ->
                iSettings.getDateOfDirtyCache())
                .filter(aLong -> ((aLong == -1L) || (aLong < dateHandler.getCurrentTimestamp())))
                .flatMap(aLong -> owm.getCurrentWeather(
                        regionLocation.getLatitude(),
                        regionLocation.getLongitude(),
                        Constants.OWM_API_KEY,
                        (iSettings.getTempType().equals(TempType.kelvin.name()) ? UnitsType.standard.name() : UnitsType.metric.name()),
                        Constants.TYPE_OF_ACCURACY,
                        locale.getLanguage()))
                .map(currentWeatherResponse -> {
                    CurrentWeather currentWeather = modelTransformer.weatherApiToWeatherDb(currentWeatherResponse, iSettings.getPressureType(), regionLocation.getId());
                    return iDbSource.saveWeather(currentWeather);
                })
                .flatMap(aLong -> owm.getHourlyForecast(
                        regionLocation.getLatitude(),
                        regionLocation.getLongitude(),
                        Constants.OWM_API_KEY,
                        (iSettings.getTempType().equals(TempType.kelvin.name()) ? UnitsType.standard.name() : UnitsType.metric.name()),
                        Constants.TYPE_OF_ACCURACY,
                        locale.getLanguage(),
                        HourlyForecastHandler.getNumberOfHourlyForecast(dateHandler.getCurrentHour(), dateHandler.getLocalOffset())))
                .map(hourlyForecastResponse -> {
                    List<HourlyForecast> forecasts = modelTransformer.forecastApiToHourlyForecastDb(hourlyForecastResponse, regionLocation.getId());
                    iDbSource.deleteHourlyForecasts(regionLocation.getId());
                    iDbSource.saveHourlyForecasts(forecasts);
                    return 1;
                })
                .flatMap(aLong -> owm.getDailyForecast(
                        regionLocation.getLatitude(),
                        regionLocation.getLongitude(),
                        Constants.OWM_API_KEY,
                        (iSettings.getTempType().equals(TempType.kelvin.name()) ? UnitsType.standard.name() : UnitsType.metric.name()),
                        Constants.TYPE_OF_ACCURACY,
                        locale.getLanguage(),
                        Constants.NUMBER_OF_DAILY_FORECASTS))
                .map(dailyForecastResponse -> {
                    List<DailyForecast> forecasts = modelTransformer.forecastApiToDailyForecastDb(dailyForecastResponse, iSettings.getPressureType(), regionLocation.getId());
                    iDbSource.deleteDailyForecasts(regionLocation.getId());
                    iDbSource.saveDailyForecasts(forecasts);
                    long dateOfDirtyCache = dateHandler.getCurrentTimestamp() + Constants.CACHE_LIFETIME;
                    iSettings.saveDateOfDirtyCache(dateOfDirtyCache);
                    return regionLocation;
                });
    }

    @Override
    public Flowable<List<RegionLocation>> getAllRegionLocations() {
        return iDbSource.loadAllLocations();
    }

    @Override
    public Completable deleteAllInfoForThisRegion(long id) {
        return Observable.fromCallable(() -> {
            iDbSource.deleteLocation(id);
            iDbSource.deleteWeather(id);
            iDbSource.deleteDailyForecasts(id);
            return iDbSource.deleteHourlyForecasts(id);
        })
                .ignoreElements();
    }

    @Override
    public Flowable<List<DailyForecastShortItem>> getAllShortDailyForecasts(long regionId) {
        return iDbSource.loadAllDailyForecastForRegion(regionId)
                .flatMap(dailyForecasts -> {
                    if (dailyForecasts.size() == 0) {
                        List<DailyForecastShortItem> items = new ArrayList<>();
                        return Flowable.just(items);
                    }
                    String tempType = iSettings.getTempType();
                    return Flowable.just(modelAdapter.getDailyShortItems(dailyForecasts, tempType));
                });
    }

    @Override
    public Flowable<List<CommonHourlyItem>> getAllCommonHourlyForecasts(long regionId) {
        return iDbSource.loadAllHourlyForecastForRegion(regionId)
                .flatMap(hourlyForecasts -> {
                    if (hourlyForecasts.size() == 0) {
                        List<CommonHourlyItem> items = new ArrayList<>();
                        return Flowable.just(items);
                    }
                    CurrentWeather currentWeather = iDbSource.getWeather(regionId);
                    Unit unit = iSettings.getUnit();
                    return Flowable.just(modelAdapter.getHourlyItems(hourlyForecasts, currentWeather, unit));
                });
    }

    @Override
    public Flowable<CurrentWeatherItem> getCurrentWeather(long regionId) {
        return iDbSource.loadWeatherById(regionId)
                .flatMap(currentWeathers -> {
                    if (currentWeathers.size() == 0) {
                        return Flowable.just(new CurrentWeatherItem("", 0, "", 0));
                    }
                    CurrentWeather weather = currentWeathers.get(0);
                    return Flowable.just(modelAdapter.getWeatherItem(weather));
                });
    }

    @Override
    public Observable<List<DailyForecastItem>> getAllDailyForecasts(long regionId) {
        return Observable.fromCallable(() -> {
            List<DailyForecastItem> resultList = new ArrayList<>();
            List<DailyForecast> dailyForecasts = iDbSource.loadAllDailyForecasts(regionId);
            String pressureType = iSettings.getPressureType();
            for (DailyForecast forecast : dailyForecasts) {
                resultList.add(modelAdapter.getDailyForecastItem(forecast, pressureType));
            }
            return resultList;
        });
    }

    @Override
    public Observable<Unit> getUnits() {
        return Observable.fromCallable(() -> iSettings.getUnit());
    }

    @Override
    public void setUnits(Unit unit) {
        iSettings.saveTempType(unit.getTempType());
        iSettings.savePressureType(unit.getPressureType());
    }

    @Override
    public void deleteAllForecasts() {
        iDbSource.deleteAllWeathers();
        iDbSource.deleteAllDailyForecasts();
        iDbSource.deleteAllHourlyForecasts();
    }

    @Override
    public void makeCacheDirty() {
        iSettings.saveDateOfDirtyCache(dateHandler.getCurrentTimestamp() - 60);
    }
}
