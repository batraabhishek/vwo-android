package com.vwo.sampleapp.app;

import android.app.Application;
import android.os.StrictMode;

/**
 * Created by aman on 16/08/17.
 */

public class VWOApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
//        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
//                .detectAll()
//                .build());
    }
}
