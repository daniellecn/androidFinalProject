package com.example.finalproject.Model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Danielle Cohen on 04/01/2017.
 */

public class Model {
    final private static Model instance = new Model();


    private List<Dessert> dessertData = new LinkedList<Dessert>();

    private ModelFirebase remote;
    private ModelSql local;

    private static int currentKey;

    public static Model instance(){
        return instance;
    }

    private Model() {
//        for (int i =0;i<10;i++) {
//            Dessert st = null;
//            dessertData.add(new Dessert(i, "name", "address", "blabla", "0.5$", "01/01/2017 - 06/01/2017"));
//        }
        remote = new ModelFirebase();
        local = new ModelSql();
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
        public void onSuccess(Bitmap image);
        public void onFail();
    }

    public interface GetAllDessertsListener{
        void onComplete(List<Dessert> dessertList, int currentMaxKey);
        public void onCancel();
    }

    public interface GetAllDessertsAsynchListener{
        void onComplete(List<Dessert> dessertList);
        public void onCancel();
    }

    public interface GetDessertListener{
        void onComplete(Dessert dessert);
        public void onCancel();
    }

    public static void setCurrentKey(int currentKey) {
        Model.currentKey = currentKey;
    }

    public static int getCurrentKey() {
        return currentKey;
    }


    public List<Dessert> getDessertData() {
        return dessertData;
    }

    public void logIn(User user, LogInListener listener){
        remote.userLogIn(user, listener);
    }

    public void signUp(final User user, final SuccessListener listener){
        remote.userSignUp(user, listener);
    }

    public void addDessert(final Dessert dessert, Bitmap dessertImageBitmap, final SuccessListener listener){
        // Save the image on the SD-CARD
        ImageLocal.saveLocalImage(dessertImageBitmap, String.valueOf(dessert.getId()));

        // Upload image to firebase storage
        ImageFirebase.saveImage(dessertImageBitmap, String.valueOf(dessert.getId()), new SaveImageListener() {
            @Override
            public void fail() {
                listener.onResult(false);
            }

            @Override
            public void complete(String url) {
                dessert.setImageUrl(url);

                // Save the dessert to firebase database
                remote.addDessert(dessert, listener);

                listener.onResult(true);
            }
        });

         // Update the key
        setCurrentKey(getCurrentKey() + 1);
    }

    public void getAllDessertAsynch(final GetAllDessertsAsynchListener listener){
        // Get last update date
        final double lastUpdateDate = DessertSql.getLastUpdateDate(local.getReadbleDB());

        // Get all desserts records that where updated since last update date
        remote.getDessertsFromDate(lastUpdateDate, new GetAllDessertsListener() {
            @Override
            public void onComplete(List<Dessert> dessertList, int currentMaxKey) {
                // If there are new desserts in firebase
                if (dessertList != null && dessertList.size() > 0){

                    // Update the local db
                    double recentUpdate = lastUpdateDate;
                    for (Dessert dessert : dessertList){
                        DessertSql.addDessert(local.getWritableDB(), dessert);

                        if (dessert.getLastUpdated() > recentUpdate){
                            recentUpdate = dessert.getLastUpdated();
                        }
                    }

                    // Update the last update date
                    DessertSql.setLastUpdateDate(local.getWritableDB(), recentUpdate);

                    // Update the current key
                    if (getCurrentKey() < currentMaxKey){
                        setCurrentKey(currentMaxKey + 1);
                    }
                }

                // Return all Desserts from the updated local db
                List<Dessert> result = DessertSql.getAllDesserts(local.getReadbleDB());
                listener.onComplete(result);
            }

            @Override
            public void onCancel() {
                listener.onCancel();
            }
        });
    }
}
