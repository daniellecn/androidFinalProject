package com.example.finalproject.Model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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


    public static void addDessert(SQLiteDatabase writeableDatabase, Dessert dessert){
        ContentValues values = new ContentValues();

        // Set values
        values.put(ID, dessert.getId());
        values.put(NAME, dessert.getName());
        values.put(DESC, dessert.getDescription());
        values.put(IMAGE_URL, dessert.getImageUrl());
        values.put(COST, dessert.getCost());
        values.put(DATES, dessert.getDatesAvailable());

        // Add to local db
        long rowId = writeableDatabase.insert(DESSERTS_TABLE, ID, values);
        if (rowId <= 0) {
            Log.e("SQLite","fail to insert into student");
        }
    }

    public static Dessert getDessertById(SQLiteDatabase readableDatabase, String id)    {
        // Set the selection parameters
        String[] selectArg = {id};

        // Get the dessert
        Cursor cursor = readableDatabase.query(DESSERTS_TABLE, null, ID + " = ?", selectArg, null, null, null );
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

    public static List<Dessert> getAllDesserts(SQLiteDatabase readableDatabase){
        List<Dessert> dessertList = null;

        // Get all the desserts
        Cursor cursor = readableDatabase.query(DESSERTS_TABLE, null, null, null, null, null, null);

        // Create the list
        return getDessertListFromCourse(cursor);
    }

    public static void create(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + DESSERTS_TABLE + " (" +
                ID          + " NUM PRIMARY KEY," +
                NAME        + " TEXT," +
                DESC        + " TEXT," +
                IMAGE_URL   + " TEXT," +
                COST        + " TEXT," +
                DATES       + " TEXT );");
    }

    public static void dropTable(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("DROP TABLE " + DESSERTS_TABLE);
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
}
