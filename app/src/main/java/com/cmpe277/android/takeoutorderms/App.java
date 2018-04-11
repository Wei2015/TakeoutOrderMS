package com.cmpe277.android.takeoutorderms;


import android.app.Application;

/**
 * Created by weiyao on 4/11/18.
 */

public class App extends Application {


        private GoogleApiHelper googleApiHelper;
        private static App mInstance;

        @Override
        public void onCreate() {
            super.onCreate();

            mInstance = this;
            googleApiHelper = new GoogleApiHelper(mInstance);
        }

        public static synchronized App getInstance() {
            return mInstance;
        }

        public GoogleApiHelper getGoogleApiHelperInstance() {
            return this.googleApiHelper;
        }
        public static GoogleApiHelper getGoogleApiHelper() {
            return getInstance().getGoogleApiHelperInstance();
        }


}
