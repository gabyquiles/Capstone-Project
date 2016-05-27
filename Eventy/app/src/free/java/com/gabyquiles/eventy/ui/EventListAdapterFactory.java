package com.gabyquiles.eventy.ui;

import android.content.Context;
import android.view.View;

/**
 * Description
 *
 * @author gabrielquiles-perez
 */
public class EventListAdapterFactory extends RecyclerViewAdapterFactory implements RecyclerViewAdapterFactory.AdapterOnClickHandler{
    private final String LOG_TAG = EventListAdapterFactory.class.getSimpleName();

    @Override
    EventAdapter getAdapter(Context context, View emptyView) {
        return  new EventAdapter();
    }

    @Override
    public void onClick(String key) {

    }

    @Override
    public void delete(String key) {

    }
}
