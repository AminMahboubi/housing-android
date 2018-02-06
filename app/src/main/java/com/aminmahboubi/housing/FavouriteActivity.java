package com.aminmahboubi.housing;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.aminmahboubi.housing.model.Favourite;
import com.aminmahboubi.housing.model.House;
import com.aminmahboubi.housing.view.HouseAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.FluentIterable.from;

public class FavouriteActivity extends AppCompatActivity {
    final String TAG = "FavouriteActivity";

    private ArrayList<House> houseList;
    private ArrayList<String> allFav;
    private RecyclerView recyclerView;
    private HouseAdapter houseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.favourites);

        houseList = (ArrayList<House>) getIntent().getSerializableExtra("house");

        recyclerView = findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            GestureDetector gestureDetector = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
            });

            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
                View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
                if (child != null && gestureDetector.onTouchEvent(motionEvent)) {
                    Intent intent = new Intent(FavouriteActivity.this, HouseActivity.class);
                    intent.putExtra("house", houseAdapter.getItem(recyclerView.getChildAdapterPosition(child)));
                    startActivity(intent);
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {}

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {}
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        allFav = Favourite.getInstance(getApplicationContext()).getAllFav();
        List<House> favList = from(houseList).filter(house -> allFav.contains(house.get_id())).toList();
        houseAdapter = new HouseAdapter(favList);
        recyclerView.setAdapter(houseAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}