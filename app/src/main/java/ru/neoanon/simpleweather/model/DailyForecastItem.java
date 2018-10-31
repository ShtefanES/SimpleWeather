package ru.neoanon.simpleweather.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by eshtefan on  16.10.2018.
 */

public class DailyForecastItem implements Parcelable {
    private String title;
    private String morningTemp;
    private String dayTemp;
    private String eveningTemp;
    private String nightTemp;
    private int weatherIconId;
    private String weatherDescription;
    private int windDirectIconId;
    private String windDescription;
    private String snow;
    private String rain;
    private boolean isWithoutPrecipitation;
    private String pressure;
    private String humidity;


    public DailyForecastItem() {
    }

    protected DailyForecastItem(Parcel in) {
        title = in.readString();
        morningTemp = in.readString();
        dayTemp = in.readString();
        eveningTemp = in.readString();
        nightTemp = in.readString();
        weatherIconId = in.readInt();
        weatherDescription = in.readString();
        windDirectIconId = in.readInt();
        windDescription = in.readString();
        snow = in.readString();
        rain = in.readString();
        isWithoutPrecipitation = in.readByte() != 0;
        pressure = in.readString();
        humidity = in.readString();
    }

    public static final Creator<DailyForecastItem> CREATOR = new Creator<DailyForecastItem>() {
        @Override
        public DailyForecastItem createFromParcel(Parcel in) {
            return new DailyForecastItem(in);
        }

        @Override
        public DailyForecastItem[] newArray(int size) {
            return new DailyForecastItem[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMorningTemp() {
        return morningTemp;
    }

    public void setMorningTemp(String morningTemp) {
        this.morningTemp = morningTemp;
    }

    public String getDayTemp() {
        return dayTemp;
    }

    public void setDayTemp(String dayTemp) {
        this.dayTemp = dayTemp;
    }

    public String getEveningTemp() {
        return eveningTemp;
    }

    public void setEveningTemp(String eveningTemp) {
        this.eveningTemp = eveningTemp;
    }

    public String getNightTemp() {
        return nightTemp;
    }

    public void setNightTemp(String nightTemp) {
        this.nightTemp = nightTemp;
    }

    public int getWeatherIconId() {
        return weatherIconId;
    }

    public void setWeatherIconId(int weatherIconId) {
        this.weatherIconId = weatherIconId;
    }

    public String getWeatherDescription() {
        return weatherDescription;
    }

    public void setWeatherDescription(String weatherDescription) {
        this.weatherDescription = weatherDescription;
    }

    public int getWindDirectIconId() {
        return windDirectIconId;
    }

    public void setWindDirectIconId(int windDirectIconId) {
        this.windDirectIconId = windDirectIconId;
    }

    public String getWindDescription() {
        return windDescription;
    }

    public void setWindDescription(String windDescription) {
        this.windDescription = windDescription;
    }

    public String getSnow() {
        return snow;
    }

    public void setSnow(String snow) {
        this.snow = snow;
    }

    public String getRain() {
        return rain;
    }

    public void setRain(String rain) {
        this.rain = rain;
    }

    public boolean isWithoutPrecipitation() {
        return isWithoutPrecipitation;
    }

    public void setWithoutPrecipitation(boolean withoutPrecipitation) {
        isWithoutPrecipitation = withoutPrecipitation;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(morningTemp);
        parcel.writeString(dayTemp);
        parcel.writeString(eveningTemp);
        parcel.writeString(nightTemp);
        parcel.writeInt(weatherIconId);
        parcel.writeString(weatherDescription);
        parcel.writeInt(windDirectIconId);
        parcel.writeString(windDescription);
        parcel.writeString(snow);
        parcel.writeString(rain);
        parcel.writeByte((byte) (isWithoutPrecipitation ? 1 : 0));
        parcel.writeString(pressure);
        parcel.writeString(humidity);
    }
}
