package com.aminmahboubi.housing.model;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by amin on 1/29/18.
 */

public class UniqueIdentifier {

    public synchronized static String getUniqueID() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
}
