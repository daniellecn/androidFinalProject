package com.example.finalproject.Model;

import com.google.firebase.database.ServerValue;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Danielle Cohen on 04/01/2017.
 */

public class Dessert {
    private int id;
    private String name;
    private String description;
    private String imageUrl;
    private String cost;
    private String datesAvailable;
    private double lastUpdated;

    public Dessert() {
    }

    public Dessert(int id, String name, String description, String imageUrl, String cost, String datesAvailable) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.cost = cost;
        this.datesAvailable = datesAvailable;
    }

    public Dessert(Dessert dessert){
        this.id = dessert.getId();
        this.name = dessert.getName();
        this.description = dessert.getDescription();
        this.imageUrl = dessert.getImageUrl();
        this.cost = dessert.getCost();
        this.datesAvailable = dessert.getDatesAvailable();
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getDatesAvailable() {
        return datesAvailable;
    }

    public void setDatesAvailable(String datesAvailable) {
        this.datesAvailable = datesAvailable;
    }

    public double getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(double lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", getId());
        result.put("name", getName());
        result.put("description", getDescription());
        result.put("imageUrl", getImageUrl());
        result.put("cost", getCost());
        result.put("datesAvailable", getDatesAvailable());
        result.put("lastUpdated", ServerValue.TIMESTAMP);
        return result;
    }
}
