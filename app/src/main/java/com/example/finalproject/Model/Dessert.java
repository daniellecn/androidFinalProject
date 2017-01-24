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
    private String imageUrl;
    private String cost;
    private String datesAvailable;

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
}
