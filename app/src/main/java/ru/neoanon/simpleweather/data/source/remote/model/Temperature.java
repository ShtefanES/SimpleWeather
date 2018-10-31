package ru.neoanon.simpleweather.data.source.remote.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by eshtefan on  25.09.2018.
 */

public class Temperature {
    private double day;
    private double night;
    @SerializedName("eve")
    private double evening;
    @SerializedName("morn")
    private double morning;

    public double getDay() {
        return day;
    }

    public void setDay(double day) {
        this.day = day;
    }

    public double getNight() {
        return night;
    }

    public void setNight(double night) {
        this.night = night;
    }

    public double getEvening() {
        return evening;
    }

    public void setEvening(double evening) {
        this.evening = evening;
    }

    public double getMorning() {
        return morning;
    }

    public void setMorning(double morning) {
        this.morning = morning;
    }
}
