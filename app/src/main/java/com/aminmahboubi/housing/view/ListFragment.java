package com.aminmahboubi.housing.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;

import com.aminmahboubi.housing.HouseActivity;
import com.aminmahboubi.housing.R;
import com.aminmahboubi.housing.model.House;

import java.util.ArrayList;

/**
 * Created by amin on 1/31/18.
 */

public class ListFragment extends Fragment {
    private static final String HOUSE = "house";

    private ArrayList<House> houses;
    private RecyclerView recyclerView;
    private HouseAdapter houseAdapter;

    public ListFragment() {
    }

    public static ListFragment newInstance(ArrayList<House> houses) {
        ListFragment fragment = new ListFragment();
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

        View rootView = inflater.inflate(R.layout.fragment_list, container, false);

        recyclerView = rootView.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        houseAdapter = new HouseAdapter(houses);
        recyclerView.setAdapter(houseAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            GestureDetector gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
            });

            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
                View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
                if (child != null && gestureDetector.onTouchEvent(motionEvent)) {
                    Intent intent = new Intent(getActivity(), HouseActivity.class);
                    intent.putExtra("house", houseAdapter.getItem(recyclerView.getChildAdapterPosition(child)));
                    startActivity(intent);
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            }

        });

        return rootView;
    }

    public Filter getFilter() {
        return houseAdapter.getFilter();
    }

}
