package com.example.finalproject.Model;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Danielle Cohen on 17/01/2017.
 */

public class ModelFirebase {

    public void addUser(User user){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users").child(user.getName());
        myRef.setValue(user);
    }

    public void userLogIn(User user, Model.logInListener listener){
        UserFirebase.userLogIn(user, listener);
    }

    public void userSignUp(User user, Model.signUpListener listener){
        UserFirebase.userSignUp(user, listener);
    }
}
