package com.gabyquiles.eventy.ui;

import android.content.Context;
import android.view.View;
import android.widget.AbsListView;

import com.gabyquiles.eventy.data.source.local.EventContract;
import com.gabyquiles.eventy.events.EventsAdapter;

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
    public EventsAdapter getAdapter(Context context, View emptyView) {
        mContext = context;
        int choiceMode = AbsListView.CHOICE_MODE_NONE;
//        EventsAdapter adapter = new EventsAdapter(context, this, choiceMode);
//        ((AppCompatActivity) context).getLoaderManager().initLoader(EVENT_LOADER, null, adapter);
        return  null;
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
