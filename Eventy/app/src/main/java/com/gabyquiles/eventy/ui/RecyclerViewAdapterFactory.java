package com.gabyquiles.eventy.ui;

import android.content.Context;
import android.view.View;

import com.gabyquiles.eventy.events.EventsAdapter;

/**
 * Description
 *
 * @author gabrielquiles-perez
 */
abstract public class RecyclerViewAdapterFactory {
    private final String LOG_TAG = RecyclerViewAdapterFactory.class.getSimpleName();

    abstract EventsAdapter getAdapter(Context context, View emptyView);

    public interface AdapterOnClickHandler {
        void onClick(String key);
        void delete(String key);
    }
}
