package com.aminmahboubi.housing.api;

import com.aminmahboubi.housing.model.House;

import java.util.ArrayList;

/**
 * Created by amin on 1/26/18.
 */

public interface GetAllHouseAPIListener {

    void onSuccess(ArrayList<House> houses);

    void onError(Exception e);
}
