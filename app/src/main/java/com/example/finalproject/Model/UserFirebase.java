package com.example.finalproject.Model;

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

                if (user.getPassword().equals(readUser.getPassword())){
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
}
