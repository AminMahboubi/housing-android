package com.aminmahboubi.housing.api;

import android.content.Context;
import android.util.Log;

import com.aminmahboubi.housing.model.House;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by amin on 1/26/18.
 */

public class HouseAPI {
    private static Context mContext;
    private static HouseAPI mInstance;
    private final String baseUrl = "http://dima.aminmahboubi.com/api/house";
    private final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();

    private HouseAPI(Context context) {
        mContext = context;
    }

    public static HouseAPI getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new HouseAPI(context);
        }
        return mInstance;
    }

    public void getAll(final GetAllHouseAPIListener getAllHouseAPIListener) {

        StringRequest getAll = new StringRequest(Request.Method.GET, baseUrl,
                response -> {
                    try {
                        ArrayList<House> houses = parseGetAllJSON(response);
                        getAllHouseAPIListener.onSuccess(houses);
                    } catch (JSONException e) {
                        getAllHouseAPIListener.onError(e);
                    }
                }, e -> getAllHouseAPIListener.onError(e));

        SingletonRequestQueue.getInstance(mContext).addToRequestQueue(getAll);
    }

    public ArrayList<House> getAllSync() throws ExecutionException, InterruptedException, JSONException {

        RequestFuture<String> requestFuture = RequestFuture.newFuture();
        StringRequest getAll = new StringRequest(Request.Method.GET, baseUrl, requestFuture, requestFuture);
        SingletonRequestQueue.getInstance(mContext).addToRequestQueue(getAll);

        String response = requestFuture.get();
        ArrayList<House> houses = parseGetAllJSON(response);
        return houses;
    }

    public void postHouse(House house) throws JSONException {
        JsonObjectRequest postHouse = new JsonObjectRequest(Request.Method.POST, baseUrl, house.toJSON(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("postHouse", "onResponse: " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                Log.e("postHouse", "onResponse: " + e);
            }
        });

        SingletonRequestQueue.getInstance(mContext).addToRequestQueue(postHouse);

    }

    private ArrayList<House> parseGetAllJSON(String response) throws JSONException {

        Type HouseArrayListType = new TypeToken<ArrayList<House>>() {
        }.getType();
        return gson.fromJson(response, HouseArrayListType);
    }
}