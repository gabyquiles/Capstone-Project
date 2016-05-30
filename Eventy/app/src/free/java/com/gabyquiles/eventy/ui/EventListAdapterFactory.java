package com.gabyquiles.eventy.ui;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AbsListView;

import com.gabyquiles.eventy.data.EventContract;

/**
 * Description
 *
 * @author gabrielquiles-perez
 */
public class EventListAdapterFactory extends RecyclerViewAdapterFactory
        implements RecyclerViewAdapterFactory.AdapterOnClickHandler{
    private final String LOG_TAG = EventListAdapterFactory.class.getSimpleName();

    private static final int EVENT_LOADER = 0;


    private Context mContext;

    @Override
    EventAdapter getAdapter(Context context, View emptyView) {
        mContext = context;
        int choiceMode = AbsListView.CHOICE_MODE_NONE;
        EventAdapter adapter = new EventAdapter(context, emptyView, this, choiceMode);
        ((AppCompatActivity) context).getLoaderManager().initLoader(EVENT_LOADER, null, adapter);
        return  adapter;
    }

    @Override
    public void onClick(String key) {
        ((EventListFragment.Callback) mContext)
                .showEventDetails(EventContract.EventEntry.buildEventUri(Long.valueOf(key)));
    }

    @Override
    public void delete(String key) {
        mContext.getContentResolver().delete(EventContract.EventEntry.CONTENT_URI,
                EventContract.EventEntry._ID + " = ?",
                new String[] {key});

    }
}
