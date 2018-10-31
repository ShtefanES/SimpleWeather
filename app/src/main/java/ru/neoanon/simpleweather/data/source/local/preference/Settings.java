package ru.neoanon.simpleweather.data.source.local.preference;

import android.content.SharedPreferences;

import javax.inject.Inject;

import ru.neoanon.simpleweather.model.Unit;
import ru.neoanon.simpleweather.model.enumerations.PressureType;
import ru.neoanon.simpleweather.model.enumerations.TempType;

/**
 * Created by eshtefan on  20.09.2018.
 */

public class Settings implements ISettings {
    private static final String KEY_LANG = "keyLang";
    private static final String KEY_TEMP = "keyTemp";
    private static final String KEY_PRESSURE = "keyPressure";
    private static final String KEY_DATE_OF_DIRTY_CACHE = "keyDateOfDirtyCache";

    private SharedPreferences sharedPreferences;

    @Inject
    public Settings(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public void saveTempType(String type) {
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.putString(KEY_TEMP, type);
        ed.apply();
    }

    @Override
    public String getTempType() {
        return sharedPreferences.getString(KEY_TEMP, TempType.celsius.name());
    }

    @Override
    public void savePressureType(String pressure) {
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.putString(KEY_PRESSURE, pressure);
        ed.apply();
    }

    @Override
    public String getPressureType() {
        return sharedPreferences.getString(KEY_PRESSURE, PressureType.torr.name());
    }

    @Override
    public void saveDateOfDirtyCache(long timestamp) {
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.putLong(KEY_DATE_OF_DIRTY_CACHE, timestamp);
        ed.apply();
    }

    @Override
    public long getDateOfDirtyCache() {
        return sharedPreferences.getLong(KEY_DATE_OF_DIRTY_CACHE, -1L);
    }

    @Override
    public Unit getUnit() {
        return new Unit(getTempType(), getPressureType());
    }
}
