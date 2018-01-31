package com.aminmahboubi.housing.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.aminmahboubi.housing.R;
import com.aminmahboubi.housing.model.House;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amin on 1/31/18.
 */

public class HouseAdapter extends RecyclerView.Adapter<HouseAdapter.ViewHolder> implements Filterable {
    private List<House> houseList;
    private List<House> houseListFiltered;


    public HouseAdapter(List<House> houseList) {
        this.houseList = houseList;
        this.houseListFiltered = houseList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.house_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        House house = houseListFiltered.get(position);

        holder.address.setText(house.getAddress());
        holder.renting.setText(String.format("Renting a %s", house.getHouseType().toString()));
        holder.campus.setText(String.format("Close to Campus %s", house.getCampus().toString()));
        holder.sex.setText(String.format("Preferred Sex is  %s", house.getPreferredSex().toString()));
        holder.price.setText(String.format("Rent: %s €/M", house.getPrice().toString()));
    }

    @Override
    public int getItemCount() {
        return houseListFiltered.size();
    }

    public House getItem(int position) {
        return houseListFiltered.get(position);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                String search = constraint.toString();

                if (search.isEmpty()) {
                    houseListFiltered = houseList;
                } else {
                    List<House> filteredList = new ArrayList<>();

                    for (House house : houseList) {
                        if (house.getAddress().toLowerCase().contains(search.toLowerCase())) {
                            filteredList.add(house);
                        }
                    }
                    houseListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = houseListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                houseListFiltered = (ArrayList<House>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView address;
        TextView renting;
        TextView campus;
        TextView sex;
        TextView price;

        ViewHolder(View view) {
            super(view);
            address = view.findViewById(R.id.address);
            renting = view.findViewById(R.id.renting);
            campus = view.findViewById(R.id.campus);
            sex = view.findViewById(R.id.sex);
            price = view.findViewById(R.id.price);
        }

    }

}
