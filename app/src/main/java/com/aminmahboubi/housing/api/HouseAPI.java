package com.aminmahboubi.housing.api;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.aminmahboubi.housing.model.House;
import com.aminmahboubi.housing.model.UniqueIdentifier;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;

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
    private final String userUrl = baseUrl + "/user/";
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


    public void getAll(final GetHouseAPIListener getHouseAPIListener) {

        StringRequest getAll = new StringRequest(Request.Method.GET, baseUrl,
                response -> {
                    try {
                        ArrayList<House> houses = parseGetAllJSON(response);
                        getHouseAPIListener.onSuccess(houses);
                    } catch (JSONException e) {
                        getHouseAPIListener.onError(e);
                    }
                }, e -> getHouseAPIListener.onError(e));

        SingletonRequestQueue.getInstance(mContext).addToRequestQueue(getAll);
    }

    public void postHouse(House house) throws JSONException {
        JsonObjectRequest postHouse = new JsonObjectRequest(Request.Method.POST, baseUrl, house.toJSON(mContext),
                response -> {
                    Log.d("postHouse", "onResponse: " + response);
                    Toast.makeText(mContext, response.toString(), Toast.LENGTH_SHORT).show();
                },
                e -> {
                    Log.e("postHouse", "onResponse: " + e);
                    Toast.makeText(mContext, e.toString(), Toast.LENGTH_SHORT).show();
                });

        SingletonRequestQueue.getInstance(mContext).addToRequestQueue(postHouse);
    }

    public String deleteHouse(House house) throws ExecutionException, InterruptedException, JSONException {
        String url = userUrl + UniqueIdentifier.getUniqueID(mContext) + "/" + house.get_id();
        RequestFuture<org.json.JSONObject> requestFuture = RequestFuture.newFuture();

        JsonObjectRequest getAll = new JsonObjectRequest(Request.Method.DELETE, url, null, requestFuture, requestFuture);
        SingletonRequestQueue.getInstance(mContext).addToRequestQueue(getAll);

        return requestFuture.get().getString("message");
    }

    public ArrayList<House> getAllSync() throws ExecutionException, InterruptedException, JSONException {
        return getAllHousesSync(baseUrl);
    }

    public ArrayList<House> getUserHousesSync() throws ExecutionException, InterruptedException, JSONException {
        String url = userUrl + UniqueIdentifier.getUniqueID(mContext);
        return getAllHousesSync(url);
    }

    private ArrayList<House> getAllHousesSync(String url) throws ExecutionException, InterruptedException, JSONException {

        RequestFuture<String> requestFuture = RequestFuture.newFuture();

        StringRequest getAll = new StringRequest(Request.Method.GET, url, requestFuture, requestFuture);
        SingletonRequestQueue.getInstance(mContext).addToRequestQueue(getAll);

        String response = requestFuture.get();
        ArrayList<House> houses = parseGetAllJSON(response);
        return houses;
    }

    private ArrayList<House> parseGetAllJSON(String response) throws JSONException {

        Type HouseArrayListType = new TypeToken<ArrayList<House>>() {
        }.getType();
        return gson.fromJson(response, HouseArrayListType);
    }

}