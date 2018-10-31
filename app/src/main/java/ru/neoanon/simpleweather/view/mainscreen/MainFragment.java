package ru.neoanon.simpleweather.view.mainscreen;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import ru.neoanon.simpleweather.R;
import ru.neoanon.simpleweather.app.App;
import ru.neoanon.simpleweather.databinding.FragmentMainBinding;
import ru.neoanon.simpleweather.utils.Constants;
import ru.neoanon.simpleweather.view.detailedforecast.ForecastActivity;
import ru.neoanon.simpleweather.view.detailedforecast.ForecastPagerFragment;
import timber.log.Timber;

import static android.support.v4.content.ContextCompat.checkSelfPermission;

/**
 * Created by eshtefan on  05.10.2018.
 */

public class MainFragment extends Fragment {
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 101;
    private CompositeDisposable disposable = new CompositeDisposable();
    private CompositeDisposable weatherDisposable = new CompositeDisposable();

    private RecyclerView dailyForecastRecyclerView;
    private RecyclerView hourlyForecastRecyclerView;
    private long selectedRegionId;
    private WeatherViewModel weatherViewModel;
    private SwipeRefreshLayout swipeRefresh;
    private TextView tvTemp;
    private ImageView iconWeather;
    private TextView tvWeatherDescription;
    private ImageView ivWeatherBackground;
    private View currentWeatherBlock;

    @Inject
    DailyForecastAdapter dailyForecastAdapter;
    @Inject
    HourlyForecastAdapter hourlyForecastAdapter;
    @Inject
    WeatherViewModelFactory weatherViewModelFactory;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((App) getActivity().getApplication()).getAppComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        FragmentMainBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_main, container, false);

        dailyForecastRecyclerView = binding.shortDailyForecastRecyclerView;
        dailyForecastRecyclerView.setAdapter(dailyForecastAdapter);

        dailyForecastRecyclerView.setNestedScrollingEnabled(false);

        hourlyForecastRecyclerView = binding.hourlyForecastRecyclerView;
        hourlyForecastRecyclerView.setAdapter(hourlyForecastAdapter);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        hourlyForecastRecyclerView.setLayoutManager(horizontalLayoutManager);

        swipeRefresh = binding.swipeContainer;
        tvTemp = binding.tvTemp;
        iconWeather = binding.iconWeather;
        tvWeatherDescription = binding.tvWeatherDescription;
        ivWeatherBackground = binding.ivWeatherBackground;
        currentWeatherBlock = binding.currentWeatherBlock;

        ivWeatherBackground.setImageResource(R.drawable.ic_background_clear);
        changeWeatherBackgroundScale(ivWeatherBackground);

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        weatherViewModel = ViewModelProviders.of(getActivity(), weatherViewModelFactory).get(WeatherViewModel.class);

        DailyForecastAdapter.OnDailyForecastClickListener forecastClickListener = (position -> {
            if (swipeRefresh.isRefreshing()) {
                showMessage(R.string.wait_text);
            } else {
                Intent intent = new Intent(MainFragment.this.getActivity(), ForecastActivity.class);
                intent.putExtra(ForecastPagerFragment.FORECAST_POSITION_KEY, position);
                intent.putExtra(ForecastPagerFragment.REGION_ID_KEY, selectedRegionId);
                startActivity(intent);
            }
        });
        dailyForecastAdapter.setClickListener(forecastClickListener);
    }

    @Override
    public void onStart() {
        super.onStart();
        disposable.add(weatherViewModel.getSelectedRegionIdSub()
                .subscribe(regionId -> {
                    selectedRegionId = regionId;
                    Timber.w("selected region id: %s", selectedRegionId);
                }));

        disposable.add(weatherViewModel.getProgress()
                .subscribe(aBoolean -> {
                    swipeRefresh.setRefreshing(aBoolean);
                }));

        disposable.add(weatherViewModel.getRegionWasChangedSub()
                .subscribe(regionId -> {
                    unsubscribeToChangeUIWeatherLists();
                    subscribeToChangeUIWeatherLists(regionId);
                }));

        disposable.add(weatherViewModel.errorObservable()
                .subscribe(integer -> showMessage(integer)));

        if (selectedRegionId == Constants.CURRENT_LOCATION_ID) {
            if (!isPermissionGranted()) {
                requestPermission();
            } else {
                subscribeToData(selectedRegionId);
            }
        } else {
            subscribeToData(selectedRegionId);
        }

        subscribeToChangeUIWeatherLists(selectedRegionId);

        SwipeRefreshLayout.OnRefreshListener refreshListener = (() -> {
            if (selectedRegionId == Constants.CURRENT_LOCATION_ID) {
                if (!isPermissionGranted()) {
                    requestPermission();
                } else {
                    subscribeToData(selectedRegionId);
                }
            } else {
                subscribeToData(selectedRegionId);
            }
        });
        swipeRefresh.setOnRefreshListener(refreshListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSIONS_REQUEST_CODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    subscribeToData(Constants.CURRENT_LOCATION_ID);
                } else {
                    weatherViewModel.getProgress().onNext(false);
                }
                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        disposable.clear();
        unsubscribeToChangeUIWeatherLists();
        swipeRefresh.setOnRefreshListener(null);
    }

    private boolean isPermissionGranted() {
        return (checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermission() {
        new AlertDialog.Builder(getActivity())
                .setMessage(R.string.pre_request_for_permission_request)
                .setPositiveButton(R.string.btn_name_continue,
                        (dialogInterface, i) ->
                                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSIONS_REQUEST_CODE))
                .setCancelable(false)
                .create()
                .show();
    }

    private void subscribeToData(long regionId) {
        disposable.add(
                weatherViewModel.subscribeToData(regionId)
                        .subscribe((regionLocation) -> {
                        }, throwable -> {
                        })
        );
    }

    private void subscribeToChangeUIWeatherLists(long regionId) {
        weatherDisposable.add(weatherViewModel.subscribeDailyForecasts(regionId)
                .subscribe(forecastShortItems -> dailyForecastAdapter.updateData(forecastShortItems), throwable -> {
                }));

        weatherDisposable.add(weatherViewModel.subscribeHourlyForecasts(regionId)
                .subscribe(commonHourlyItems -> hourlyForecastAdapter.updateData(commonHourlyItems), throwable -> {
                }));

        weatherDisposable.add(weatherViewModel.subscribeCurrentWeather(regionId)
                .subscribe(currentWeatherItem -> {
                    currentWeatherBlock.setVisibility((currentWeatherItem.getIconId() == 0) ? View.GONE : View.VISIBLE);

                    if (currentWeatherItem.getIconId() != 0) {
                        currentWeatherBlock.setVisibility(View.VISIBLE);
                        tvTemp.setText(currentWeatherItem.getTemp());
                        iconWeather.setImageResource(currentWeatherItem.getIconId());
                        tvWeatherDescription.setText(currentWeatherItem.getWeatherDescription());

                        ivWeatherBackground.setImageResource(currentWeatherItem.getBackgroundIconId());
                    }
                }, throwable -> {
                }));
    }

    private void unsubscribeToChangeUIWeatherLists() {
        weatherDisposable.clear();
    }

    private void changeWeatherBackgroundScale(ImageView ivBackground) {
        final Matrix matrix = ivBackground.getImageMatrix();
        final float imageWidth = ivBackground.getDrawable().getIntrinsicWidth();
        final int screenWidth = getResources().getDisplayMetrics().widthPixels;
        final float scaleRatio = (screenWidth < 500) ? screenWidth / imageWidth + 1.0f : screenWidth / imageWidth + 0.2f;
        matrix.postScale(scaleRatio, scaleRatio);
        ivBackground.setImageMatrix(matrix);
    }

    private void showMessage(int resIdString) {
        Toast.makeText(getActivity(), getString(resIdString), Toast.LENGTH_SHORT).show();
    }
}
