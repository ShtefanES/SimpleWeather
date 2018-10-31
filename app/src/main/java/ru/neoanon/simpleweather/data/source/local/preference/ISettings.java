package ru.neoanon.simpleweather.data.source.local.preference;

import ru.neoanon.simpleweather.model.Unit;

/**
 * Created by eshtefan on  20.09.2018.
 */

public interface ISettings {

    void saveTempType(String type);

    String getTempType();

    void savePressureType(String pressure);

    String getPressureType();

    void saveDateOfDirtyCache(long timestamp);

    long getDateOfDirtyCache();

    Unit getUnit();
}
