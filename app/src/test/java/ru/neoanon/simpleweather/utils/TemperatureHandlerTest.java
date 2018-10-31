package ru.neoanon.simpleweather.utils;

import org.junit.Before;
import org.junit.Test;

import ru.neoanon.simpleweather.model.enumerations.TempType;

import static org.junit.Assert.assertEquals;

/**
 * Created by eshtefan on  26.10.2018.
 */

public class TemperatureHandlerTest {
    private TemperatureHandler temperatureHandler;

    @Before
    public void initTemperatureHandler() {
        temperatureHandler = new TemperatureHandler();
    }

    @Test
    public void getTempWithType() {
        int tempValue = 15;
        String tempType = TempType.celsius.name();
        String expectedTemp = "+15 \u2103";
        String actualTemp = temperatureHandler.getTempWithType(tempValue, tempType);
        assertEquals(expectedTemp, actualTemp);
    }

    @Test
    public void getTempWithTypeWithNegativeTemp() {
        int tempValue = -177;
        String tempType = TempType.kelvin.name();
        String expectedTemp = "-177 \u00B0K";
        String actualTemp = temperatureHandler.getTempWithType(tempValue, tempType);
        assertEquals(expectedTemp, actualTemp);
    }

    @Test
    public void getTempWithTypeWithNUllTemp() {
        int tempValue = 0;
        String tempType = TempType.kelvin.name();
        String expectedTemp = "0 \u00B0K";
        String actualTemp = temperatureHandler.getTempWithType(tempValue, tempType);
        assertEquals(expectedTemp, actualTemp);
    }

    @Test
    public void getTemp() {
        int tempValue = 15;
        String expectedTemp = "+15\u00B0";
        String actualTemp = temperatureHandler.getTemp(tempValue);
        assertEquals(expectedTemp, actualTemp);
    }

    @Test
    public void getTempWithNegativeTemperature() {
        int tempValue = -35;
        String expectedTemp = "-35\u00B0";
        String actualTemp = temperatureHandler.getTemp(tempValue);
        assertEquals(expectedTemp, actualTemp);
    }

    @Test
    public void getTempWithNullTemperature() {
        int tempValue = 0;
        String expectedTemp = "0\u00B0";
        String actualTemp = temperatureHandler.getTemp(tempValue);
        assertEquals(expectedTemp, actualTemp);
    }
}