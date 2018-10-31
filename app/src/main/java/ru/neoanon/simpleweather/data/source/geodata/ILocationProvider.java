package ru.neoanon.simpleweather.data.source.geodata;

import io.reactivex.Observable;
import ru.neoanon.simpleweather.model.LocationDto;

/**
 * Created by eshtefan on  26.10.2018.
 */

public interface ILocationProvider {

    Observable<LocationDto> getLastKnownLocation();
}
