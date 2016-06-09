package com.gabyquiles.eventy.data.source;

import android.support.annotation.NonNull;

import com.gabyquiles.eventy.model.Event;

import java.util.List;

/**
 * Main entry point for accessing events data.
 */
public interface EventsDataSource {


    interface GetEventsCallback {

        void onEventsLoaded(List<Event> tasks);

        void onDataNotAvailable();
    }

    void getEvents(@NonNull GetEventsCallback callback);
}
