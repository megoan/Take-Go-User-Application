package com.example.fotij.userapplication.controller;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by fotij on 11/01/2018.
 */

public class MyService extends IntentService {
    boolean isRun=false;
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public MyService(String name) {
        super(name);
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        isRun=true;

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                while (isRun) {
                    try {
                        sendBroadcast(new Intent(getBaseContext(),MyReceiver.class));
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        thread.start();
    }
}
