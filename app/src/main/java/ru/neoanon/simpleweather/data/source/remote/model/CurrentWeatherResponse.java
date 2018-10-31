package ru.neoanon.simpleweather.data.source.remote.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by eshtefan on  19.09.2018.
 */

public class CurrentWeatherResponse {
    @SerializedName("weather")
    private List<Weather> weathers;
    private Main main;
    private Wind wind;

    public List<Weather> getWeathers() {
        return weathers;
    }

    public void setWeathers(List<Weather> weathers) {
        this.weathers = weathers;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }
}
