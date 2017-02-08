package com.example.finalproject.Model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

/**
 * Created by Danielle Cohen on 24/01/2017.
 */

public class ModelSql {
    private SQLiteOpenHelper helper;
    private int version = 7;

    public ModelSql() {
        helper = new Helper(AppContext.getAppContext());
        helper.onUpgrade(getWritableDB(), 7, 7);
    }

    public SQLiteDatabase getWritableDB() {
        return helper.getWritableDatabase();
    }

    public SQLiteDatabase getReadbleDB() {
        return helper.getReadableDatabase();
    }

    public void addDessert(Dessert dessert) {
        DessertSql.addDessert(helper.getWritableDatabase(), dessert);
    }

    public void updateDessert(Dessert dessert) {
        DessertSql.updateDessert(helper.getWritableDatabase(), dessert);
    }

    public Dessert getDessertById(int id) {
        return DessertSql.getDessertById(helper.getReadableDatabase(), String.valueOf(id));
    }

    public List<Dessert> getAllDesserts() {
        return DessertSql.getAllDesserts(helper.getReadableDatabase());
    }

    public void deleteDessert(int id) {
        DessertSql.deleteDessert(id, getWritableDB());
        ImageLocal.deleteLocalImage(String.valueOf(id));
    }

    public List<Dessert> getBySearch(String query) {
        return DessertSql.getDessertsBySearch(helper.getReadableDatabase(), query);
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
