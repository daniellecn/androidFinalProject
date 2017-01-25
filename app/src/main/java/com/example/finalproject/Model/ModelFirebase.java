package com.example.finalproject.Model;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by Danielle Cohen on 17/01/2017.
 */

public class ModelFirebase {

    public ModelFirebase() {
    }

    public void userLogIn(User user, Model.LogInListener listener){
        UserFirebase.userLogIn(user, listener);
    }

    public void userSignUp(final User user, final Model.SuccessListener listener){
        UserFirebase.userSignUp(user, new Model.SignUpListener() {
            @Override
            public void onComplete(boolean isExist) {
                if (isExist){
                    listener.onResult(false);
                }
                else{
                    UserFirebase.addUser(user);
                    listener.onResult(true);
                }
            }
        });
    }

    public void addDessert(Dessert dessert, Model.SuccessListener listener) {
        DessertFirebase.addDessert(dessert, listener);
    }

    public void getDessertsFromDate(double lastUpdateDate, final Model.GetAllDessertsListener listener){
        DessertFirebase.getDessertsFromDate(lastUpdateDate, listener);
    }

    public void getDessertById(double id, final Model.GetDessertListener listener){
        DessertFirebase.getDessertById(id, listener);
    }
}
