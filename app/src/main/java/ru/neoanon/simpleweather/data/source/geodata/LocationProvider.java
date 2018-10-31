package ru.neoanon.simpleweather.data.source.geodata;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import pl.charmas.android.reactivelocation2.ReactiveLocationProvider;
import ru.neoanon.simpleweather.model.LocationDto;
import timber.log.Timber;

/**
 * Created by eshtefan on  26.10.2018.
 */

public class LocationProvider implements ILocationProvider {
    private ReactiveLocationProvider locationProvider;

    public LocationProvider(ReactiveLocationProvider locationProvider) {
        this.locationProvider = locationProvider;
    }

    @Override
    public Observable<LocationDto> getLastKnownLocation() {
        return locationProvider.getLastKnownLocation()
                .flatMap(location -> {
                    Timber.d(String.format(" LastKnownLocation location: %s,%s", location.getLatitude(), location.getLongitude()));
                    return Observable.just(new LocationDto(location.getLatitude(), location.getLongitude()))
                            .subscribeOn(Schedulers.io());
                });
    }
}
