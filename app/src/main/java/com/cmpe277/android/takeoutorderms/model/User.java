package com.cmpe277.android.takeoutorderms.model;


import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class User {



    private String useId;
    private String username;
    private String email;

    private Map<String, Long> cart;

    public User() {

    }

    public User(String useId, String username, String email) {
        this.useId = useId;
        this.username = username;
        this.email = email;
        cart = new HashMap<>();
    }

    public Map<String, Long> getCartItems() {
        return cart;
    }

    public void setCartItems(Map<String, Long> cartItems) {
        this.cart = cartItems;
    }

    public String getUseId() {
        return useId;
    }

    public void setUseId(String useId) {
        this.useId = useId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
