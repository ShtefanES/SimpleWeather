package ru.neoanon.simpleweather.view.detailedforecast;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.neoanon.simpleweather.R;
import ru.neoanon.simpleweather.databinding.FragmentDailyForecastItemBinding;
import ru.neoanon.simpleweather.model.DailyForecastItem;

/**
 * Created by eshtefan on  16.10.2018.
 */

public class ForecastItemFragment extends Fragment {
    private static final String FORECAST_ITEM_KEY = "forecastItemKey";

    public static ForecastItemFragment newInstance(DailyForecastItem item) {
        ForecastItemFragment f = new ForecastItemFragment();
        Bundle args = new Bundle();
        args.putParcelable(FORECAST_ITEM_KEY, item);
        f.setArguments(args);
        return f;
    }

    private DailyForecastItem dailyForecastItem;
    private View rainBlock;
    private View snowBlock;
    private TextView tvWithoutPrecipitation;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        dailyForecastItem = (DailyForecastItem) bundle.getParcelable(FORECAST_ITEM_KEY);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentDailyForecastItemBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_daily_forecast_item, container, false);

        binding.iconWeather.setImageResource(dailyForecastItem.getWeatherIconId());
        binding.tvWeatherDescription.setText(dailyForecastItem.getWeatherDescription());
        binding.tvMorningTemp.setText(dailyForecastItem.getMorningTemp());
        binding.tvDayTemp.setText(dailyForecastItem.getDayTemp());
        binding.tvEveningTemp.setText(dailyForecastItem.getEveningTemp());
        binding.tvNightTemp.setText(dailyForecastItem.getNightTemp());
        binding.tvWind.setText(dailyForecastItem.getWindDescription());
        binding.iconWindDirect.setImageResource(dailyForecastItem.getWindDirectIconId());

        rainBlock = binding.rainBlock;
        snowBlock = binding.snowBlock;
        tvWithoutPrecipitation = binding.tvWithoutPrecipitation;

        if (dailyForecastItem.isWithoutPrecipitation()) {
            rainBlock.setVisibility(View.GONE);
            snowBlock.setVisibility(View.GONE);
            tvWithoutPrecipitation.setVisibility(View.VISIBLE);
        } else {
            tvWithoutPrecipitation.setVisibility(View.GONE);
            if (dailyForecastItem.getRain() != null) {
                rainBlock.setVisibility(View.VISIBLE);
                binding.tvRain.setText(dailyForecastItem.getRain());
            }
            if (dailyForecastItem.getSnow() != null) {
                snowBlock.setVisibility(View.VISIBLE);
                binding.tvSnow.setText(dailyForecastItem.getSnow());
            }
        }

        binding.tvPressure.setText(dailyForecastItem.getPressure());
        binding.tvHumidity.setText(dailyForecastItem.getHumidity());

        return binding.getRoot();
    }
}
