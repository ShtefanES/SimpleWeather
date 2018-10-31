package ru.neoanon.simpleweather.utils;

import javax.inject.Inject;

/**
 * Created by eshtefan on  04.10.2018.
 */

public class TemperatureHandler {

    @Inject
    public TemperatureHandler() {
    }

    public String getTempWithType(int temp, String tempType) {
        StringBuilder stringBuilder = new StringBuilder();
        if (temp > 0) {
            stringBuilder.append("+");
        }
        stringBuilder.append(temp);
        stringBuilder.append((tempType.equals(ru.neoanon.simpleweather.model.enumerations.TempType.celsius.name())) ? Constants.DEGREE_CELSIUS : Constants.DEGREE_KELVIN);
        return stringBuilder.toString();
    }

    public String getTemp(int temp) {
        StringBuilder stringBuilder = new StringBuilder();
        if (temp > 0) {
            stringBuilder.append("+");
        }
        stringBuilder.append(temp);
        stringBuilder.append(Constants.DEGREE);
        return stringBuilder.toString();
    }
}
