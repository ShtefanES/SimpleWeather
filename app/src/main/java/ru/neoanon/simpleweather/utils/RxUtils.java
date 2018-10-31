package ru.neoanon.simpleweather.utils;

import android.support.v7.widget.SearchView;
import android.view.View;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;

/**
 * Created by eshtefan on  27.09.2018.
 */

public class RxUtils {
    private RxUtils() {
    }

    public static Observable<String> searchViewQueryTextListener(@NonNull final SearchView searchView) {
        return Observable.create(emitter -> {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    emitter.onNext(newText);
                    return false;
                }
            });

            emitter.setCancellable(() -> searchView.setOnQueryTextListener(null));
        });
    }

    public static Observable<Object> searchViewCloseListener(@NonNull final SearchView searchView) {
        return Observable.create(emitter -> {
            searchView.setOnCloseListener(() -> {
                emitter.onNext(new Object());
                return false;
            });

            emitter.setCancellable(() -> searchView.setOnCloseListener(null));
        });
    }

    public static Observable<Boolean> searchViewFocusChangeListener(@NonNull final SearchView searchView) {
        return Observable.create(emitter -> {
            searchView.setOnQueryTextFocusChangeListener((view, b) -> emitter.onNext(b));

            emitter.setCancellable(() -> searchView.setOnQueryTextFocusChangeListener(null));
        });
    }


    public static Observable<Object> clickView(@NonNull View view) {
        return Observable.create(emitter -> {
            view.setOnClickListener((view1 -> emitter.onNext(new Object())));

            emitter.setCancellable(() -> view.setOnClickListener(null));
        });
    }
}