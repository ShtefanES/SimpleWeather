package ru.neoanon.simpleweather.view.places;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.neoanon.simpleweather.R;
import ru.neoanon.simpleweather.data.source.local.db.location.RegionLocation;
import ru.neoanon.simpleweather.databinding.ItemPlaceBinding;
import ru.neoanon.simpleweather.utils.Constants;

/**
 * Created by eshtefan on  01.10.2018.
 */

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.MyViewHolder> {
    public interface OnPlaceClickListener {
        void onPlaceClick(RegionLocation regionLocation);

        void onDeletePlaceClick(long regionId);
    }

    private List<RegionLocation> placesList;
    private PlaceAdapter.OnPlaceClickListener clickListener;

    public PlaceAdapter(PlaceAdapter.OnPlaceClickListener clickListener) {
        this.clickListener = clickListener;
        placesList = new ArrayList<>();
    }

    @NonNull
    @Override
    public PlaceAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemPlaceBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_place, parent, false);
        return new PlaceAdapter.MyViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull PlaceAdapter.MyViewHolder holder, int position) {
        final RegionLocation regionLocation = placesList.get(position);
        holder.bind(regionLocation);

        TextView tvCurrentLocationTitle = holder.binding.tvCurrentLocationTitle;
        ImageView ivTrashCan = holder.binding.ivTrashCan;
        if (regionLocation.getId() == Constants.CURRENT_LOCATION_ID) {
            tvCurrentLocationTitle.setVisibility(View.VISIBLE);
            ivTrashCan.setVisibility(View.INVISIBLE);
        } else {
            tvCurrentLocationTitle.setVisibility(View.GONE);
            ivTrashCan.setVisibility(View.VISIBLE);
        }

        holder.itemView.setOnClickListener(view -> {
            if (clickListener != null) {
                clickListener.onPlaceClick(regionLocation);
            }
        });

        holder.binding.ivTrashCan.setOnClickListener(view -> {
            if (clickListener != null) {
                clickListener.onDeletePlaceClick(regionLocation.getId());
            }
        });
    }

    public void updateData(List<RegionLocation> placesList) {
        this.placesList.clear();
        this.placesList.addAll(placesList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return placesList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemPlaceBinding binding;

        public MyViewHolder(ItemPlaceBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(RegionLocation regionLocation) {
            binding.setRegionLocation(regionLocation);
            binding.executePendingBindings();
        }
    }
}