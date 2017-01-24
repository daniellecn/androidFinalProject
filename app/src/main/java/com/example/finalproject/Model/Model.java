package com.example.finalproject.Model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;

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

    private Model() {
        for (int i =0;i<10;i++) {
            Dessert st = null;
            dessertData.add(new Dessert(i, "name", "address", "blabla", "0.5$", "01/01/2017 - 06/01/2017"));
        }
    }

    public interface LogInListener {
        void onComplete(boolean isLogIn);
    }

    public interface SignUpListener{
        void onComplete(boolean isExist);
    }

    public interface SuccessListener {
        public void onResult(boolean result);
    }

    public interface SaveImageListener{
        public void fail();
        public void complete(String url);
    }

    public interface GetImageListener{
        public void onSccess(Bitmap image);
        public void onFail();
    }

    public interface GetAllDessertsListener{
        void onComplete(List<Dessert> students);
    }

    public List<Dessert> getDessertData() {
        return dessertData;
    }

    public void logIn(User user, LogInListener listener){
        modelFirebase.userLogIn(user, listener);
    }

    public void signUp(final User user, final SuccessListener listener){
        modelFirebase.userSignUp(user, listener);
    }

    public void addDessert(Dessert dessert, final SuccessListener listener){
        modelFirebase.addDessert(dessert, listener);
    }

    public static int getNextDessertId() {
        return 0;
    }
}
