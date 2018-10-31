package ru.neoanon.simpleweather.view.detailedforecast;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import ru.neoanon.simpleweather.R;
import ru.neoanon.simpleweather.app.App;
import ru.neoanon.simpleweather.databinding.FragmentForecastPagerBinding;

/**
 * Created by eshtefan on  16.10.2018.
 */

public class ForecastPagerFragment extends Fragment {
    public static final String FORECAST_POSITION_KEY = "forecastPositionKey";
    public static final String REGION_ID_KEY = "regionIdKey";

    public static ForecastPagerFragment newInstance(int selectedForecastPosition, long regionId) {
        ForecastPagerFragment f = new ForecastPagerFragment();

        Bundle args = new Bundle();
        args.putInt(FORECAST_POSITION_KEY, selectedForecastPosition);
        args.putLong(REGION_ID_KEY, regionId);
        f.setArguments(args);
        return f;
    }

    private CompositeDisposable disposable = new CompositeDisposable();
    private int selectedForecastPosition;
    private long regionId;
    private ViewPager viewPager;
    private ProgressBar progressBar;
    private PagerForecastAdapter pagerAdapter;
    private DailyForecastViewModel forecastViewModel;
    private TabLayout tabLayout;

    @Inject
    DailyForecastViewModelFactory forecastViewModelFactory;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((App) getActivity().getApplication()).getAppComponent().inject(this);
        Bundle bundle = getArguments();
        selectedForecastPosition = bundle.getInt(FORECAST_POSITION_KEY);
        regionId = bundle.getLong(REGION_ID_KEY);


    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        FragmentForecastPagerBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_forecast_pager, container, false);
        viewPager = binding.viewpager;

        progressBar = binding.progressBar;
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        tabLayout = (TabLayout) getActivity().findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        forecastViewModel = ViewModelProviders.of(getActivity(), forecastViewModelFactory).get(DailyForecastViewModel.class);

        disposable.add(forecastViewModel.getProgress()
                .subscribe(aBoolean -> progressBar.setVisibility((aBoolean) ? View.VISIBLE : View.GONE)));

        disposable.add(forecastViewModel.subscribeToDailyForecasts(regionId)
                .subscribe(dailyForecastItems -> {
                    pagerAdapter = new PagerForecastAdapter(getChildFragmentManager(), dailyForecastItems);
                    viewPager.setAdapter(pagerAdapter);
                    viewPager.setCurrentItem(selectedForecastPosition);
                }, throwable -> {
                }));

        disposable.add(forecastViewModel.errorObservable()
                .subscribe(integer -> showMessage(integer)));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.clear();
    }

    private void showMessage(int resIdString) {
        Toast.makeText(getActivity(), getString(resIdString), Toast.LENGTH_SHORT).show();
    }
}
