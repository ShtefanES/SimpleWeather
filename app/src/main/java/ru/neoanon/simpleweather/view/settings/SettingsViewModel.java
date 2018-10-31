package ru.neoanon.simpleweather.view.settings;

import android.arch.lifecycle.ViewModel;


import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import ru.neoanon.simpleweather.data.source.ForecastsRepo;
import ru.neoanon.simpleweather.model.Unit;
import timber.log.Timber;

/**
 * Created by eshtefan on  19.10.2018.
 */

public class SettingsViewModel extends ViewModel {
    private ForecastsRepo forecastsRepo;
    private BehaviorSubject<Unit> originalSettingsSub = BehaviorSubject.create();

    public SettingsViewModel(ForecastsRepo forecastsRepo) {
        this.forecastsRepo = forecastsRepo;
    }

    public Observable<Unit> subscribeToUnits() {
        return forecastsRepo.getUnits()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(unit -> originalSettingsSub.onNext(unit))
                .doOnError(throwable ->
                        Timber.e(throwable, "error in subscribeToUnits ")
                );
    }

    public Completable saveSettings(Unit unit) {
        return Observable.fromCallable(() -> {
            Unit originSettings = originalSettingsSub.getValue();
            return originSettings;
        }).filter(originalUnits -> (!originalUnits.equals(unit)))
                .map(originalUnits -> {
                    forecastsRepo.setUnits(unit);
                    forecastsRepo.deleteAllForecasts();
                    forecastsRepo.makeCacheDirty();
                    return 1;
                }).ignoreElements()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable ->
                        Timber.e(throwable, "error in saveSettings ")
                );
    }
}
