package com.example.finalproject.Model;

import java.util.Date;
import java.util.List;

/**
 * Created by Danielle Cohen on 04/01/2017.
 */

public class Dessert {
    private int id;
    private String name;
    private String description;
    private double cost;
    private List<Date> datesAvailable;

    public Dessert(int id, String name, String description, double cost, List<Date> datesAvailable) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.cost = cost;
        this.datesAvailable = datesAvailable;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public List<Date> getDatesAvailable() {
        return datesAvailable;
    }

    public void setDatesAvailable(List<Date> datesAvailable) {
        this.datesAvailable = datesAvailable;
    }
}
