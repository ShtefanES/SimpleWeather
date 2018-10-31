package ru.neoanon.simpleweather.view.settings;

import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import javax.inject.Inject;

import ru.neoanon.simpleweather.data.source.ForecastsRepo;

/**
 * Created by eshtefan on  19.10.2018.
 */

public class SettingsViewModelFactory implements ViewModelProvider.Factory {
    private ForecastsRepo forecastsRepo;

    @Inject
    public SettingsViewModelFactory(ForecastsRepo forecastsRepo) {
        this.forecastsRepo = forecastsRepo;
    }

    @NonNull
    @Override
    public SettingsViewModel create(@NonNull Class modelClass) {
        return new SettingsViewModel(forecastsRepo);
    }
}