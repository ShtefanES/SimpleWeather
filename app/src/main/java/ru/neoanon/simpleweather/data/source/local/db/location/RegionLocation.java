package ru.neoanon.simpleweather.data.source.local.db.location;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import ru.neoanon.simpleweather.model.LocationDto;

/**
 * Created by eshtefan on  20.09.2018.
 */

@Entity(tableName = "region_location")
public class RegionLocation {
    @PrimaryKey(autoGenerate = true)
    private long id;
    public String name;
    private double latitude;
    private double longitude;

    public RegionLocation() {
    }

    public RegionLocation(LocationDto location, String name) {
        this.name = name;
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getName() {
        return name;
    }
}
