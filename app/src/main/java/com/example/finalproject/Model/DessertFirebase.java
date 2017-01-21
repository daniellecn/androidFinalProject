package com.example.finalproject.Model;

import android.graphics.PorterDuff;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Danielle Cohen on 22/01/2017.
 */

public class DessertFirebase {

    public static void addDessert(Dessert dessert, Model.successListener listener){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("desserts").child(dessert.getName());
        myRef.setValue(dessert);
    }
}
