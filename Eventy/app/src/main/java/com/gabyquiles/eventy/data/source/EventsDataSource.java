package com.gabyquiles.eventy.data.source;

import android.support.annotation.NonNull;

import com.gabyquiles.eventy.model.Event;

import java.util.List;

/**
 * Main entry point for accessing events data.
 */
public interface EventsDataSource {

    interface GetEventsCallback {

        void onEventsLoaded(List<Event> events);

        void onDataNotAvailable();
    }

    interface GetEventCallback {
        void onEventLoaded(Event event);

        void onDataNotAvailable();
    }

    void getEvents(@NonNull GetEventsCallback callback);

    void getEvent(@NonNull String taskId, @NonNull GetEventCallback callback);

    void saveEvent(@NonNull Event event);

    void updateEvent(@NonNull Event event);

    void deleteEvent(@NonNull Event event);

//    TODO: Delete old events
}
