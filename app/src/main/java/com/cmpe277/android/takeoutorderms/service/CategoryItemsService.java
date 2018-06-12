package com.cmpe277.android.takeoutorderms.service;


import com.cmpe277.android.takeoutorderms.model.Item;
import com.google.firebase.database.DatabaseReference;

public class CategoryItemsService {

    DatabaseReference db;

    String nodeName;


    public CategoryItemsService(DatabaseReference db, String nodeName){
        this.db = db;
        this.nodeName = nodeName;
    }


    /**
     * Add a new Item into this Category node in Firebase real-time database
     */
    public void addItem(Item newItem) {
        //Creating new item node, which returns the unique key value
        String itemId = db.child(nodeName).push().getKey();
        //set id into Item object
        newItem.setId(itemId);
        //pushing item to the node using itemId
        db.child(nodeName).child(itemId).setValue(newItem);
    }

    /**
     * Remove an item of this Category node in Firebase real-time database
     */
    public void removeItem(String itemId) {
        //delete the itemId value
        db.child(nodeName).child(itemId).removeValue();

    }
}
