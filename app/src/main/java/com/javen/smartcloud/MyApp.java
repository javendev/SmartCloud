package com.javen.smartcloud;

import android.app.Application;

import com.orhanobut.logger.Logger;

/**
 * Created by Javen on 16/8/2.
 */
public class MyApp extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        Logger.init("Javen205");
    }
}
