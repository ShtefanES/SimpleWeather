package ru.neoanon.simpleweather.data.source.local.db.currentweather;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by eshtefan on  20.09.2018.
 */

@Entity(tableName = "current_weather")
public class CurrentWeather {
    @PrimaryKey
    @ColumnInfo(name = "region_id")
    private long regionId;
    @ColumnInfo(name = "icon_id")
    private String iconId;
    @ColumnInfo(name = "wind_speed")
    private int windSpeed;
    @ColumnInfo(name = "wind_direct")
    private String windDirect;
    private int temp;
    private int pressure;
    private int humidity;
    private String description;

    public long getRegionId() {
        return regionId;
    }

    public void setRegionId(long regionId) {
        this.regionId = regionId;
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

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
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
}
