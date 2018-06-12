package com.cmpe277.android.takeoutorderms.model;


import java.util.Comparator;

public class SortByPopularity implements Comparator<Item> {

    @Override
    public int compare(Item a, Item b) {
       return b.getPopularity()-a.getPopularity();
    }
}
