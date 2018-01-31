package com.aminmahboubi.housing.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aminmahboubi.housing.HouseActivity;
import com.aminmahboubi.housing.R;
import com.aminmahboubi.housing.model.House;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;


public class MapFragment extends Fragment {

    private static final String HOUSE = "house";

    private SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private ArrayList<House> houses;

    public MapFragment() {
    }

    public static MapFragment newInstance(ArrayList<House> houses) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putSerializable(HOUSE, houses);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            houses = (ArrayList<House>) getArguments().getSerializable(HOUSE);
        }
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();

            mapFragment.getMapAsync(googleMap -> {
                mMap = googleMap;
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(45.46, 9.19), 11));
                mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(getActivity()));

                BitmapDescriptor markerIcon = getBitmapFromVectorDrawable(getContext(), R.drawable.ic_place_black_24dp);

                for (House house : houses) {
                    mMap.addMarker(new MarkerOptions().position(house.getLatLng()).icon(markerIcon)).setTag(house);
                }

                mMap.setOnInfoWindowClickListener(marker -> {
                    House house = (House) marker.getTag();

                    Intent intent = new Intent(getActivity(), HouseActivity.class);
                    intent.putExtra("house", house);
                    startActivity(intent);
                });

            });

        }

        getChildFragmentManager().beginTransaction().replace(R.id.map, mapFragment).commit();

        return rootView;
    }

    private BitmapDescriptor getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

}
