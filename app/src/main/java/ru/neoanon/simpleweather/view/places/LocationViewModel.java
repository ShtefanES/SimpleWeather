package ru.neoanon.simpleweather.view.places;

import android.arch.lifecycle.ViewModel;
import android.support.v7.widget.SearchView;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import ru.neoanon.simpleweather.data.source.geodata.IRegionSource;
import ru.neoanon.simpleweather.data.source.local.db.location.RegionLocation;
import ru.neoanon.simpleweather.utils.RxUtils;
import timber.log.Timber;

/**
 * Created by eshtefan on  27.09.2018.
 */

public class LocationViewModel extends ViewModel {
    private IRegionSource iRegionSource;

    private PublishSubject<Boolean> isSuggestionVisible = PublishSubject.create();
    private PublishSubject<Boolean> isPlacesVisible = PublishSubject.create();
    private PublishSubject<Boolean> isTitleVisible = PublishSubject.create();

    public LocationViewModel(IRegionSource iRegionSource) {
        this.iRegionSource = iRegionSource;
    }

    public PublishSubject<Boolean> getIsTitleVisible() {
        return isTitleVisible;
    }

    public PublishSubject<Boolean> getIsSuggestionVisible() {
        return isSuggestionVisible;
    }

    public PublishSubject<Boolean> getIsPlacesVisible() {
        return isPlacesVisible;
    }

    public void setTitleVisible(boolean b) {
        isTitleVisible.onNext(b);
    }

    public Observable<List<RegionLocation>> addressSuggestions(SearchView searchView) {
        return RxUtils.searchViewQueryTextListener(searchView)
                .doOnNext(s -> {
                    if (s.isEmpty()) {
                        isSuggestionVisible.onNext(false);
                        isPlacesVisible.onNext(true);
                    }
                })
                .filter(s -> (!s.isEmpty() && s.length() > 3 && s.length() < 20))
                .flatMap(s -> iRegionSource.getAddressSuggestions(s))
                .doOnNext(regionLocations -> {
                    if (regionLocations.size() != 0) {
                        isPlacesVisible.onNext(false);
                        isSuggestionVisible.onNext(true);
                    } else {
                        isSuggestionVisible.onNext(false);
                        isPlacesVisible.onNext(true);
                    }
                })
                .doOnError(throwable -> Timber.e(throwable, "error in addressSuggestions "))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


}
