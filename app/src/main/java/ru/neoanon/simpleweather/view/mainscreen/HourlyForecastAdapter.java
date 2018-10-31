package ru.neoanon.simpleweather.view.mainscreen;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ru.neoanon.simpleweather.R;
import ru.neoanon.simpleweather.databinding.ItemHeaderOfHourlyForecastBinding;
import ru.neoanon.simpleweather.databinding.ItemHourlyForecastBinding;
import ru.neoanon.simpleweather.model.CommonHourlyItem;
import ru.neoanon.simpleweather.model.HeaderHourlyForecastItem;
import ru.neoanon.simpleweather.model.HourlyForecastItem;

/**
 * Created by eshtefan on  08.10.2018.
 */

public class HourlyForecastAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<CommonHourlyItem> forecastsList;
    private static final int WEATHER_ITEM = 0;
    private static final int FORECAST_ITEM = 1;

    private Context context;

    @Inject
    public HourlyForecastAdapter(Context context) {
        forecastsList = new ArrayList<>();
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        CommonHourlyItem commonHourlyItem = forecastsList.get(position);
        if (commonHourlyItem instanceof HeaderHourlyForecastItem) {
            return WEATHER_ITEM;
        } else {
            return FORECAST_ITEM;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == WEATHER_ITEM) {
            ItemHeaderOfHourlyForecastBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_header_of_hourly_forecast, parent, false);
            return new WeatherHolder(binding);
        } else {
            ItemHourlyForecastBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_hourly_forecast, parent, false);
            return new ForecastHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof WeatherHolder) {
            WeatherHolder weatherHolder = (WeatherHolder) holder;
            HeaderHourlyForecastItem header = (HeaderHourlyForecastItem) forecastsList.get(position);
            weatherHolder.binding.iconWindDirect.setImageResource(header.getIconWindDirectId());
            weatherHolder.binding.tvWind.setText(header.getWindDescription());
            weatherHolder.binding.tvPressure.setText(header.getPressure());
            weatherHolder.binding.tvHumidity.setText(header.getHumidity());
        } else {
            ForecastHolder forecastHolder = (ForecastHolder) holder;
            HourlyForecastItem hourlyForecast = (HourlyForecastItem) forecastsList.get(position);
            forecastHolder.binding.tvTemp.setText(hourlyForecast.getTemp());
            if (position == 1) {
                forecastHolder.binding.tvTime.setText(context.getString(R.string.now));
            } else {
                forecastHolder.binding.tvTime.setText(hourlyForecast.getTime());
            }
            forecastHolder.binding.iconWeather.setImageResource(hourlyForecast.getIconId());
        }
    }

    public void updateData(List<CommonHourlyItem> forecastsList) {
        this.forecastsList.clear();
        this.forecastsList.addAll(forecastsList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return forecastsList.size();
    }

    public class ForecastHolder extends RecyclerView.ViewHolder {
        ItemHourlyForecastBinding binding;

        ForecastHolder(ItemHourlyForecastBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public class WeatherHolder extends RecyclerView.ViewHolder {
        ItemHeaderOfHourlyForecastBinding binding;

        WeatherHolder(ItemHeaderOfHourlyForecastBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}