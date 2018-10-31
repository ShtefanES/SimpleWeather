package ru.neoanon.simpleweather.data.source.geodata;

import android.app.Application;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import ru.neoanon.simpleweather.RxTestRule;
import ru.neoanon.simpleweather.data.source.local.db.location.RegionLocation;
import ru.neoanon.simpleweather.model.LocationDto;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

/**
 * Created by eshtefan on  26.10.2018.
 */

public class LocationSourceTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Rule
    public RxTestRule rxTestRule = new RxTestRule();

    @Mock
    ILocationProvider locationProvider;
    @Mock
    Geocoder geocoder;
    @Mock
    Application application;
    @Mock
    Context context;
    @Mock
    Address address;

    private RegionSource regionSource;
    private String locality = "Moscow";
    private String userRequest = "mosc";
    private double latitude = 124.57d;
    private double longitude = 177.23d;
    private String emptyLocality = "none";

    @Before
    public void initRegionSource() {
        regionSource = new RegionSource(locationProvider, geocoder, application);
    }


    @Test
    public void getCurrentRegionLocation() throws IOException {
        List<Address> addresses = new ArrayList<>();
        addresses.add(address);

        when(locationProvider.getLastKnownLocation()).thenReturn(Observable.just(new LocationDto(latitude, longitude)));
        when(geocoder.getFromLocation(latitude, longitude, 2)).thenReturn(addresses);
        when(address.getLocality()).thenReturn(locality);

        regionSource.getCurrentRegionLocation()
                .test()
                .assertValue(regionLocation -> regionLocation.name.equals(locality) && regionLocation.getLatitude() == latitude && regionLocation.getLongitude() == longitude);
    }

    @Test
    public void getCurrentRegionLocationWhenEmptyAddresses() throws IOException {
        List<Address> addresses = new ArrayList<>();

        when(locationProvider.getLastKnownLocation()).thenReturn(Observable.just(new LocationDto(latitude, longitude)));
        when(geocoder.getFromLocation(latitude, longitude, 2)).thenReturn(addresses);
        when(application.getApplicationContext()).thenReturn(context);
        when(context.getString(anyInt())).thenReturn(emptyLocality);

        regionSource.getCurrentRegionLocation()
                .test()
                .assertValue(regionLocation -> regionLocation.name.equals(emptyLocality) && regionLocation.getLatitude() == latitude && regionLocation.getLongitude() == longitude);
    }

    @Test
    public void getCurrentRegionLocationWhenLocalityNullSubAdminAreaNotNull() throws IOException {
        List<Address> addresses = new ArrayList<>();
        addresses.add(address);

        when(locationProvider.getLastKnownLocation()).thenReturn(Observable.just(new LocationDto(latitude, longitude)));
        when(geocoder.getFromLocation(latitude, longitude, 2)).thenReturn(addresses);
        when(address.getLocality()).thenReturn(null);
        when(address.getSubAdminArea()).thenReturn(locality);

        regionSource.getCurrentRegionLocation()
                .test()
                .assertValue(regionLocation -> regionLocation.name.equals(locality) && regionLocation.getLatitude() == latitude && regionLocation.getLongitude() == longitude);
    }

    @Test
    public void getCurrentRegionLocationWhenLocalityAndSubAdminAreaNull() throws IOException {
        List<Address> addresses = new ArrayList<>();
        addresses.add(address);

        when(locationProvider.getLastKnownLocation()).thenReturn(Observable.just(new LocationDto(latitude, longitude)));
        when(geocoder.getFromLocation(latitude, longitude, 2)).thenReturn(addresses);
        when(address.getLocality()).thenReturn(null);
        when(application.getApplicationContext()).thenReturn(context);
        when(context.getString(anyInt())).thenReturn(emptyLocality);

        regionSource.getCurrentRegionLocation()
                .test()
                .assertValue(regionLocation -> regionLocation.name.equals(emptyLocality) && regionLocation.getLatitude() == latitude && regionLocation.getLongitude() == longitude);
    }

    @Test
    public void getAddressSuggestions() throws IOException {
        List<Address> addresses = new ArrayList<>();
        addresses.add(address);

        when(geocoder.getFromLocationName(userRequest, 5)).thenReturn(addresses);
        when(address.getLocality()).thenReturn(locality);
        when(address.getLatitude()).thenReturn(latitude);
        when(address.getLongitude()).thenReturn(longitude);
        regionSource.getAddressSuggestions(userRequest)
                .test()
                .assertValue(regionLocations -> {
                    RegionLocation regionLocation = regionLocations.get(0);
                    return (regionLocation.name.equals(locality) && regionLocation.getLatitude() == latitude && regionLocation.getLongitude() == longitude);
                });
    }

    @Test
    public void getAddressSuggestionsWhenLocalityNullSubAdminAreaNotNull() throws IOException {
        List<Address> addresses = new ArrayList<>();
        addresses.add(address);

        when(geocoder.getFromLocationName(userRequest, 5)).thenReturn(addresses);
        when(address.getSubAdminArea()).thenReturn(locality);
        when(address.getLatitude()).thenReturn(latitude);
        when(address.getLongitude()).thenReturn(longitude);
        regionSource.getAddressSuggestions(userRequest)
                .test()
                .assertValue(regionLocations -> {
                    RegionLocation regionLocation = regionLocations.get(0);
                    return (regionLocation.name.equals(locality) && regionLocation.getLatitude() == latitude && regionLocation.getLongitude() == longitude);
                });
    }

    @Test
    public void getAddressSuggestionsWhenLocalityAndSubAdminAreaNull() throws IOException {
        List<Address> addresses = new ArrayList<>();
        addresses.add(address);

        when(geocoder.getFromLocationName(userRequest, 5)).thenReturn(addresses);
        regionSource.getAddressSuggestions(userRequest)
                .test()
                .assertValue(regionLocations -> regionLocations.size() == 0);
    }

    @Test
    public void getAddressSuggestionsWhenGeocoderReturnEmptyList() throws IOException {
        List<Address> addresses = new ArrayList<>();

        when(geocoder.getFromLocationName(userRequest, 5)).thenReturn(addresses);
        regionSource.getAddressSuggestions(userRequest)
                .test()
                .assertValue(regionLocations -> regionLocations.size() == 0);
    }
}