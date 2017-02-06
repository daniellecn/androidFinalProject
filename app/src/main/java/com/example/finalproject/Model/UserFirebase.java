package com.example.finalproject.Model;

import android.graphics.PorterDuff;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Danielle Cohen on 20/01/2017.
 */

public class UserFirebase {

    public static void userLogIn(final User user, final Model.LogInListener listener){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users").child(user.getName());

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User readUser = dataSnapshot.getValue(User.class);

                if ((readUser != null) && (user.getPassword().equals(readUser.getPassword()))){
                    // TODO: return the user to the model
                    Model.instance().setConnectedUser(readUser);
                    listener.onComplete(true);
                }
                else{
                    listener.onComplete(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onComplete(false);
            }

        });
    }

    public static void userSignUp(User user, final Model.SignUpListener listener){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users").child(user.getName());

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User readUser = dataSnapshot.getValue(User.class);

                if (readUser == null){
                    listener.onComplete(false);
                }
                else{
                    listener.onComplete(true);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onComplete(true);
            }
        });
    }

    public static void addUser(User user){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users").child(user.getName());
        myRef.setValue(user);
    }

    public static void isUserNameExist(final String userName, final Model.SuccessListener listener){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users").child(userName);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User readUser = dataSnapshot.getValue(User.class);

                if (readUser == null){
                    listener.onResult(false);
                }
                else{
                    listener.onResult(true);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onResult(false);
            }

        });
    }

    public static void isUserMailExist(final String userMail, final Model.SuccessListener listener){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean result = false;
                for (DataSnapshot dstSnapshot : dataSnapshot.getChildren()) {
                    User readUser = dstSnapshot.getValue(User.class);

                    if (readUser.getMail().toString().equals(userMail)){
                        result = true;
                    }
                }

                listener.onResult(result);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onResult(false);
            }

        });
    }
}
