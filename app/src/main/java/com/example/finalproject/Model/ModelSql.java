package com.example.finalproject.Model;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by Danielle Cohen on 24/01/2017.
 */

public class ModelSql {
    private SQLiteOpenHelper helper;
    private int version = 7;

    public ModelSql() {
        helper = new Helper(AppContext.getAppContext());
    }

    public SQLiteDatabase getWritableDB(){
        return helper.getWritableDatabase();
    }

    public SQLiteDatabase getReadbleDB(){
        return helper.getReadableDatabase();
    }

    public void addDessert(Dessert dessert){
        DessertSql.addDessert(helper.getWritableDatabase(), dessert);
    }

    public Dessert getDessertById(String id){
        return DessertSql.getDessertById(helper.getReadableDatabase(), id);
    }

    public List<Dessert> getAllDesserts(){
        return DessertSql.getAllDesserts(helper.getReadableDatabase());
    }

    private void saveImageToFile(Bitmap imageBitmap, String imageFileName){
        FileOutputStream fos;
        OutputStream out = null;
        try {
            File dir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES);
            if (!dir.exists()) {
                dir.mkdir();
            }
            File imageFile = new File(dir,imageFileName);

            imageFile.createNewFile();

            out = new FileOutputStream(imageFile);
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();

            //add the picture to the gallery so we dont need to manage the cache size
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(imageFile);
            mediaScanIntent.setData(contentUri);
            AppContext.getAppContext().sendBroadcast(mediaScanIntent);
            Log.d("TAG","add image to cache: " + imageFileName);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class Helper extends SQLiteOpenHelper {


        public Helper(Context context) {
            super(context, "database.db", null, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            DessertSql.create(db);
            LastUpdateSql.create(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int i, int i1) {
            DessertSql.dropTable(db);
            LastUpdateSql.drop(db);
            onCreate(db);
        }
    }
}
