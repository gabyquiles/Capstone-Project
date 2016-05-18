package com.gabyquiles.eventy;

import com.firebase.client.Firebase;

/**
 * Created by gabrielquiles-perez on 5/9/16.
 */
public class EventyApplication extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //TODO: Set persistence of DB
        Firebase.setAndroidContext(this);
    }
}
