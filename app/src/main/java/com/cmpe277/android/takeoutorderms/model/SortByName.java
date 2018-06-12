package com.cmpe277.android.takeoutorderms.model;

import java.util.Comparator;

public class SortByName implements Comparator<Item> {
    @Override
    public int compare(Item a, Item b) {
        return a.getName().compareToIgnoreCase(b.getName());
    }
}
