package ru.neoanon.simpleweather.model;

/**
 * Created by eshtefan on  12.10.2018.
 */

public class HeaderHourlyForecastItem implements CommonHourlyItem {
    private int iconWindDirectId;
    private String windDescription;
    private String pressure;
    private String humidity;

    public int getIconWindDirectId() {
        return iconWindDirectId;
    }

    public void setIconWindDirectId(int iconWindDirectId) {
        this.iconWindDirectId = iconWindDirectId;
    }

    public String getWindDescription() {
        return windDescription;
    }

    public void setWindDescription(String windDescription) {
        this.windDescription = windDescription;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }
}
