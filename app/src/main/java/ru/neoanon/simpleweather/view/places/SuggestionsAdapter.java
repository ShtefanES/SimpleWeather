package ru.neoanon.simpleweather.view.places;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ru.neoanon.simpleweather.R;
import ru.neoanon.simpleweather.data.source.local.db.location.RegionLocation;
import ru.neoanon.simpleweather.databinding.ItemSuggestionBinding;

/**
 * Created by eshtefan on  27.09.2018.
 */

public class SuggestionsAdapter extends RecyclerView.Adapter<SuggestionsAdapter.MyViewHolder> {
    public interface OnSuggestionClickListener {
        void onSuggestionClick(RegionLocation regionLocation);
    }

    private List<RegionLocation> suggestionList;
    private OnSuggestionClickListener clickListener;

    public SuggestionsAdapter(OnSuggestionClickListener clickListener) {
        this.clickListener = clickListener;
        suggestionList = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemSuggestionBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_suggestion, parent, false);
        return new MyViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final RegionLocation regionLocation = suggestionList.get(position);
        holder.bind(regionLocation);

        holder.itemView.setOnClickListener(view -> {
            if (clickListener != null) {
                clickListener.onSuggestionClick(regionLocation);
            }
        });
    }

    public void updateData(List<RegionLocation> suggestionList) {
        this.suggestionList.clear();
        this.suggestionList.addAll(suggestionList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return suggestionList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemSuggestionBinding binding;

        public MyViewHolder(ItemSuggestionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(RegionLocation regionLocation) {
            binding.setRegionLocation(regionLocation);
            binding.executePendingBindings();
        }
    }
}