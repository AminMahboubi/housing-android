package com.aminmahboubi.housing;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.aminmahboubi.housing.api.HouseAPI;
import com.aminmahboubi.housing.model.House;
import com.aminmahboubi.housing.view.ListFragment;
import com.aminmahboubi.housing.view.MapFragment;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 123;
    final String TAG = "MainActivity";
    private ArrayList<House> houses;
    private MapFragment mapFragment;
    private ListFragment listFragment;
    private SearchView searchView;
    private DrawerLayout drawer;

    private FirebaseAuth mAuth;
    private TextView userId;
    private TextView signOut;
    private View navHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initDrawer();
        initNavigation();
        initFab();

        mAuth = FirebaseAuth.getInstance();
        new LoadDataTask().execute();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            navHeader.setOnClickListener(null);
            signOut.setVisibility(View.VISIBLE);
            signOut.setOnClickListener(v -> {
                Snackbar.make(drawer, "Signing out ...", Snackbar.LENGTH_SHORT).show();
                AuthUI.getInstance().signOut(getApplicationContext())
                        .addOnCompleteListener(task -> {
                            this.onStart();
                            Snackbar.make(drawer, "Signed Out!", Snackbar.LENGTH_SHORT).show();
                        });
            });

            if (user.getProviders().get(0).equals("phone")) {
                userId.setText(user.getPhoneNumber());
            } else {
                userId.setText(user.getEmail());
            }
        } else {
            userId.setText(R.string.sign_in);
            signOut.setVisibility(View.GONE);
            navHeader.setOnClickListener(v -> signIn());
        }
    }

    @Override
    public void onBackPressed() {
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
        return item.getItemId() == R.id.action_search || super.onOptionsItemSelected(item);
    }

    private void initNavigation() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        userId = navigationView.getHeaderView(0).findViewById(R.id.user_id);
        signOut = navigationView.getHeaderView(0).findViewById(R.id.sign_out);
        navHeader = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (houses == null) {
                Toast.makeText(getApplicationContext(), R.string.async_null, Toast.LENGTH_SHORT).show();

            } else if (id == R.id.nav_fav) {
                Intent intent = new Intent(MainActivity.this, FavouriteActivity.class);
                intent.putExtra("house", houses);
                startActivity(intent);

            } else if (id == R.id.nav_owned) {
                Intent intent = new Intent(MainActivity.this, OwnedHouseActivity.class);
                startActivity(intent);

            } else if (id == R.id.nav_new) {
                newHouseActivity();
                return false;
            }
            drawer.closeDrawer(GravityCompat.START);
            return false;
        });
    }

    private void initFab() {
        findViewById(R.id.fab).setOnClickListener(v -> newHouseActivity());
    }

    private void initDrawer() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void newHouseActivity() {
        if (houses == null) {
            Toast.makeText(getApplicationContext(), R.string.async_null, Toast.LENGTH_SHORT).show();
        } else if (mAuth.getCurrentUser() == null) {
            drawer.openDrawer(Gravity.LEFT);
            Snackbar.make(drawer, "To post a house, you have to Sign in first!", Snackbar.LENGTH_SHORT).show();
        } else {
            Intent AddHouseIntent = new Intent(getApplicationContext(), AddHouseActivity.class);
            startActivity(AddHouseIntent);
        }
    }

    private void signIn() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.PhoneBuilder().setDefaultCountryIso("it").build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());
        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers).build(), RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                Snackbar.make(drawer, "Successfully Signed In!", Snackbar.LENGTH_SHORT).show();
            } else {
                Snackbar.make(drawer, "Sign In Failed!", Snackbar.LENGTH_INDEFINITE).setActionTextColor(Color.RED)
                        .setAction("RETRY", view -> signIn()).show();
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class LoadDataTask extends AsyncTask<Void, Void, ArrayList<House>> {
        ProgressDialog dialog;

        public LoadDataTask() {
            this.dialog = new ProgressDialog(MainActivity.this);
            dialog.setMessage("Loading Data, Please Wait...");
            dialog.setCancelable(false);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }

        @Override
        protected void onPostExecute(ArrayList<House> houseList) {
            super.onPostExecute(houseList);
            if (houseList == null) {
                cancel(true);
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                Snackbar.make(drawer, "No internet connection!", Snackbar.LENGTH_INDEFINITE).setActionTextColor(Color.RED)
                        .setAction("RETRY", view -> new LoadDataTask().execute()).show();
            } else {
                houses = houseList;
                mapFragment = MapFragment.newInstance(houses);
                listFragment = ListFragment.newInstance(houses);

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_holder, mapFragment).commit();
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        }

        @Override
        protected ArrayList<House> doInBackground(Void... voids) {
            try {
                ArrayList<House> houses = HouseAPI.getInstance(getApplicationContext()).getAll();
                return houses;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

}