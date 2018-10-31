package ru.neoanon.simpleweather.view.mainscreen;

import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

import ru.neoanon.simpleweather.data.source.ForecastsRepo;
import ru.neoanon.simpleweather.data.source.geodata.IRegionSource;

/**
 * Created by eshtefan on  19.09.2018.
 */

@Singleton
public class WeatherViewModelFactory implements ViewModelProvider.Factory {
    private ForecastsRepo forecastsRepo;
    private IRegionSource iRegionSource;

    @Inject
    public WeatherViewModelFactory(ForecastsRepo forecastsRepo, IRegionSource iRegionSource) {
        this.forecastsRepo = forecastsRepo;
        this.iRegionSource = iRegionSource;
    }

    @NonNull
    @Override
    public WeatherViewModel create(@NonNull Class modelClass) {
        return new WeatherViewModel(forecastsRepo, iRegionSource);
    }
}
