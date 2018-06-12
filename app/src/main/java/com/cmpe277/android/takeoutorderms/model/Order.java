package com.cmpe277.android.takeoutorderms.model;


import com.google.firebase.database.IgnoreExtraProperties;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


@IgnoreExtraProperties
public class Order {

    private String email;
    private String userID;
    private String orderID;
    private String totalPrice;
    private String status;
    private String pickupDate;
    private String placeDate;
    private ArrayList<CartItem> orderItems;

    private String fulfillmentStartTime;
    private String readyTime;
    private Boolean isValid;


    public Order() {

    }

    public Boolean isValid() {
        return isValid;
    }

    public void setValid(Boolean valid) {
        isValid = valid;
    }

    public Order(String userID, String email, String orderID, String totalPrice, String status,
                 String pickupDate, String placeDate, ArrayList<CartItem> orderItems) {
        this.userID = userID;
        this.email = email;
        this.orderID = orderID;
        this.totalPrice = totalPrice;
        this.status = status;
        this.pickupDate = pickupDate;
        this.placeDate = placeDate;
        this.orderItems = orderItems;
        this.isValid = Boolean.FALSE;

    }

    public ArrayList<CartItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(ArrayList<CartItem> orderItems) {
        this.orderItems = orderItems;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }


    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getTotalPrice() {
        return this.totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getPickupDate() {
        return pickupDate;
    }

    public void setPickupDate(String pickupDate) {
        this.pickupDate = pickupDate;
    }


    public String getPlaceDate() {
        return placeDate;
    }

    public void setPlaceDate(String placeDate) {
        this.placeDate = placeDate;
    }

    public String getFulfillmentStartTime() {
        return fulfillmentStartTime;
    }

    public void setFulfillmentStartTime(String fulfillmentStartTime) {
        this.fulfillmentStartTime = fulfillmentStartTime;
    }



    public String getReadyTime() {
        return readyTime;
    }


    public void setReadyTime(String readyTime) {
        this.readyTime = readyTime;
    }

}
