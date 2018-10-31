package ru.neoanon.simpleweather.view.detailedforecast;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import ru.neoanon.simpleweather.model.DailyForecastItem;

/**
 * Created by eshtefan on  16.10.2018.
 */

public class PagerForecastAdapter extends FragmentPagerAdapter {
    private List<DailyForecastItem> forecasts;

    public PagerForecastAdapter(FragmentManager fm, List<DailyForecastItem> forecasts) {
        super(fm);
        this.forecasts = forecasts;
    }

    @Override
    public Fragment getItem(int i) {
        DailyForecastItem item = forecasts.get(i);
        return ForecastItemFragment.newInstance(item);
    }

    @Override
    public int getCount() {
        return forecasts.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return forecasts.get(position).getTitle();
    }
}
