package com.gabyquiles.eventy;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by gabrielquiles-perez on 5/9/16.
 */
public class EventyApplication extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Set persistence of DB
//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
