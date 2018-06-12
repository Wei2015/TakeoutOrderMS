package com.cmpe277.android.takeoutorderms.model;

/**
 * Created by weiyao on 4/11/18.
 */

public class Item {

    private String id;

    private String name;
    private String picturePath;
    private Double price;
    private Integer preparationTime;
    private String category;
    private Integer calories;
    private int popularity;


    //Default constructor required for calls to DataSnapshot.getValue(Item.class)
    public Item () {

    }

    public Item (String name, String picturePath, Double price, Integer preparationTime, String category, Integer calories) {
        this.id = null;
        this.name = name;
        this.picturePath = picturePath;
        this.price = price;
        this.preparationTime = preparationTime;
        this.category = category;
        this.calories = calories;
        this.popularity = 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPreparationTime() {
        return preparationTime;
    }

    public String getCategory() {
        return category;
    }

    public String getName() {
        return name;

    }

    public String getPicturePath() {
        return picturePath;
    }

    public Double getPrice() {
        return price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setPreparationTime(Integer preparationTime) {
        this.preparationTime = preparationTime;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }
}



