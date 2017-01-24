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
    private int version = 2;

    public ModelSql() {
        helper = new Helper(AppContext.getAppContext());
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

    class Helper extends SQLiteOpenHelper {


        public Helper(Context context) {
            super(context, "database.db", null, version);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            DessertSql.create(sqLiteDatabase);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            DessertSql.dropTable(sqLiteDatabase);
            onCreate(sqLiteDatabase);
        }
    }
}
