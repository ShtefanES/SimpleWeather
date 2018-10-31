package ru.neoanon.simpleweather.data.source.remote.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by eshtefan on  25.09.2018.
 */

public class HourlyForecastApiItem {
    @SerializedName("dt")
    private long timestamp;
    private Main main;
    @SerializedName("weather")
    private List<Weather> weathers;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public List<Weather> getWeathers() {
        return weathers;
    }

    public void setWeathers(List<Weather> weathers) {
        this.weathers = weathers;
    }
}