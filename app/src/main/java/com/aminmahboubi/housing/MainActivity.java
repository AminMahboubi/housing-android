package com.aminmahboubi.housing;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.aminmahboubi.housing.api.HouseAPI;
import com.aminmahboubi.housing.model.House;
import com.aminmahboubi.housing.view.ListFragment;
import com.aminmahboubi.housing.view.MapFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    final String TAG = "MainActivity";

    private ArrayList<House> houses;
    private MapFragment mapFragment;
    private ListFragment listFragment;
    private SearchView searchView;

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(v -> {
            Intent AddHouseIntent = new Intent(getApplicationContext(), AddHouseActivity.class);
            startActivity(AddHouseIntent);
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        final ProgressDialog dialog = new ProgressDialog(MainActivity.this);
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
                    dialog.dismiss();
                    finish();
                    return;
                }

                houses = houseList;
                mapFragment = MapFragment.newInstance(houses);
                listFragment = ListFragment.newInstance(houses);

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_holder, mapFragment).commit();

                dialog.dismiss();
            }

            @Override
            protected ArrayList<House> doInBackground(Void... voids) {
                try {
                    ArrayList<House> houses = HouseAPI.getInstance(getApplicationContext()).getAllSync();
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
            if (!searchView.isIconified()) {
                searchView.setIconified(true);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_holder, mapFragment).commit();
            } else
                super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnSearchClickListener(v -> getSupportFragmentManager().beginTransaction().replace(R.id.fragment_holder, listFragment).commit());
        searchView.setOnCloseListener(() -> {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_holder, mapFragment).commit();
            return false;
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                listFragment.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                listFragment.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_fav) {
            Intent intent = new Intent(MainActivity.this, FavouriteActivity.class);
            intent.putExtra("house", houses);
            startActivity(intent);

        } else if (id == R.id.nav_owned) {
            Intent intent = new Intent(MainActivity.this, OwnedHouseActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

}
