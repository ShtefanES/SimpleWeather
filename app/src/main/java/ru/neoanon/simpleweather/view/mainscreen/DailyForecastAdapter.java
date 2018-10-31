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
import ru.neoanon.simpleweather.databinding.ItemShortDailyForecastBinding;
import ru.neoanon.simpleweather.model.DailyForecastShortItem;

/**
 * Created by eshtefan on  02.10.2018.
 */

public class DailyForecastAdapter extends RecyclerView.Adapter<DailyForecastAdapter.MyViewHolder> {
    public interface OnDailyForecastClickListener {
        void onDailyForecastClick(int position);
    }

    private List<DailyForecastShortItem> forecastsList;
    private DailyForecastAdapter.OnDailyForecastClickListener clickListener;
    private Context context;


    @Inject
    public DailyForecastAdapter(Context context) {
        forecastsList = new ArrayList<>();
        this.context = context;
    }

    @NonNull
    @Override
    public DailyForecastAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemShortDailyForecastBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_short_daily_forecast, parent, false);

        return new DailyForecastAdapter.MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DailyForecastAdapter.MyViewHolder holder, int position) {
        final DailyForecastShortItem dailyForecast = forecastsList.get(position);

        holder.binding.tvDayOfWeek.setText(dailyForecast.getDayOfWeek());
        holder.binding.tvTemp.setText(dailyForecast.getTemp());
        holder.binding.tvDayOfWeek.setTextColor(context.getResources().getColor(dailyForecast.getDayOfWeekColorId()));

        if (position == 0) {
            holder.binding.tvDayOfMonth.setText(context.getString(R.string.today));
        } else if (position == 1) {
            holder.binding.tvDayOfMonth.setText(context.getString(R.string.tomorrow));
        } else {
            holder.binding.tvDayOfMonth.setText(dailyForecast.getDayOfMonth());
        }

        holder.binding.iconWeather.setImageResource(dailyForecast.getIconId());
    }

    public void setClickListener(OnDailyForecastClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void updateData(List<DailyForecastShortItem> forecastsList) {
        this.forecastsList.clear();
        this.forecastsList.addAll(forecastsList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return forecastsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemShortDailyForecastBinding binding;

        public MyViewHolder(ItemShortDailyForecastBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            this.binding.getRoot().setOnClickListener(view -> {
                if (clickListener != null) {
                    clickListener.onDailyForecastClick(getAdapterPosition());
                }
            });
        }
    }
}