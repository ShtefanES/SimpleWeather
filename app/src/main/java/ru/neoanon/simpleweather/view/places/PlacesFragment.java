package ru.neoanon.simpleweather.view.places;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import ru.neoanon.simpleweather.R;
import ru.neoanon.simpleweather.app.App;
import ru.neoanon.simpleweather.data.source.local.db.location.RegionLocation;
import ru.neoanon.simpleweather.databinding.FragmentPlacesBinding;
import ru.neoanon.simpleweather.utils.RxUtils;
import ru.neoanon.simpleweather.view.mainscreen.WeatherViewModel;
import ru.neoanon.simpleweather.view.mainscreen.WeatherViewModelFactory;

/**
 * Created by eshtefan on  27.09.2018.
 */

public class PlacesFragment extends Fragment implements SuggestionsAdapter.OnSuggestionClickListener, PlaceAdapter.OnPlaceClickListener {
    private ImageView ivArrowBack;
    private RecyclerView placesRecyclerView;
    private TextView tvTitle;
    private SearchView searchView;
    private RecyclerView suggestionRecyclerView;
    private SuggestionsAdapter suggestionsAdapter;
    private PlaceAdapter placeAdapter;
    private CompositeDisposable disposable = new CompositeDisposable();


    @Inject
    WeatherViewModelFactory weatherViewModelFactory;
    @Inject
    LocationViewModelFactory locationViewModelFactory;
    private WeatherViewModel weatherViewModel;
    private LocationViewModel locationViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        FragmentPlacesBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_places, container, false);
        searchView = binding.searchView;
        tvTitle = binding.tvNavigationTitle;
        ivArrowBack = binding.ivNavigationArrowBack;
        suggestionRecyclerView = binding.suggestionRecyclerView;
        suggestionsAdapter = new SuggestionsAdapter(this);
        suggestionRecyclerView.setAdapter(suggestionsAdapter);
        placesRecyclerView = binding.placesRecyclerView;
        placeAdapter = new PlaceAdapter(this);
        placesRecyclerView.setAdapter(placeAdapter);

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((App) getActivity().getApplication()).getAppComponent().inject(this);

        weatherViewModel = ViewModelProviders.of(getActivity(), weatherViewModelFactory).get(WeatherViewModel.class);

        locationViewModel = ViewModelProviders.of(getActivity(), locationViewModelFactory).get(LocationViewModel.class);
    }

    @Override
    public void onStart() {
        super.onStart();

        setSearchViewListeners(searchView);

        disposable.add(locationViewModel.getIsTitleVisible()
                .subscribe(aBoolean -> tvTitle.setVisibility((aBoolean) ? View.VISIBLE : View.GONE)));

        disposable.add(locationViewModel.getIsSuggestionVisible()
                .subscribe(aBoolean -> suggestionRecyclerView.setVisibility((aBoolean) ? View.VISIBLE : View.GONE)));

        disposable.add(locationViewModel.getIsPlacesVisible()
                .subscribe(aBoolean -> placesRecyclerView.setVisibility((aBoolean) ? View.VISIBLE : View.GONE)));

        disposable.add(RxUtils.clickView(ivArrowBack)
                .subscribe(o -> {
                    if (!searchView.isIconified()) {
                        minimizingSearchIcon();
                    } else {
                        weatherViewModel.closeDrawer();
                    }
                }));

        disposable.add(weatherViewModel.subscribeRegionLocations()
                .subscribe(regionLocations -> placeAdapter.updateData(regionLocations), throwable -> {
                }));
    }

    @Override
    public void onStop() {
        super.onStop();
        disposable.clear();
    }

    private void setSearchViewListeners(SearchView searchView) {
        disposable.add(locationViewModel.addressSuggestions(searchView)
                .subscribe(regionLocations -> {
                    suggestionsAdapter.updateData(regionLocations);
                }, throwable -> {
                }));

        disposable.add(RxUtils.searchViewCloseListener(searchView)
                .subscribe(o -> {
                    locationViewModel.setTitleVisible(true);
                }));

        disposable.add(RxUtils.searchViewFocusChangeListener(searchView)
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        locationViewModel.setTitleVisible(false);
                    }
                }));
    }

    @Override
    public void onSuggestionClick(RegionLocation regionLocation) {
        minimizingSearchIcon();
        weatherViewModel.closeDrawer();
        disposable.add(weatherViewModel.selectRegionLocation(regionLocation)
                .subscribe((regionLocation1) -> {

                }, throwable -> {
                }));
    }

    private void minimizingSearchIcon() {
        searchView.setIconified(true);//twice call because if searchView has text then your call of setIconified(true) just clear text without minimizing the icon
        searchView.setIconified(true);
        locationViewModel.setTitleVisible(true);
    }

    @Override
    public void onPlaceClick(RegionLocation regionLocation) {
        minimizingSearchIcon();
        weatherViewModel.closeDrawer();
        disposable.add(weatherViewModel.selectRegionLocation(regionLocation)
                .subscribe((regionLocation1) -> {
                }, throwable -> {
                }));
    }

    @Override
    public void onDeletePlaceClick(long regionId) {
        disposable.add(weatherViewModel.deleteRegionLocation(regionId)
                .subscribe((regionLocation) -> {
                }, throwable -> {
                }));
    }
}
