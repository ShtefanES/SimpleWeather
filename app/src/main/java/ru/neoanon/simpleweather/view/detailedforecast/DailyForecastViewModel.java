package ru.neoanon.simpleweather.view.detailedforecast;

import android.arch.lifecycle.ViewModel;

import java.io.IOException;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import ru.neoanon.simpleweather.R;
import ru.neoanon.simpleweather.data.source.ForecastsRepo;
import ru.neoanon.simpleweather.model.DailyForecastItem;
import timber.log.Timber;

/**
 * Created by eshtefan on  16.10.2018.
 */

public class DailyForecastViewModel extends ViewModel {
    private ForecastsRepo forecastsRepo;

    private BehaviorSubject<Boolean> progress = BehaviorSubject.createDefault(false);
    private PublishSubject<Throwable> errorSub = PublishSubject.create();

    public DailyForecastViewModel(ForecastsRepo forecastsRepo) {
        this.forecastsRepo = forecastsRepo;
    }

    public BehaviorSubject<Boolean> getProgress() {
        return progress;
    }

    public Observable<List<DailyForecastItem>> subscribeToDailyForecasts(long regionId) {
        return forecastsRepo.getAllDailyForecasts(regionId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe((disposable) -> progress.onNext(true))
                .doOnError(throwable -> {
                    progress.onNext(false);
                    Timber.e(throwable, "error in subscribeToDailyForecasts ");
                    errorSub.onNext(throwable);
                })
                .doOnNext(dailyForecastItems -> {
                    progress.onNext(false);
                });
    }

    public Observable<Integer> errorObservable() {
        return errorSub.map(throwable -> (throwable instanceof IOException) ? R.string.network_error : R.string.general_error);
    }
}
