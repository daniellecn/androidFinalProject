package com.example.finalproject.Model;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Danielle Cohen on 24/01/2017.
 */

public class DessertSql {
    private static final String DESSERTS_TABLE = "DESSERTS";
    private static final String ID = "ID";
    private static final String NAME = "NAME";
    private static final String DESC = "DESC";
    private static final String IMAGE_URL = "IMG_URL";
    private static final String COST = "COST";
    private static final String DATES = "DATES";

    public static void create(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + DESSERTS_TABLE + " (" +
                ID          + " NUM PRIMARY KEY," +
                NAME        + " TEXT," +
                DESC        + " TEXT," +
                IMAGE_URL   + " TEXT," +
                COST        + " TEXT," +
                DATES       + " TEXT );");
    }

    public static void dropTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE " + DESSERTS_TABLE);
    }

    public static void addDessert(SQLiteDatabase db, Dessert dessert){
        ContentValues values = new ContentValues();

        // Set values
        values.put(ID, dessert.getId());
        values.put(NAME, dessert.getName());
        values.put(DESC, dessert.getDescription());
        values.put(IMAGE_URL, dessert.getImageUrl());
        values.put(COST, dessert.getCost());
        values.put(DATES, dessert.getDatesAvailable());

        // Add to local db
        long rowId = db.insertWithOnConflict(DESSERTS_TABLE, ID, values,SQLiteDatabase.CONFLICT_REPLACE);
        if (rowId <= 0) {
            Log.e("SQLite","fail to insert into student");
        }
    }

    public static Dessert getDessertById(SQLiteDatabase db, String id)    {
        // Set the selection parameters
        String[] selectArg = {id};

        // Get the dessert
        Cursor cursor = db.query(DESSERTS_TABLE, null, ID + " = ?", selectArg, null, null, null );
        Dessert dessert = null;

        if (cursor.moveToFirst() == true){
            // Create the dessert object
            dessert = new Dessert(cursor.getInt(cursor.getColumnIndex(ID)),
                    cursor.getString(cursor.getColumnIndex(NAME)),
                    cursor.getString(cursor.getColumnIndex(DESC)),
                    cursor.getString(cursor.getColumnIndex(IMAGE_URL)),
                    cursor.getString(cursor.getColumnIndex(COST)),
                    cursor.getString(cursor.getColumnIndex(DATES)));
        }
        return dessert;
    }

    public static List<Dessert> getAllDesserts(SQLiteDatabase db){
        List<Dessert> dessertList = null;

        // Get all the desserts
        Cursor cursor = db.query(DESSERTS_TABLE, null, null, null, null, null, null);

        // Create the list
        return getDessertListFromCourse(cursor);
    }

    private static List<Dessert> getDessertListFromCourse(Cursor cursor){
        Dessert dessert = null;
        List<Dessert> data = new LinkedList<Dessert>();

        // If data selected
        if (cursor.moveToFirst()){
            // Get indexes
            int idIndex = cursor.getColumnIndex(ID);
            int nameIndex = cursor.getColumnIndex(NAME);
            int descIndex = cursor.getColumnIndex(DESC);
            int imageIndex = cursor.getColumnIndex(IMAGE_URL);
            int costIndex = cursor.getColumnIndex(COST);
            int datesIndex = cursor.getColumnIndex(DATES);

            // Move on the selected data and create list of desserts
            do {
                dessert = new Dessert(cursor.getInt(idIndex),
                        cursor.getString(nameIndex),
                        cursor.getString(descIndex),
                        cursor.getString(imageIndex),
                        cursor.getString(costIndex),
                        cursor.getString(datesIndex));

                data.add(dessert);
            } while (cursor.moveToNext());
        }

        return data;
    }

    public static double getLastUpdateDate(SQLiteDatabase db){
        return LastUpdateSql.getLastUpdate(db,DESSERTS_TABLE);
    }

    public static void setLastUpdateDate(SQLiteDatabase db, double date){
        LastUpdateSql.setLastUpdate(db,DESSERTS_TABLE, date);
    }
}
