package ru.neoanon.simpleweather.view.mainscreen;

import android.arch.lifecycle.ViewModel;

import java.io.IOException;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import ru.neoanon.simpleweather.R;
import ru.neoanon.simpleweather.data.source.ForecastsRepo;
import ru.neoanon.simpleweather.data.source.geodata.IRegionSource;
import ru.neoanon.simpleweather.data.source.local.db.location.RegionLocation;
import ru.neoanon.simpleweather.model.CommonHourlyItem;
import ru.neoanon.simpleweather.model.CurrentWeatherItem;
import ru.neoanon.simpleweather.model.DailyForecastShortItem;
import ru.neoanon.simpleweather.utils.Constants;
import timber.log.Timber;

/**
 * Created by eshtefan on  19.09.2018.
 */

public class WeatherViewModel extends ViewModel {
    private ForecastsRepo forecastsRepo;
    private IRegionSource iRegionSource;

    private PublishSubject<Object> closeDrawerSub = PublishSubject.create();
    private PublishSubject<Long> regionWasChangedSub = PublishSubject.create();
    private BehaviorSubject<Boolean> progress = BehaviorSubject.createDefault(false);
    private PublishSubject<Throwable> errorSub = PublishSubject.create();
    private BehaviorSubject<Boolean> isCurrentRegionAvailable = BehaviorSubject.createDefault(false);
    private BehaviorSubject<String> selectedRegionNameSub = BehaviorSubject.createDefault("Unknown");
    private BehaviorSubject<Long> selectedRegionIdSub = BehaviorSubject.createDefault(Constants.CURRENT_LOCATION_ID);

    public WeatherViewModel(ForecastsRepo forecastsRepo, IRegionSource iRegionSource) {
        this.forecastsRepo = forecastsRepo;
        this.iRegionSource = iRegionSource;
    }

    public PublishSubject<Object> getCloseDrawerSub() {
        return closeDrawerSub;
    }

    public PublishSubject<Long> getRegionWasChangedSub() {
        return regionWasChangedSub;
    }

    public BehaviorSubject<Boolean> getProgress() {
        return progress;
    }

    public BehaviorSubject<Long> getSelectedRegionIdSub() {
        return selectedRegionIdSub;
    }

    public Observable<RegionLocation> subscribeToData(long regionId) {
        return Observable.fromCallable(() -> regionId)
                .flatMap(id -> (id == Constants.CURRENT_LOCATION_ID) ?
                        iRegionSource.getCurrentRegionLocation()
                                .flatMap(regionLocation -> {
                                    regionLocation.setId(Constants.CURRENT_LOCATION_ID);
                                    forecastsRepo.addRegionLocation(regionLocation);
                                    return Observable.just(regionLocation);
                                })
                        :
                        forecastsRepo.getRegionLocation(id)
                ).flatMap(regionLocation -> {
                    selectedRegionNameSub.onNext(regionLocation.name);
                    return forecastsRepo.updateData(regionLocation);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe((disposable) -> progress.onNext(true))
                .doOnComplete(() -> progress.onNext(false))
                .doOnError(throwable -> {
                    Timber.e(throwable, "error in subscribeToData ");
                    errorSub.onNext(throwable);
                    progress.onNext(false);
                })
                .doOnNext(regionLocation -> {
                    isCurrentRegionAvailable.onNext((regionLocation.getId() == Constants.CURRENT_LOCATION_ID));
                    selectedRegionIdSub.onNext(regionLocation.getId());
                    progress.onNext(false);
                });
    }

    public Observable<String> subscribeSelectedRegionName() {
        return selectedRegionNameSub
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> Timber.e(throwable, "error in subscribeSelectedRegionName "));
    }

    public Observable<RegionLocation> selectRegionLocation(RegionLocation regionLocation) {
        return Observable.fromCallable(() -> {
            RegionLocation regionLocation1 = forecastsRepo.getRegionLocation(regionLocation.name);
            if (regionLocation1 == null) {
                long regionId = forecastsRepo.addRegionLocation(regionLocation);
                regionLocation1 = regionLocation;
                regionLocation1.setId(regionId);
            }
            forecastsRepo.makeCacheDirty();
            selectedRegionNameSub.onNext(regionLocation1.name);
            return regionLocation1;
        })
                .flatMap(regionLocation1 -> forecastsRepo.updateData(regionLocation1))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe((disposable) -> progress.onNext(true))
                .doOnError(throwable -> {
                    Timber.e(throwable, "error in selectRegionLocation ");
                    errorSub.onNext(throwable);
                    progress.onNext(false);
                })
                .doOnNext(fullRegionLocation -> {
                    if (selectedRegionIdSub.getValue() != fullRegionLocation.getId()) {
                        selectedRegionIdSub.onNext(fullRegionLocation.getId());
                        regionWasChangedSub.onNext(fullRegionLocation.getId());
                    }
                    progress.onNext(false);
                });
    }

    public Observable<RegionLocation> deleteRegionLocation(long regionId) {
        return forecastsRepo.deleteAllInfoForThisRegion(regionId)
                .andThen(Observable.just(regionId))
                .filter(regionId1 -> regionId1.equals(selectedRegionIdSub.getValue()))
                .filter(regionId1 -> {
                    boolean isAvailable = isCurrentRegionAvailable.getValue();
                    if (!isAvailable) {
                        forecastsRepo.makeCacheDirty();
                        selectedRegionNameSub.onNext("Unknown");
                        selectedRegionIdSub.onNext(Constants.CURRENT_LOCATION_ID);
                    }
                    return isAvailable;
                })
                .flatMap(aLong -> forecastsRepo.getRegionLocation(Constants.CURRENT_LOCATION_ID))
                .flatMap(regionLocation -> {
                    forecastsRepo.makeCacheDirty();
                    selectedRegionNameSub.onNext(regionLocation.name);
                    return forecastsRepo.updateData(regionLocation);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe((disposable) -> progress.onNext(true))
                .doOnError(throwable -> {
                    progress.onNext(false);
                    Timber.e(throwable, "error in deleteRegionLocation ");
                    errorSub.onNext(throwable);
                })
                .doOnComplete(() -> {
                    progress.onNext(false);
                })
                .doOnNext(regionLocation -> {
                    selectedRegionIdSub.onNext(regionLocation.getId());
                    regionWasChangedSub.onNext(regionLocation.getId());
                    progress.onNext(false);
                });
    }

    public Flowable<List<RegionLocation>> subscribeRegionLocations() {
        return forecastsRepo.getAllRegionLocations()
                .doOnError(throwable -> {
                    Timber.e(throwable, "error in subscribeRegionLocations ");
                    errorSub.onNext(throwable);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Flowable<List<DailyForecastShortItem>> subscribeDailyForecasts(long regionId) {
        return forecastsRepo.getAllShortDailyForecasts(regionId)
                .doOnError(throwable -> {
                    Timber.e(throwable, "error in subscribeDailyForecasts ");
                    errorSub.onNext(throwable);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Flowable<List<CommonHourlyItem>> subscribeHourlyForecasts(long regionId) {
        return forecastsRepo.getAllCommonHourlyForecasts(regionId)
                .doOnError(throwable -> {
                    Timber.e(throwable, "error in subscribeHourlyForecasts ");
                    errorSub.onNext(throwable);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Flowable<CurrentWeatherItem> subscribeCurrentWeather(long regionId) {
        return forecastsRepo.getCurrentWeather(regionId)
                .doOnError(throwable -> {
                    Timber.e(throwable, "error in subscribeCurrentWeather ");
                    errorSub.onNext(throwable);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Integer> errorObservable() {
        return errorSub.map(throwable -> (throwable instanceof IOException) ? R.string.network_error : R.string.general_error);
    }

    public void closeDrawer() {
        closeDrawerSub.onNext(new Object());
    }
}
