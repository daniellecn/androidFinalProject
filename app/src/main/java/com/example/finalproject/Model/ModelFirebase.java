package com.example.finalproject.Model;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.finalproject.Model.UserFirebase.addUser;

/**
 * Created by Danielle Cohen on 17/01/2017.
 */

public class ModelFirebase {

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

    public void getAllDesserts(final Model.GetAllDessertsListener listener){
        DessertFirebase.getAllDesserts(listener);
    }
}
