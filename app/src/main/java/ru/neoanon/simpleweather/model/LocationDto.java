package ru.neoanon.simpleweather.model;

import android.arch.persistence.room.Dao;

import com.google.gson.annotations.SerializedName;

/**
 * Created by eshtefan on  19.09.2018.
 */

@Dao
public class LocationDto {
    @SerializedName("lat")
    private double latitude;
    @SerializedName("lon")
    private double longitude;

    public LocationDto(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
