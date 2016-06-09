package com.gabyquiles.eventy.events;

import dagger.Module;
import dagger.Provides;

/**
 * Description
 *
 * @author gabrielquiles-perez
 */
@Module
public class EventsPresenterModule {
    private final String LOG_TAG = EventsPresenterModule.class.getSimpleName();

    private final EventsContract.View mView;

    public EventsPresenterModule(EventsContract.View view) {
        mView = view;
    }

    @Provides
    EventsContract.View provideEventsContractView() {
        return mView;
    }
}
