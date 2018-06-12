package com.cmpe277.android.takeoutorderms.model;

import java.util.Comparator;

public class SortByPrice implements Comparator<Item> {
    @Override
    public int compare(Item a, Item b){
        if (a.getPrice()-b.getPrice()>1E-7) {
            return 1;
        } else if (a.getPrice()-b.getPrice()< 0) {
            return -1;
        } else {
            return 0;
        }
    }
}
