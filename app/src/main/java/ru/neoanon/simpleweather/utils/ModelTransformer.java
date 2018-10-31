package ru.neoanon.simpleweather.utils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import ru.neoanon.simpleweather.data.source.local.db.currentweather.CurrentWeather;
import ru.neoanon.simpleweather.data.source.local.db.dailyforecast.DailyForecast;
import ru.neoanon.simpleweather.data.source.local.db.hourlyforecast.HourlyForecast;
import ru.neoanon.simpleweather.data.source.remote.model.CurrentWeatherResponse;
import ru.neoanon.simpleweather.data.source.remote.model.DailyForecastApiItem;
import ru.neoanon.simpleweather.data.source.remote.model.DailyForecastResponse;
import ru.neoanon.simpleweather.data.source.remote.model.HourlyForecastApiItem;
import ru.neoanon.simpleweather.data.source.remote.model.HourlyForecastResponse;
import ru.neoanon.simpleweather.data.source.remote.model.Temperature;
import ru.neoanon.simpleweather.data.source.remote.model.Weather;
import ru.neoanon.simpleweather.model.enumerations.CompassValue;
import ru.neoanon.simpleweather.model.enumerations.PressureType;

/**
 * Created by eshtefan on  24.09.2018.
 */
@Singleton
public class ModelTransformer {
    private static final double HECTO_PASCAL_FACTOR = 0.750064;// 1 hPa = 0.75006375541921 mmHg

    @Inject
    public ModelTransformer() {
    }

    public CurrentWeather weatherApiToWeatherDb(CurrentWeatherResponse response, String pressureType, long regionId) {
        CurrentWeather weather = new CurrentWeather();
        Weather weatherDescription = response.getWeathers().get(0);
        weather.setHumidity(response.getMain().getHumidity());
        weather.setIconId(weatherDescription.getIcon());
        weather.setDescription(capitalizeFirstLater(weatherDescription.getDescription()));
        double pressure = response.getMain().getPressure();
        weather.setPressure((pressureType.equals(PressureType.hectoPascal.name()) ? (int) Math.round(pressure) : convertToTorr(pressure)));
        weather.setRegionId(regionId);
        weather.setTemp(round(response.getMain().getTemp()));
        weather.setWindDirect(convertToDirection(response.getWind().getDeg()));
        weather.setWindSpeed((int) Math.round(response.getWind().getSpeed()));
        return weather;
    }

    public List<HourlyForecast> forecastApiToHourlyForecastDb(HourlyForecastResponse response, long regionId) {
        List<HourlyForecast> forecastsResult = new ArrayList<>();
        List<HourlyForecastApiItem> forecastItems = response.getList();
        for (HourlyForecastApiItem item : forecastItems) {
            HourlyForecast forecast = new HourlyForecast();
            forecast.setIconId(item.getWeathers().get(0).getIcon());
            forecast.setRegionId(regionId);
            forecast.setTemp(round(item.getMain().getTemp()));
            forecast.setTimestamp(convertSecToMsec(item.getTimestamp()));
            forecastsResult.add(forecast);
        }
        return forecastsResult;
    }

    public List<DailyForecast> forecastApiToDailyForecastDb(DailyForecastResponse response, String pressureType, long regionId) {
        List<DailyForecast> forecastsResult = new ArrayList<>();
        List<DailyForecastApiItem> forecastItems = response.getList();
        for (DailyForecastApiItem item : forecastItems) {
            DailyForecast forecast = new DailyForecast();
            forecast.setWindSpeed((int) Math.round(item.getSpeed()));
            forecast.setWindDirect(convertToDirection(item.getDeg()));
            forecast.setTimestamp(convertSecToMsec(item.getTimestamp()));
            forecast.setTempEvening(round(item.getTemp().getEvening()));
            forecast.setTempMorning(round(item.getTemp().getMorning()));
            forecast.setTempNight(round(item.getTemp().getNight()));
            forecast.setTempDay(round(item.getTemp().getDay()));
            forecast.setTempAverage(calculateAverageTemp(item.getTemp()));
            forecast.setRegionId(regionId);
            double pressure = item.getPressure();
            forecast.setPressure((pressureType.equals(PressureType.hectoPascal.name()) ? (int) Math.round(pressure) : convertToTorr(pressure)));
            forecast.setHumidity(item.getHumidity());
            forecast.setIconId(item.getWeathers().get(0).getIcon());
            forecast.setDescription(capitalizeFirstLater(item.getWeathers().get(0).getDescription()));
            forecastsResult.add(forecast);
            forecast.setSnow((int) Math.round(item.getSnow()));
            forecast.setRain((int) Math.round(item.getRain()));
        }
        return forecastsResult;
    }

    private int convertToTorr(double hectoPascal) {
        return (int) Math.round(hectoPascal * HECTO_PASCAL_FACTOR);
    }

    private String convertToDirection(double deg) {
        String result;
        if (deg == 0.0 || deg == 360.0) {
            result = CompassValue.N.name();
        } else if (deg > 0.0 && deg < 90.0) {
            result = CompassValue.NE.name();
        } else if (deg == 90.0) {
            result = CompassValue.E.name();
        } else if (deg > 90.0 && deg < 180.0) {
            result = CompassValue.SE.name();
        } else if (deg == 180.0) {
            result = CompassValue.S.name();
        } else if (deg > 180.0 && deg < 270.0) {
            result = CompassValue.SW.name();
        } else if (deg == 270.0) {
            result = CompassValue.W.name();
        } else {
            result = CompassValue.NW.name();
        }
        return result;
    }

    private int calculateAverageTemp(Temperature temp) {
        double average = (temp.getDay() + temp.getNight() + temp.getEvening() + temp.getMorning()) / 4;
        return (int) Math.round(average);
    }

    private int round(double d) {
        return (int) Math.round(d);
    }

    private long convertSecToMsec(long sec) {
        return sec * 1000;
    }

    private String capitalizeFirstLater(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
