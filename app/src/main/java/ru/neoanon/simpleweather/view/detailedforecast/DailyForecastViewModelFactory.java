package ru.neoanon.simpleweather.view.detailedforecast;

import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import javax.inject.Inject;

import ru.neoanon.simpleweather.data.source.ForecastsRepo;

/**
 * Created by eshtefan on  16.10.2018.
 */

public class DailyForecastViewModelFactory implements ViewModelProvider.Factory {
    private ForecastsRepo forecastsRepo;

    @Inject
    public DailyForecastViewModelFactory(ForecastsRepo forecastsRepo) {
        this.forecastsRepo = forecastsRepo;
    }

    @NonNull
    @Override
    public DailyForecastViewModel create(@NonNull Class modelClass) {
        return new DailyForecastViewModel(forecastsRepo);
    }
}