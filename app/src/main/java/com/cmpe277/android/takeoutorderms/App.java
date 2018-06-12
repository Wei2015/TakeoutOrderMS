package com.cmpe277.android.takeoutorderms;


import android.app.Application;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;

/**
 * This class make GoogleSignInClient global access in my app.
 */

public class App extends Application {


    private static App mInstance;
    private GoogleSignInClient mGoogleApiClient;

    public static synchronized App getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
    }

    public GoogleSignInClient getmGoogleSignInClient() {
        return mGoogleApiClient;
    }

    public void setmGoogleSignInClient(GoogleSignInClient mGoogleApiClient) {
        this.mGoogleApiClient = mGoogleApiClient;
    }
}
