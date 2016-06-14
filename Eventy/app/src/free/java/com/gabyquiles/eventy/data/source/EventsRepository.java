package com.gabyquiles.eventy.data.source;

import android.support.annotation.NonNull;

import com.gabyquiles.eventy.model.Event;

import javax.inject.Inject;

/**
 * Description
 *
 * @author gabrielquiles-perez
 */
public class EventsRepository implements EventsDataSource {
    private final String LOG_TAG = EventsRepository.class.getSimpleName();
    EventsDataSource mDataSource;

    @Inject
    EventsRepository(@NonNull EventsDataSource dataSource) {
        mDataSource = dataSource;
    }

    @Override
    public void getEvents(@NonNull GetEventsCallback callback) {
        mDataSource.getEvents(callback);
    }

    @Override
    public void getEvent(@NonNull String eventId, @NonNull GetEventCallback callback) {
        mDataSource.getEvent(eventId, callback);
    }

    @Override
    public void saveEvent(@NonNull Event event){
        mDataSource.saveEvent(event);
    }
}
