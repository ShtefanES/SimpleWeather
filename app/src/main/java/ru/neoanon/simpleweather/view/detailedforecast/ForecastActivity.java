package ru.neoanon.simpleweather.view.detailedforecast;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import ru.neoanon.simpleweather.R;
import ru.neoanon.simpleweather.databinding.ActivityForecastBinding;

/**
 * Created by eshtefan on  16.10.2018.
 */

public class ForecastActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityForecastBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_forecast);
        Toolbar toolbar = binding.toolbarContainer.toolbar;
        setupToolbar(toolbar);

        Intent intent = getIntent();
        int selectedForecastPosition = intent.getIntExtra(ForecastPagerFragment.FORECAST_POSITION_KEY, -1);
        long regionId = intent.getLongExtra(ForecastPagerFragment.REGION_ID_KEY, -1);
        if (selectedForecastPosition != -1 && regionId != -1) {
            ForecastPagerFragment pagerFragment = ForecastPagerFragment.newInstance(selectedForecastPosition, regionId);
            commitFragment(pagerFragment);
        }
    }

    private void setupToolbar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.ten_day_forecast_title);

        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    void commitFragment(Fragment fragment) {
        FragmentTransaction fTrans = getSupportFragmentManager().beginTransaction();
        fTrans.replace(R.id.daily_forecast_content, fragment);
        fTrans.commit();
    }
}
