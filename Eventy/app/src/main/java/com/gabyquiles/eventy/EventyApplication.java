package com.gabyquiles.eventy;

import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by gabrielquiles-perez on 5/9/16.
 */
public class EventyApplication extends android.app.Application {

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    public void onCreate() {
        super.onCreate();
//        TODO: When event list is empty, the no events is scrollable
//        TODO: Random crashes
//        TODO: Notifications for next day events - Is in stash, maybe we have to implement FCM (GCM)???
//        TODO: Handle errors (Net, FB permissions, invalid data in FB)
//        TODO: Tablet design
//        TODO: Tests
//        TODO: WebApp
//        TODO: Move Analytics creation the Application class
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
        // Set persistence of DB
//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);


    }

    public FirebaseAnalytics getAnalytics() {
        return mFirebaseAnalytics;
    }
}
