package com.cmpe277.android.takeoutorderms.model;


import java.util.ArrayList;

import static com.cmpe277.android.takeoutorderms.R.*;

public class CategoryCollection {



    //get data for adapter
    public static ArrayList<Category> getData() {
        ArrayList<Category> data = new ArrayList<>();

        Category appetizer = new Category(drawable.appetizer, Constant.APPETIZERS);
        Category dessert = new Category(drawable.dessert, Constant.DESSERTS);
        Category drink = new Category(drawable.drink, Constant.DRINKS);
        Category main = new Category(drawable.main, Constant.MAIN_COURSES);

        data.add(appetizer);
        data.add(dessert);
        data.add(main);
        data.add(drink);

        return data;
    }
}
