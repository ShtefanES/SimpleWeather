package ru.neoanon.simpleweather.data.source.local.db.hourlyforecast;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by eshtefan on  25.09.2018.
 */

@Entity(tableName = "hourly_forecast")
public class HourlyForecast {
    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(name = "region_id")
    private long regionId;
    private long timestamp;
    private int temp;
    @ColumnInfo(name = "icon_id")
    private String iconId;

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

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public String getIconId() {
        return iconId;
    }

    public void setIconId(String iconId) {
        this.iconId = iconId;
    }
}
