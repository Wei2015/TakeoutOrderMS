package com.cmpe277.android.takeoutorderms.model;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by weiyao on 4/11/18.
 */
@IgnoreExtraProperties
public class User {

    public String username;
    public String email;

    public User() {

    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }
}
