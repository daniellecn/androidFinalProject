package com.example.finalproject.Model;

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

    public static Model instance(){
        return instance;
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
}
