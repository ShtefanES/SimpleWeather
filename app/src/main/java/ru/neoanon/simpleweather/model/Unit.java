package ru.neoanon.simpleweather.model;

/**
 * Created by eshtefan on  11.10.2018.
 */

public class Unit {
    private String tempType;
    private String pressureType;

    public Unit() {
    }

    public Unit(String tempType, String pressureType) {
        this.tempType = tempType;
        this.pressureType = pressureType;
    }

    public String getTempType() {
        return tempType;
    }

    public String getPressureType() {
        return pressureType;
    }

    public void setTempType(String tempType) {
        this.tempType = tempType;
    }

    public void setPressureType(String pressureType) {
        this.pressureType = pressureType;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Unit)) {
            return false;
        }

        Unit unit = (Unit) o;
        return unit.getTempType().equals(tempType) &&
                unit.getPressureType().equals(pressureType);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + tempType.hashCode();
        result = 31 * result + pressureType.hashCode();
        return result;
    }
}
