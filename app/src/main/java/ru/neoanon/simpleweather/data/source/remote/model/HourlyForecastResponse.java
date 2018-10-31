package ru.neoanon.simpleweather.data.source.remote.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by eshtefan on  25.09.2018.
 */

public class HourlyForecastResponse {
    @SerializedName("cnt")
    private int amount;
    private List<HourlyForecastApiItem> list;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public List<HourlyForecastApiItem> getList() {
        return list;
    }

    public void setList(List<HourlyForecastApiItem> list) {
        this.list = list;
    }
}