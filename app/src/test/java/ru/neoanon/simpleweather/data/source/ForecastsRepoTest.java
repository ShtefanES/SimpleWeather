package ru.neoanon.simpleweather.data.source;


import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import ru.neoanon.simpleweather.RxTestRule;
import ru.neoanon.simpleweather.data.source.local.db.IDbSource;
import ru.neoanon.simpleweather.data.source.local.db.currentweather.CurrentWeather;
import ru.neoanon.simpleweather.data.source.local.db.dailyforecast.DailyForecast;
import ru.neoanon.simpleweather.data.source.local.db.hourlyforecast.HourlyForecast;
import ru.neoanon.simpleweather.data.source.local.db.location.RegionLocation;
import ru.neoanon.simpleweather.data.source.local.preference.ISettings;
import ru.neoanon.simpleweather.data.source.remote.OpenWeatherMap;
import ru.neoanon.simpleweather.data.source.remote.model.CurrentWeatherResponse;
import ru.neoanon.simpleweather.data.source.remote.model.DailyForecastResponse;
import ru.neoanon.simpleweather.data.source.remote.model.HourlyForecastResponse;
import ru.neoanon.simpleweather.model.CommonHourlyItem;
import ru.neoanon.simpleweather.model.CurrentWeatherItem;
import ru.neoanon.simpleweather.model.DailyForecastItem;
import ru.neoanon.simpleweather.model.DailyForecastShortItem;
import ru.neoanon.simpleweather.model.LocationDto;
import ru.neoanon.simpleweather.model.Unit;
import ru.neoanon.simpleweather.model.enumerations.PressureType;
import ru.neoanon.simpleweather.model.enumerations.TempType;
import ru.neoanon.simpleweather.model.enumerations.UnitsType;
import ru.neoanon.simpleweather.utils.Constants;
import ru.neoanon.simpleweather.utils.DateHandler;
import ru.neoanon.simpleweather.utils.ModelAdapter;
import ru.neoanon.simpleweather.utils.ModelTransformer;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by eshtefan on  26.10.2018.
 */

public class ForecastsRepoTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Rule
    public RxTestRule rxTestRule = new RxTestRule();

    @Mock
    OpenWeatherMap owm;
    @Mock
    ISettings iSettings;
    @Mock
    IDbSource iDbSource;
    @Mock
    ModelTransformer modelTransformer;
    @Mock
    DateHandler dateHandler;
    @Mock
    ModelAdapter modelAdapter;

    private Locale locale;
    private Forecasts repo;
    private RegionLocation regionLocation;
    private String regionName = "Moscow";
    private double latitude = 124.57d;
    private double longitude = 177.23d;
    private long regionId = 101L;

    @Before
    public void initRepo() {
        locale = new Locale("en");
        repo = new ForecastsRepo(owm, iDbSource, iSettings, modelTransformer, dateHandler, modelAdapter, locale);
    }

    @Before
    public void initRegionLocation() {
        regionLocation = new RegionLocation(new LocationDto(latitude, longitude), regionName);
        regionLocation.setId(regionId);
    }

    @Test
    public void getRegionLocation() {
        long regionId = 123;
        RegionLocation regionLocation = new RegionLocation();
        regionLocation.setId(regionId);

        when(iDbSource.getLocation(regionId)).thenReturn(regionLocation);

        repo.getRegionLocation(regionId)
                .test()
                .assertValue(regionLocation1 -> regionLocation1.getId() == regionId);

        verify(iDbSource, times(1)).getLocation(regionId);
    }

    @Test
    public void getRegionLocationByName() {
        when(iDbSource.getLocation(regionName)).thenReturn(regionLocation);

        RegionLocation actualRegionLocation = repo.getRegionLocation(regionName);
        assertEquals(actualRegionLocation.getId(), regionLocation.getId());
        verify(iDbSource, times(1)).getLocation(regionName);
    }

    @Test
    public void addRegionLocation() {
        when(iDbSource.saveLocation(regionLocation)).thenReturn(1L);

        repo.addRegionLocation(regionLocation);
        verify(iDbSource, times(1)).saveLocation(regionLocation);
    }

    @Test
    public void updateData() {
        long dateOfDirtyCache = 100L;
        long currentTime = 100500L;
        String currentTempType = TempType.celsius.name();
        String currentPressureType = PressureType.torr.name();
        String currentUnitsType = UnitsType.metric.name();
        int hourlyForecastCnt = 15;
        CurrentWeatherResponse currentWeatherResponse = new CurrentWeatherResponse();
        CurrentWeather currentWeather = new CurrentWeather();
        HourlyForecastResponse hourlyForecastResponse = new HourlyForecastResponse();
        List<HourlyForecast> forecasts = new ArrayList<>();
        DailyForecastResponse dailyForecastResponse = new DailyForecastResponse();
        List<DailyForecast> dailyForecasts = new ArrayList<>();

        when(iSettings.getDateOfDirtyCache()).thenReturn(dateOfDirtyCache);
        when(dateHandler.getCurrentTimestamp()).thenReturn(currentTime);
        when(iSettings.getTempType()).thenReturn(currentTempType);
        when(iSettings.getPressureType()).thenReturn(currentPressureType);

        when(owm.getCurrentWeather(latitude, longitude, Constants.OWM_API_KEY, currentUnitsType, Constants.TYPE_OF_ACCURACY, locale.getLanguage())).thenReturn(Observable.just(currentWeatherResponse));
        when(modelTransformer.weatherApiToWeatherDb(currentWeatherResponse, currentPressureType, regionLocation.getId())).thenReturn(currentWeather);
        when(iDbSource.saveWeather(currentWeather)).thenReturn(1L);

        when(dateHandler.getCurrentHour()).thenReturn(0);
        when(dateHandler.getLocalOffset()).thenReturn(0);
        when(owm.getHourlyForecast(latitude, longitude, Constants.OWM_API_KEY, currentUnitsType, Constants.TYPE_OF_ACCURACY, locale.getLanguage(), hourlyForecastCnt)).thenReturn(Observable.just(hourlyForecastResponse));
        when(modelTransformer.forecastApiToHourlyForecastDb(hourlyForecastResponse, regionLocation.getId())).thenReturn(forecasts);
        when(iDbSource.deleteHourlyForecasts(regionLocation.getId())).thenReturn(1);
        doNothing().when(iDbSource).saveHourlyForecasts(forecasts);

        when(owm.getDailyForecast(latitude, longitude, Constants.OWM_API_KEY, currentUnitsType, Constants.TYPE_OF_ACCURACY, locale.getLanguage(), Constants.NUMBER_OF_DAILY_FORECASTS)).thenReturn(Observable.just(dailyForecastResponse));
        when(modelTransformer.forecastApiToDailyForecastDb(dailyForecastResponse, currentPressureType, regionLocation.getId())).thenReturn(dailyForecasts);
        when(iDbSource.deleteDailyForecasts(regionLocation.getId())).thenReturn(1);
        doNothing().when(iDbSource).saveDailyForecasts(dailyForecasts);
        doNothing().when(iSettings).saveDateOfDirtyCache(any(Long.class));

        repo.updateData(regionLocation)
                .test()
                .assertValue(regionLocation1 -> regionLocation1.getName().equals(regionName) && regionLocation1.getLatitude() == latitude && regionLocation1.getLongitude() == longitude);
    }

    @Test
    public void updateDataWhenWhenEarlyToUpdate() {
        long dateOfDirtyCache = 100500L;
        long currentTime = 100L;
        when(iSettings.getDateOfDirtyCache()).thenReturn(dateOfDirtyCache);
        when(dateHandler.getCurrentTimestamp()).thenReturn(currentTime);

        repo.updateData(regionLocation)
                .test()
                .assertComplete();

        verify(iSettings, times(1)).getDateOfDirtyCache();
        verify(dateHandler, times(1)).getCurrentTimestamp();
    }

    @Test
    public void getAllRegionLocations() {
        List<RegionLocation> locations = new ArrayList<>();
        when(iDbSource.loadAllLocations()).thenReturn(Flowable.just(locations));
        repo.getAllRegionLocations()
                .test()
                .assertComplete();

        verify(iDbSource, times(1)).loadAllLocations();
    }

    @Test
    public void deleteAllInfoForThisRegion() {
        when(iDbSource.deleteWeather(regionId)).thenReturn(1);
        when(iDbSource.deleteDailyForecasts(regionId)).thenReturn(1);
        when(iDbSource.deleteHourlyForecasts(regionId)).thenReturn(1);

        repo.deleteAllInfoForThisRegion(regionId)
                .test()
                .assertComplete();

        verify(iDbSource, times(1)).deleteWeather(regionId);
        verify(iDbSource, times(1)).deleteDailyForecasts(regionId);
        verify(iDbSource, times(1)).deleteHourlyForecasts(regionId);
    }

    @Test
    public void getAllShortDailyForecasts() {
        DailyForecastShortItem item = new DailyForecastShortItem();
        List<DailyForecastShortItem> forecastShortItems = new ArrayList<>();
        DailyForecast dailyForecast = new DailyForecast();
        List<DailyForecast> dailyForecasts = new ArrayList<>();
        dailyForecasts.add(dailyForecast);
        forecastShortItems.add(item);
        String tempType = TempType.celsius.name();

        when(iDbSource.loadAllDailyForecastForRegion(regionId)).thenReturn(Flowable.just(dailyForecasts));
        when(iSettings.getTempType()).thenReturn(tempType);
        when(modelAdapter.getDailyShortItems(dailyForecasts, tempType)).thenReturn(forecastShortItems);

        repo.getAllShortDailyForecasts(regionId)
                .test()
                .assertValue(dailyForecastShortItems -> dailyForecastShortItems.size() == forecastShortItems.size());
    }

    @Test
    public void getAllShortDailyForecastsWhenEmpty() {
        List<DailyForecast> dailyForecasts = new ArrayList<>();

        when(iDbSource.loadAllDailyForecastForRegion(regionId)).thenReturn(Flowable.just(dailyForecasts));

        repo.getAllShortDailyForecasts(regionId)
                .test()
                .assertValue(dailyForecastShortItems -> dailyForecastShortItems.size() == 0);
    }

    @Test
    public void getAllCommonHourlyForecasts() {
        HourlyForecast hourlyForecast = new HourlyForecast();
        List<HourlyForecast> hourlyForecasts = new ArrayList<>();
        hourlyForecasts.add(hourlyForecast);
        CurrentWeather currentWeather = new CurrentWeather();
        Unit unit = new Unit();
        List<CommonHourlyItem> items = new ArrayList<>();
        CommonHourlyItem item = new CommonHourlyItem() {
        };
        items.add(item);

        when(iDbSource.loadAllHourlyForecastForRegion(regionId)).thenReturn(Flowable.just(hourlyForecasts));
        when(iDbSource.getWeather(regionId)).thenReturn(currentWeather);
        when(iSettings.getUnit()).thenReturn(unit);
        when(modelAdapter.getHourlyItems(hourlyForecasts, currentWeather, unit)).thenReturn(items);

        repo.getAllCommonHourlyForecasts(regionId)
                .test()
                .assertValue(hourlyItems -> hourlyItems.size() == items.size());
    }

    @Test
    public void getAllCommonHourlyForecastsWhenEmpty() {
        List<HourlyForecast> hourlyForecasts = new ArrayList<>();

        when(iDbSource.loadAllHourlyForecastForRegion(regionId)).thenReturn(Flowable.just(hourlyForecasts));

        repo.getAllCommonHourlyForecasts(regionId)
                .test()
                .assertValue(items -> items.size() == 0);
    }

    @Test
    public void getCurrentWeather() {
        int iconId = 100;
        CurrentWeather currentWeather = new CurrentWeather();
        List<CurrentWeather> currentWeathers = new ArrayList<>();
        currentWeathers.add(currentWeather);
        CurrentWeatherItem currentWeatherItem = new CurrentWeatherItem("", iconId, "", 0);

        when(iDbSource.loadWeatherById(regionId)).thenReturn(Flowable.just(currentWeathers));
        when(modelAdapter.getWeatherItem(currentWeather)).thenReturn(currentWeatherItem);

        repo.getCurrentWeather(regionId)
                .test()
                .assertValue(item -> item.getIconId() == iconId);
    }

    @Test
    public void getCurrentWeatherWhenEmpty() {
        List<CurrentWeather> currentWeathers = new ArrayList<>();

        when(iDbSource.loadWeatherById(regionId)).thenReturn(Flowable.just(currentWeathers));

        repo.getCurrentWeather(regionId)
                .test()
                .assertValue(item -> item.getIconId() == 0);
    }

    @Test
    public void getAllDailyForecasts() {
        DailyForecast dailyForecast = new DailyForecast();
        List<DailyForecast> dailyForecasts = new ArrayList<>();
        dailyForecasts.add(dailyForecast);
        String pressureType = PressureType.torr.name();
        DailyForecastItem dailyForecastItem = new DailyForecastItem();

        when(iDbSource.loadAllDailyForecasts(regionId)).thenReturn(dailyForecasts);
        when(iSettings.getPressureType()).thenReturn(pressureType);
        when(modelAdapter.getDailyForecastItem(dailyForecast, pressureType)).thenReturn(dailyForecastItem);

        repo.getAllDailyForecasts(regionId)
                .test()
                .assertValue(items -> items.get(0).equals(dailyForecastItem));
    }

    @Test
    public void getUnits() {
        Unit unit = new Unit();
        when(iSettings.getUnit()).thenReturn(unit);

        repo.getUnits()
                .test()
                .assertValue(unit1 -> unit1.equals(unit));

        verify(iSettings, times(1)).getUnit();
    }

    @Test
    public void setUnits() {
        String tempType = TempType.celsius.name();
        String pressureType = PressureType.torr.name();
        Unit unit = new Unit(tempType, pressureType);
        doNothing().when(iSettings).saveTempType(tempType);
        doNothing().when(iSettings).savePressureType(pressureType);

        repo.setUnits(unit);

        verify(iSettings, times(1)).saveTempType(tempType);
        verify(iSettings, times(1)).savePressureType(pressureType);
    }

    @Test
    public void deleteAllForecasts() {
        doNothing().when(iDbSource).deleteAllWeathers();
        doNothing().when(iDbSource).deleteAllDailyForecasts();
        doNothing().when(iDbSource).deleteAllHourlyForecasts();

        repo.deleteAllForecasts();

        verify(iDbSource, times(1)).deleteAllWeathers();
        verify(iDbSource, times(1)).deleteAllDailyForecasts();
        verify(iDbSource, times(1)).deleteAllHourlyForecasts();
    }

    @Test
    public void makeCacheDirty() {
        long currentTime = 100500;
        long dateOfDirtyCache = 100440;
        when(dateHandler.getCurrentTimestamp()).thenReturn(currentTime);
        doNothing().when(iSettings).saveDateOfDirtyCache(dateOfDirtyCache);

        repo.makeCacheDirty();

        verify(dateHandler, times(1)).getCurrentTimestamp();
        verify(iSettings, times(1)).saveDateOfDirtyCache(dateOfDirtyCache);
    }
}