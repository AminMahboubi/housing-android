package com.aminmahboubi.housing;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.aminmahboubi.housing.activity.AddHouseActivity;
import com.aminmahboubi.housing.api.HouseAPI;
import com.aminmahboubi.housing.model.House;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    final String TAG = "MainActivity";

    private GoogleMap mMap;

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show());

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent AddHouseIntent = new Intent(getApplicationContext(), AddHouseActivity.class);
                startActivity(AddHouseIntent);
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        final ProgressDialog dialog = new ProgressDialog(MainActivity.this);
        dialog.setMessage("Loading House Data, Please Wait...");

        new AsyncTask<Void, Void, ArrayList<House>>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog.show();

            }

            @Override
            protected void onPostExecute(ArrayList<House> houses) {
                super.onPostExecute(houses);
                dialog.dismiss();
                if (houses == null) {
                    Toast.makeText(getApplicationContext(), "Could'nt Fetch Data, Please Check Your Connection!", Toast.LENGTH_LONG).show();
                    return;
                }

                Toast.makeText(getApplicationContext(), "FUCK", Toast.LENGTH_LONG).show();

                mapFragment.getMapAsync(googleMap -> {
                    mMap = googleMap;
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(45.46, 9.19), 11));

                    Geocoder coder = new Geocoder(getApplicationContext());
                    for (House house : houses) {

                        try {
                            List<Address> address = coder.getFromLocationName(house.getAddress() + " Milan, IT", 1);
                            LatLng latLng = new LatLng(address.get(0).getLatitude(), address.get(0).getLongitude());
                            mMap.addMarker(new MarkerOptions().position(latLng).title(house.getName()));

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                });

            }

            @Override
            protected ArrayList<House> doInBackground(Void... voids) {
                try {
                    ArrayList<House> houses = HouseAPI.getInstance(getApplicationContext()).getAllSync();

                    Geocoder coder = new Geocoder(getApplicationContext());
                    for (House house : houses) {
                        try {
                            List<Address> address = coder.getFromLocationName(house.getAddress() + " Milan, Italy", 1);
                            house.setLat(address.get(0).getLatitude());
                            house.setLng(address.get(0).getLongitude());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    return houses;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }.execute();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
