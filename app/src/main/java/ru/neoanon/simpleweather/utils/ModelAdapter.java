package ru.neoanon.simpleweather.utils;

import android.app.Application;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import ru.neoanon.simpleweather.R;
import ru.neoanon.simpleweather.data.source.local.db.currentweather.CurrentWeather;
import ru.neoanon.simpleweather.data.source.local.db.dailyforecast.DailyForecast;
import ru.neoanon.simpleweather.data.source.local.db.hourlyforecast.HourlyForecast;
import ru.neoanon.simpleweather.model.CommonHourlyItem;
import ru.neoanon.simpleweather.model.CurrentWeatherItem;
import ru.neoanon.simpleweather.model.DailyForecastItem;
import ru.neoanon.simpleweather.model.DailyForecastShortItem;
import ru.neoanon.simpleweather.model.DateInfo;
import ru.neoanon.simpleweather.model.HeaderHourlyForecastItem;
import ru.neoanon.simpleweather.model.HourlyForecastItem;
import ru.neoanon.simpleweather.model.MyPair;
import ru.neoanon.simpleweather.model.Unit;
import ru.neoanon.simpleweather.model.enumerations.CompassValue;
import ru.neoanon.simpleweather.model.enumerations.PressureType;

/**
 * Created by eshtefan on  11.10.2018.
 */

@Singleton
public class ModelAdapter {
    private Application application;
    private DateHandler dateHandler;
    private TemperatureHandler temperatureHandler;

    @Inject
    public ModelAdapter(Application application, DateHandler dateHandler, TemperatureHandler temperatureHandler) {
        this.application = application;
        this.dateHandler = dateHandler;
        this.temperatureHandler = temperatureHandler;
    }

    public DailyForecastItem getDailyForecastItem(DailyForecast forecast, String pressureType) {
        Context mContext = application.getApplicationContext();
        DailyForecastItem item = new DailyForecastItem();
        String title = dateHandler.getDayOfMonthAndWeek(forecast.getTimestamp());
        item.setTitle(title);
        item.setMorningTemp(temperatureHandler.getTemp(forecast.getTempMorning()));
        item.setDayTemp(temperatureHandler.getTemp(forecast.getTempDay()));
        item.setEveningTemp(temperatureHandler.getTemp(forecast.getTempEvening()));
        item.setNightTemp(temperatureHandler.getTemp(forecast.getTempNight()));
        item.setWeatherIconId(getDailyWeatherIconId(forecast.getIconId()));
        item.setWeatherDescription(forecast.getDescription());
        MyPair<Integer, String> windInfo = getWindInfoWithMiddleIcon(forecast.getWindDirect(), mContext);
        item.setWindDirectIconId(windInfo.first);
        String windDirect = windInfo.second;
        String windDescription = String.format("%s %s", mContext.getString(R.string.m_sec, String.valueOf(forecast.getWindSpeed())), windDirect);
        item.setWindDescription(windDescription);
        int snow = forecast.getSnow();
        int rain = forecast.getRain();
        if (snow == 0 && rain == 0) {
            item.setWithoutPrecipitation(true);
        } else {
            item.setWithoutPrecipitation(false);
            if (snow != 0) {
                String snowStr = mContext.getString(R.string.millimeters_of_precipitation, String.valueOf(snow));
                item.setSnow(snowStr);
            }
            if (rain != 0) {
                String rainStr = mContext.getString(R.string.millimeters_of_precipitation, String.valueOf(rain));
                item.setRain(rainStr);
            }
        }
        item.setPressure(
                (pressureType.equals(PressureType.torr.name())) ?
                        mContext.getString(R.string.pressure_mm_hg, String.valueOf(forecast.getPressure()))
                        :
                        mContext.getString(R.string.pressure_hectopascal, String.valueOf(forecast.getPressure()))
        );
        item.setHumidity(String.format("%s %%", String.valueOf(forecast.getHumidity())));

        return item;
    }

    public CurrentWeatherItem getWeatherItem(CurrentWeather weather) {
        String tempInfo = temperatureHandler.getTemp(weather.getTemp());
        MyPair<Integer, Integer> iconIds = getCurrentWeatherIconId(weather.getIconId());
        int iconId = iconIds.first;
        int backgroundIconId = iconIds.second;
        String description = weather.getDescription();
        return new CurrentWeatherItem(tempInfo, iconId, description, backgroundIconId);
    }

    public List<CommonHourlyItem> getHourlyItems(List<HourlyForecast> forecasts, CurrentWeather currentWeather, Unit unit) {
        List<CommonHourlyItem> commonForecasts = new ArrayList<>();
        if (forecasts.size() == 0) {
            return commonForecasts;
        }

        Context mContext = application.getApplicationContext();
        HeaderHourlyForecastItem header = getHeaderHourlyForecast(currentWeather, unit.getPressureType(), mContext);
        commonForecasts.add(header);
        HourlyForecastItem firstForecast = getHourlyForecastFromWeather(
                currentWeather,
                unit.getTempType(),
                temperatureHandler,
                dateHandler);
        commonForecasts.add(firstForecast);
        for (HourlyForecast forecast : forecasts) {
            HourlyForecastItem item = getHourlyForecast(
                    forecast,
                    unit.getTempType(),
                    temperatureHandler,
                    dateHandler);
            commonForecasts.add(item);
        }
        return commonForecasts;
    }

    public List<DailyForecastShortItem> getDailyShortItems(List<DailyForecast> forecasts, String tempType) {
        List<DailyForecastShortItem> items = new ArrayList<>();
        for (DailyForecast dailyForecast : forecasts) {
            DailyForecastShortItem item = getDailyForecastShortItem(
                    dailyForecast,
                    tempType,
                    dateHandler,
                    temperatureHandler);
            items.add(item);
        }
        return items;
    }

    private DailyForecastShortItem getDailyForecastShortItem(DailyForecast dailyForecast, String tempType, DateHandler dateHandler, TemperatureHandler tempHandler) {
        DailyForecastShortItem item = new DailyForecastShortItem();
        DateInfo dateInfo = dateHandler.getDataInfo(dailyForecast.getTimestamp());
        item.setDayOfMonth(dateInfo.getDayOfMonth());
        item.setDayOfWeek(dateInfo.getDayOfWeek());
        item.setDayOfWeekColorId((dateInfo.isWeekend() ? R.color.colorWeekend : R.color.colorWeekday));
        item.setTemp(tempHandler.getTempWithType(dailyForecast.getTempAverage(), tempType));
        item.setIconId(getDailyWeatherIconId(dailyForecast.getIconId()));
        return item;
    }

    private HeaderHourlyForecastItem getHeaderHourlyForecast(CurrentWeather weather, String pressureType, Context mContext) {
        HeaderHourlyForecastItem item = new HeaderHourlyForecastItem();
        MyPair<Integer, String> windInfo = getWindInfo(weather.getWindDirect(), mContext);
        item.setIconWindDirectId(windInfo.first);
        String windDirect = windInfo.second;
        String windDescription = String.format("%s %s", mContext.getString(R.string.m_sec, String.valueOf(weather.getWindSpeed())), windDirect);
        item.setWindDescription(windDescription);
        item.setPressure(
                (pressureType.equals(PressureType.torr.name())) ?
                        mContext.getString(R.string.pressure_mm_hg, String.valueOf(weather.getPressure()))
                        :
                        mContext.getString(R.string.pressure_hectopascal, String.valueOf(weather.getPressure()))
        );
        item.setHumidity(String.format("%s %%", String.valueOf(weather.getHumidity())));
        return item;
    }

    private HourlyForecastItem getHourlyForecastFromWeather(CurrentWeather weather, String tempType, TemperatureHandler tempHandler, DateHandler dateHandler) {
        String time = dateHandler.getHoursAndMinutes(dateHandler.getCurrentTimestamp());
        String tempInfo = tempHandler.getTempWithType(weather.getTemp(), tempType);
        int iconId = getHourlyIconId(weather.getIconId());
        return new HourlyForecastItem(time, tempInfo, iconId);
    }

    private HourlyForecastItem getHourlyForecast(HourlyForecast forecast, String tempType, TemperatureHandler tempHandler, DateHandler dateHandler) {
        String time = dateHandler.getHoursAndMinutes(forecast.getTimestamp());
        String tempInfo = tempHandler.getTempWithType(forecast.getTemp(), tempType);
        int iconId = getHourlyIconId(forecast.getIconId());
        return new HourlyForecastItem(time, tempInfo, iconId);
    }

    private MyPair<Integer, String> getWindInfo(String wind, Context mContext) {
        MyPair<Integer, String> windInfo = null;
        if (wind.equals(CompassValue.N.name())) {
            windInfo = new MyPair<>(R.drawable.ic_n, mContext.getString(R.string.north));
        } else if (wind.equals(CompassValue.NE.name())) {
            windInfo = new MyPair<>(R.drawable.ic_ne, mContext.getString(R.string.north_east));
        } else if (wind.equals(CompassValue.E.name())) {
            windInfo = new MyPair<>(R.drawable.ic_e, mContext.getString(R.string.east));
        } else if (wind.equals(CompassValue.SE.name())) {
            windInfo = new MyPair<>(R.drawable.ic_se, mContext.getString(R.string.south_east));
        } else if (wind.equals(CompassValue.S.name())) {
            windInfo = new MyPair<>(R.drawable.ic_s, mContext.getString(R.string.south));
        } else if (wind.equals(CompassValue.SW.name())) {
            windInfo = new MyPair<>(R.drawable.ic_sw, mContext.getString(R.string.south_west));
        } else if (wind.equals(CompassValue.W.name())) {
            windInfo = new MyPair<>(R.drawable.ic_w, mContext.getString(R.string.west));
        } else if (wind.equals(CompassValue.NW.name())) {
            windInfo = new MyPair<>(R.drawable.ic_nw, mContext.getString(R.string.north_west));
        } else {
            throw new IllegalArgumentException();
        }
        return windInfo;
    }

    private MyPair<Integer, String> getWindInfoWithMiddleIcon(String wind, Context mContext) {
        MyPair<Integer, String> windInfo = null;
        if (wind.equals(CompassValue.N.name())) {
            windInfo = new MyPair<>(R.drawable.ic_n_middle, mContext.getString(R.string.north));
        } else if (wind.equals(CompassValue.NE.name())) {
            windInfo = new MyPair<>(R.drawable.ic_ne_middle, mContext.getString(R.string.north_east));
        } else if (wind.equals(CompassValue.E.name())) {
            windInfo = new MyPair<>(R.drawable.ic_e_middle, mContext.getString(R.string.east));
        } else if (wind.equals(CompassValue.SE.name())) {
            windInfo = new MyPair<>(R.drawable.ic_se_middle, mContext.getString(R.string.south_east));
        } else if (wind.equals(CompassValue.S.name())) {
            windInfo = new MyPair<>(R.drawable.ic_s_middle, mContext.getString(R.string.south));
        } else if (wind.equals(CompassValue.SW.name())) {
            windInfo = new MyPair<>(R.drawable.ic_sw_middle, mContext.getString(R.string.south_west));
        } else if (wind.equals(CompassValue.W.name())) {
            windInfo = new MyPair<>(R.drawable.ic_w_middle, mContext.getString(R.string.west));
        } else if (wind.equals(CompassValue.NW.name())) {
            windInfo = new MyPair<>(R.drawable.ic_nw_middle, mContext.getString(R.string.north_west));
        } else {
            throw new IllegalArgumentException();
        }
        return windInfo;
    }

    private int getDailyWeatherIconId(String icon) {
        if (icon.equals("03d") || icon.equals("03n")) {
            return R.drawable.d30;
        } else if (icon.equals("04d") || icon.equals("04n")) {
            return R.drawable.d40;
        } else if (icon.equals("09d") || icon.equals("09n")) {
            return R.drawable.d90;
        } else if (icon.equals("11d") || icon.equals("11n")) {
            return R.drawable.d11;
        } else if (icon.equals("13d") || icon.equals("13n")) {
            return R.drawable.d31;
        } else if (icon.equals("50d") || icon.equals("50n")) {
            return R.drawable.d05;
        } else if (icon.equals("01d")) {
            return R.drawable.d10;
        } else if (icon.equals("02d")) {
            return R.drawable.d20;
        } else if (icon.equals("10d")) {
            return R.drawable.d01;
        } else if (icon.equals("01n")) {
            return R.drawable.n10;
        } else if (icon.equals("02n")) {
            return R.drawable.n20;
        } else if (icon.equals("10n")) {
            return R.drawable.n01;
        } else {
            throw new IllegalArgumentException();
        }
    }

    private MyPair<Integer, Integer> getCurrentWeatherIconId(String icon) {
        if (icon.equals("03d") || icon.equals("03n")) {
            return new MyPair<>(R.drawable.big_d30, R.drawable.ic_background_clouds);
        } else if (icon.equals("04d") || icon.equals("04n")) {
            return new MyPair<>(R.drawable.big_d40, R.drawable.ic_background_clouds);
        } else if (icon.equals("09d") || icon.equals("09n")) {
            return new MyPair<>(R.drawable.big_d90, R.drawable.ic_background_shower_rain);
        } else if (icon.equals("11d") || icon.equals("11n")) {
            return new MyPair<>(R.drawable.big_d11, R.drawable.ic_background_shower_rain);
        } else if (icon.equals("13d") || icon.equals("13n")) {
            return new MyPair<>(R.drawable.big_d31, R.drawable.ic_background_snow);
        } else if (icon.equals("50d") || icon.equals("50n")) {
            return new MyPair<>(R.drawable.big_d05, R.drawable.ic_background_clouds);
        } else if (icon.equals("01d")) {
            return new MyPair<>(R.drawable.big_d10, R.drawable.ic_background_clear);
        } else if (icon.equals("02d")) {
            return new MyPair<>(R.drawable.big_d20, R.drawable.ic_background_clear);
        } else if (icon.equals("10d")) {
            return new MyPair<>(R.drawable.big_d01, R.drawable.ic_background_rain);
        } else if (icon.equals("01n")) {
            return new MyPair<>(R.drawable.big_n10, R.drawable.ic_background_clear);
        } else if (icon.equals("02n")) {
            return new MyPair<>(R.drawable.big_n20, R.drawable.ic_background_clear);
        } else if (icon.equals("10n")) {
            return new MyPair<>(R.drawable.big_n01, R.drawable.ic_background_rain);
        } else {
            throw new IllegalArgumentException();
        }
    }

    private int getHourlyIconId(String icon) {
        if (icon.equals("03d") || icon.equals("03n")) {
            return R.drawable.white_d30;
        } else if (icon.equals("04d") || icon.equals("04n")) {
            return R.drawable.white_d40;
        } else if (icon.equals("09d") || icon.equals("09n")) {
            return R.drawable.white_d90;
        } else if (icon.equals("11d") || icon.equals("11n")) {
            return R.drawable.white_d11;
        } else if (icon.equals("13d") || icon.equals("13n")) {
            return R.drawable.white_d31;
        } else if (icon.equals("50d") || icon.equals("50n")) {
            return R.drawable.white_d05;
        } else if (icon.equals("01d")) {
            return R.drawable.white_d10;
        } else if (icon.equals("02d")) {
            return R.drawable.white_d20;
        } else if (icon.equals("10d")) {
            return R.drawable.white_d01;
        } else if (icon.equals("01n")) {
            return R.drawable.white_n10;
        } else if (icon.equals("02n")) {
            return R.drawable.white_n20;
        } else if (icon.equals("10n")) {
            return R.drawable.white_n01;
        } else {
            throw new IllegalArgumentException();
        }
    }

}