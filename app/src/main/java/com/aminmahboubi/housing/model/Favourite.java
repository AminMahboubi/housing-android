package com.aminmahboubi.housing.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.aminmahboubi.housing.api.SingletonRequestQueue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by amin on 1/31/18.
 */

public class Favourite {
    private static Favourite mInstance;

    private static final String PREF_FAV = "PREF_FAV";
    private static SharedPreferences sharedPreferences;

    private Favourite(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_FAV, Context.MODE_PRIVATE);
    }

    public static synchronized Favourite getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new Favourite(context);
        }
        return mInstance;
    }

    private synchronized void setFav(String id){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(id, true);
        editor.apply();
    }

    public synchronized Boolean getFav(String id){
        return sharedPreferences.getBoolean(id, false);
    }

    public synchronized Boolean flipFav(String id){
        boolean fav = getFav(id);
        if (fav){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove(id);
            editor.apply();
        }
        else {
            setFav(id);
        }
        return !fav;
    }

    public synchronized ArrayList<String> getAllFav(){
        return new ArrayList<>(sharedPreferences.getAll().keySet());
    }
}
