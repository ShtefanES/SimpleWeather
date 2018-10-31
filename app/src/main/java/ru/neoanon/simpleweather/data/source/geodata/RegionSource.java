package ru.neoanon.simpleweather.data.source.geodata;

import android.app.Application;
import android.location.Address;
import android.location.Geocoder;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import ru.neoanon.simpleweather.R;
import ru.neoanon.simpleweather.data.source.local.db.location.RegionLocation;
import ru.neoanon.simpleweather.model.LocationDto;

/**
 * Created by eshtefan on  21.09.2018.
 */

public class RegionSource implements IRegionSource {
    private ILocationProvider iLocationProvider;
    private Geocoder geocoder;
    private Application application;

    public RegionSource(ILocationProvider iLocationProvider, Geocoder geocoder, Application application) {
        this.iLocationProvider = iLocationProvider;
        this.geocoder = geocoder;
        this.application = application;
    }

    @Override
    public Observable<RegionLocation> getCurrentRegionLocation() {
        return iLocationProvider.getLastKnownLocation()
                .map(locationDto -> {
                    String regionName;
                    List<Address> address = geocoder.getFromLocation(locationDto.getLatitude(), locationDto.getLongitude(), 2);
                    if (!address.isEmpty()) {
                        Address address1 = address.get(0);
                        if (address1.getLocality() != null) {
                            regionName = address1.getLocality();
                        } else if (address1.getSubAdminArea() != null) {
                            regionName = address1.getSubAdminArea();
                        } else {
                            regionName = application.getApplicationContext().getString(R.string.nameless_location);
                        }
                    } else {
                        regionName = application.getApplicationContext().getString(R.string.nameless_location);
                    }
                    return new RegionLocation(locationDto, regionName);
                });
    }

    @Override
    public Observable<List<RegionLocation>> getAddressSuggestions(String userRequest) {
        return Observable.fromCallable(() -> {
            List<Address> address = geocoder.getFromLocationName(userRequest, 5);
            List<RegionLocation> regionLocations = new ArrayList<>();
            for (Address address1 : address) {
                if (address1.getLocality() != null) {
                    RegionLocation regionLocation = new RegionLocation(
                            new LocationDto(address1.getLatitude(), address1.getLongitude()),
                            address1.getLocality());
                    regionLocations.add(regionLocation);
                } else if (address1.getSubAdminArea() != null) {
                    RegionLocation regionLocation = new RegionLocation(
                            new LocationDto(address1.getLatitude(), address1.getLongitude()),
                            address1.getSubAdminArea());
                    regionLocations.add(regionLocation);
                }
            }
            return regionLocations;
        });
    }
}
