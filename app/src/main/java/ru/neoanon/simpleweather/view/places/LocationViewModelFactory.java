package ru.neoanon.simpleweather.view.places;

import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

import ru.neoanon.simpleweather.data.source.geodata.IRegionSource;

/**
 * Created by eshtefan on  27.09.2018.
 */

@Singleton
public class LocationViewModelFactory implements ViewModelProvider.Factory {
    private IRegionSource iRegionSource;

    @Inject
    public LocationViewModelFactory(IRegionSource iRegionSource) {
        this.iRegionSource = iRegionSource;
    }

    @NonNull
    @Override
    public LocationViewModel create(@NonNull Class modelClass) {
        return new LocationViewModel(iRegionSource);
    }
}
