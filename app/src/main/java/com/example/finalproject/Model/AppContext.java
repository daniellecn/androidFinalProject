package com.example.finalproject.Model;

import android.app.Application;
import android.content.Context;

/**
 * Created by Danielle Cohen on 24/01/2017.
 */

public class AppContext extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        AppContext.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return AppContext.context;
    }

}