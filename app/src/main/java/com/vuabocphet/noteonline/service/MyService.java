package com.vuabocphet.noteonline.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("SERVICE","ONSTART");
        return START_NOT_STICKY;

    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("SERVICE","ONCREATE");
    }
}
