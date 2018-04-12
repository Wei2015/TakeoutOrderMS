package com.cmpe277.android.takeoutorderms.model;

import java.util.Date;
import java.util.List;

/**
 * Created by weiyao on 4/11/18.
 */

public class Item {

    private long itemId;
    private String name;
    private String picturePath;
    private double price;
    private int preparationTimeInMin;
    private int popularity;
    private Category category;

    private List<Date> orderRecord;

    public Item(String name, String picturePath, double price, int preparationTimeInMin, int popularity, Category category, List<Date> orderRecord) {
        this.name = name;
        this.picturePath = picturePath;
        this.price = price;
        this.preparationTimeInMin = preparationTimeInMin;
        this.popularity = popularity;
        this.category = category;
        this.orderRecord = orderRecord;
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public String getName() {
        return name;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public double getPrice() {
        return price;
    }
}

