package com.cmpe277.android.takeoutorderms.model;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a category.
 */

public class Category {
    int image;

    String name;

    List<Item> itemList;

    public Category(int image, String name) {
        this.image = image;
        this.name = name;
        itemList = new ArrayList<>();
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Item> getItemList() {
        return itemList;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }
}
