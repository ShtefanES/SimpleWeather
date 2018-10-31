package ru.neoanon.simpleweather.data.source.local.db.dailyforecast;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;


/**
 * Created by eshtefan on  25.09.2018.
 */

@Entity(tableName = "daily_forecast")
public class DailyForecast {
    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(name = "region_id")
    private long regionId;
    private long timestamp;
    @ColumnInfo(name = "temp_average")
    private int tempAverage;
    @ColumnInfo(name = "temp_day")
    private int tempDay;
    @ColumnInfo(name = "temp_night")
    private int tempNight;
    @ColumnInfo(name = "temp_evening")
    private int tempEvening;
    @ColumnInfo(name = "temp_morning")
    private int tempMorning;
    private String iconId;
    private String description;
    @ColumnInfo(name = "wind_speed")
    private int windSpeed;
    @ColumnInfo(name = "wind_direct")
    private String windDirect;
    private int pressure;
    private int humidity;
    private int snow;
    private int rain;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getRegionId() {
        return regionId;
    }

    public void setRegionId(long regionId) {
        this.regionId = regionId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getTempAverage() {
        return tempAverage;
    }

    public void setTempAverage(int tempAverage) {
        this.tempAverage = tempAverage;
    }

    public int getTempDay() {
        return tempDay;
    }

    public void setTempDay(int tempDay) {
        this.tempDay = tempDay;
    }

    public int getTempNight() {
        return tempNight;
    }

    public void setTempNight(int tempNight) {
        this.tempNight = tempNight;
    }

    public int getTempEvening() {
        return tempEvening;
    }

    public void setTempEvening(int tempEvening) {
        this.tempEvening = tempEvening;
    }

    public int getTempMorning() {
        return tempMorning;
    }

    public void setTempMorning(int tempMorning) {
        this.tempMorning = tempMorning;
    }

    public String getIconId() {
        return iconId;
    }

    public void setIconId(String iconId) {
        this.iconId = iconId;
    }

    public int getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(int windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getWindDirect() {
        return windDirect;
    }

    public void setWindDirect(String windDirect) {
        this.windDirect = windDirect;
    }

    public int getPressure() {
        return pressure;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSnow() {
        return snow;
    }

    public void setSnow(int snow) {
        this.snow = snow;
    }

    public int getRain() {
        return rain;
    }

    public void setRain(int rain) {
        this.rain = rain;
    }
}
