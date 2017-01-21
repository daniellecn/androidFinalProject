package com.example.finalproject.Model;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Danielle Cohen on 04/01/2017.
 */

public class Model {
    final private static Model instance = new Model();
    private List<Dessert> dessertData = new LinkedList<Dessert>();

    private ModelFirebase modelFirebase = new ModelFirebase();
    private UserFirebase userFirebase = new UserFirebase();

    public static Model instance(){
        return instance;
    }

    public interface logInListener {
        void onComplete(boolean isLogIn);
    }

    public interface signUpListener{
        void onComplete(boolean isExist);
    }

    public interface successListener {
        public void onResult(boolean result);
    }

    private Model() {
        for (int i =0;i<10;i++){
            Dessert st = null;
            List<Date> dates = new LinkedList<Date>();
            dates.add(new Date(2016, 12, 20));

            dessertData.add(new Dessert(i, "name", "address", 0.5, dates));
        }
    }

    public List<Dessert> getDessertData() {
        return dessertData;
    }

    public void logIn(User user, logInListener listener){
        modelFirebase.userLogIn(user, listener);
    }

    public void signUp(final User user, final successListener listener){
        modelFirebase.userSignUp(user, listener);
    }

    public void addDessert(Dessert dessert){
        //modelFirebase.addDessert(dessert);
    }
}
