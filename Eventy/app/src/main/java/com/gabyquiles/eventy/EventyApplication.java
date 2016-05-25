package com.gabyquiles.eventy;

import com.google.firebase.database.FirebaseDatabase;

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
//        TODO: use a shareaction provider to invited for event
//        TODO: use a Content Provider to store templates of events
//        TODO: Provide widget for next week events with a send invite button
//        TODO: RTL support
//        TODO: Handle errors (Net, FB permissions, invalid data in FB)
//        TODO: Extract all strings
//        TODO: Add Analytics
//        TODO: Create free and paid flavors
//        TODO: Tablet design
//        TODO: Animated transitions
//        TODO: Tests
//        TODO: WebApp
        // Set persistence of DB
//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
