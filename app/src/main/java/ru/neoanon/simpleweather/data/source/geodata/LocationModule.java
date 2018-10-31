package ru.neoanon.simpleweather.data.source.geodata;

import android.app.Application;
import android.content.Context;
import android.location.Geocoder;

import java.util.Locale;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import pl.charmas.android.reactivelocation2.ReactiveLocationProvider;

/**
 * Created by eshtefan on  21.09.2018.
 */

@Module
public class LocationModule {

    @Singleton
    @Provides
    ReactiveLocationProvider providesLocationProvider(Context appContext) {
        return new ReactiveLocationProvider(appContext);
    }

    @Singleton
    @Provides
    Geocoder providesGeocoder(Context appContext, Locale locale) {
        return new Geocoder(appContext, locale);
    }

    @Singleton
    @Provides
    ILocationProvider locationProvider(ReactiveLocationProvider reactiveLocationProvider) {
        return new LocationProvider(reactiveLocationProvider);
    }

    @Singleton
    @Provides
    IRegionSource providesLocationSource(ILocationProvider iLocationProvider, Geocoder geocoder, Application application) {
        return new RegionSource(iLocationProvider, geocoder, application);
    }
}
