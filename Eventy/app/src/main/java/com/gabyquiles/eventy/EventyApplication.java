package com.gabyquiles.eventy;

/**
 * Created by gabrielquiles-perez on 5/9/16.
 */
public class EventyApplication extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
//        TODO: When event list is empty, the no events is scrollable
//        TODO: Random crashes
//        TODO: Notifications for next day events - Is in stash, maybe we have to implement FCM (GCM)???
//        TODO: Provide widget for next week events with a send invite button
//        TODO: Handle errors (Net, FB permissions, invalid data in FB)
//        TODO: Add Analytics
//        TODO: Tablet design
//        TODO: Animated transitions
//        TODO: Tests
//        TODO: WebApp
        // Set persistence of DB
//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
