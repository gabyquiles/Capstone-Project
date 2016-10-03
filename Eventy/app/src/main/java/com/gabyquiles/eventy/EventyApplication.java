package com.gabyquiles.eventy;

import android.app.Application;
import android.content.Context;

/**
 * This is a subclass of {@link Application} used to provide shared objects for this app.
 */
public class EventyApplication extends Application {

    private ApplicationComponent mApplicationComponent;

    public static EventyApplication get(Context context) {
        return (EventyApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initAppComponent();
//        TODO: When event list is empty, the no events is scrollable
//        TODO: Random crashes
//        TODO: Notifications for next day events - Is in stash, maybe we have to implement FCM (GCM)???
//        TODO: Handle errors (Net, FB permissions, invalid data in FB)
//        TODO: Tablet design
//        TODO: Tests
//        TODO: WebApp
        // Set persistence of DB
//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
//
//        mRepositoryComponent = DaggerEventsRepositoryComponent.builder().build();
//                .applicationModule(new ApplicationModule(getApplicationContext()))
//                .eventsRepositoryModule(new EventsRepositoryModule()).build();

    }
    private void initAppComponent() {
        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public ApplicationComponent getAppComponent() {
        return mApplicationComponent;
    }

}
