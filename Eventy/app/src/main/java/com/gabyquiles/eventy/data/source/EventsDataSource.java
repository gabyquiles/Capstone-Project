package com.gabyquiles.eventy.data.source;

import android.support.annotation.NonNull;

import com.gabyquiles.eventy.model.FreeEvent;

import java.util.List;

/**
 * Main entry point for accessing events data.
 */
public interface EventsDataSource {


    interface GetEventsCallback {

        void onEventsLoaded(List<FreeEvent> tasks);

        void onDataNotAvailable();
    }

    void getEvents(@NonNull GetEventsCallback callback);
}
