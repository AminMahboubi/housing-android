package com.aminmahboubi.housing.view;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.aminmahboubi.housing.R;
import com.aminmahboubi.housing.model.House;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by amin on 1/29/18.
 */

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private Activity context;

    public CustomInfoWindowAdapter(Activity context) {
        this.context = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = context.getLayoutInflater().inflate(R.layout.house_card, null);
        House house = (House) marker.getTag();

        TextView address = view.findViewById(R.id.address);
        TextView renting = view.findViewById(R.id.renting);
        TextView campus = view.findViewById(R.id.campus);
        TextView sex = view.findViewById(R.id.sex);
        TextView price = view.findViewById(R.id.price);

        address.setText(house.getAddress());
        renting.setText(String.format("Renting a %s", house.getHouseType().toString()));
        campus.setText(String.format("Close to Campus %s", house.getCampus().toString()));
        sex.setText(String.format("Preferred Sex is  %s", house.getPreferredSex().toString()));
        price.setText(String.format("Rent: %s €/M", house.getPrice().toString()));

        return view;
    }
}

