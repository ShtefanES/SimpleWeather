package ru.neoanon.simpleweather.data.source.remote.model;

/**
 * Created by eshtefan on  19.09.2018.
 */

public class Main {
    private double temp;
    private double pressure;
    private int humidity;

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }
}
