package com.aminmahboubi.housing;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.aminmahboubi.housing.api.HouseAPI;
import com.aminmahboubi.housing.model.House;
import com.aminmahboubi.housing.view.HouseAdapter;

import java.util.ArrayList;

public class OwnedHouseActivity extends AppCompatActivity {
    final String TAG = "OwnedHouseActivity";

    private ArrayList<House> houses;
    private RecyclerView recyclerView;
    private HouseAdapter houseAdapter;

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owned_house);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.my_houses);

        recyclerView = findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        final ProgressDialog dialog = new ProgressDialog(OwnedHouseActivity.this);
        dialog.setMessage("Loading Data, Please Wait...");
        dialog.setCancelable(false);

        new AsyncTask<Void, Void, ArrayList<House>>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog.show();
            }

            @Override
            protected void onPostExecute(ArrayList<House> houseList) {
                super.onPostExecute(houseList);
                if (houseList == null) {
                    Toast.makeText(getApplicationContext(), "Could'nt Fetch Data, Please Check Your Connection!", Toast.LENGTH_LONG).show();
                    return;
                }
                houses = houseList;
                houseAdapter = new HouseAdapter(houses);
                recyclerView.setAdapter(houseAdapter);

                dialog.dismiss();
            }

            @Override
            protected ArrayList<House> doInBackground(Void... voids) {
                try {
                    ArrayList<House> houses = HouseAPI.getInstance(getApplicationContext()).getUserHouses();
                    return houses;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

        }.execute();

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
                    Intent intent = new Intent(OwnedHouseActivity.this, HouseActivity.class);
                    intent.putExtra("house", houseAdapter.getItem(recyclerView.getChildAdapterPosition(child)));
                    intent.putExtra("editable", true);
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
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
