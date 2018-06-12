package com.cmpe277.android.takeoutorderms.service;

import com.cmpe277.android.takeoutorderms.model.Order;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class OrderItemsService {

    DatabaseReference db;
    DatabaseReference orderDB;
    String userId;
    ArrayList<Order> orderItemList;


    public OrderItemsService(DatabaseReference db, String userId) {
        this.db = db.child(userId);
        this.orderDB = db.child("orders");
        orderItemList = new ArrayList<>();
    }

    public ArrayList<Order> retrieve() {
        db. addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getKey().equalsIgnoreCase("orderList")){
                    fetchData(dataSnapshot);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return orderItemList;
    }


    private void fetchData(DataSnapshot dataSnapshot) {
        orderItemList.clear();


        for (DataSnapshot ds: dataSnapshot.getChildren()){
           orderItemList.add(ds.getValue(Order.class));
        }

    }
}
