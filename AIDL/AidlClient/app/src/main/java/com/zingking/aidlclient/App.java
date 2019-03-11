package com.zingking.aidlclient;

import android.app.Application;
import android.content.Context;

/**
 * Copyright (c) 2019, Z.kai All rights reserved.
 * author：Z.kai
 * date：2019/3/11
 * description：
 */
public class App extends Application {
    private static App context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    public static Context getAppContext(){
        return context.getApplicationContext();
    }
}
