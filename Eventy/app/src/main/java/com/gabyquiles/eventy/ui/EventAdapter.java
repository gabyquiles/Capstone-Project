package com.gabyquiles.eventy.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gabyquiles.eventy.R;
import com.gabyquiles.eventy.Utility;
import com.gabyquiles.eventy.model.Event;

/**
 * {@link EventAdapter} exposes a list of upcoming events
 * from a {@link android.database.Cursor} to a {@link RecyclerView}
 *
 * @author gabrielquiles-perez
 */
public class EventAdapter extends EventsFirebaseAdapter<Event> {
    private final String LOG_TAG = EventAdapter.class.getSimpleName();
    private Context mContext;
    private EventAdapterOnClickHandler mClickHandler;

    public EventAdapter(Context context, View emptyView, EventAdapterOnClickHandler clickHandler) {
        super(Event.class);
        mContext = context;
        mEmptyView = emptyView;
        mClickHandler = clickHandler;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        if(parent instanceof RecyclerView) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.event_list_item, parent, false);
            view.setFocusable(true);
            return new VH(view);
        } else {
            throw new RuntimeException("Not bound to RecyclerView");
        }
    }

    @Override
    public void populateHolder(RecyclerView.ViewHolder viewHolder, Event event) {
        ((VH) viewHolder).mTitle.setText(event.getTitle());
        ((VH) viewHolder).mPlace.setText(event.getPlace());
        ((VH) viewHolder).mDateTime.setText(Utility.formatFullDate(event.getDate()));
    }

    @Override
    public void setModelKey(String key, Event event) {
        event.setKey(key);
    }

    class VH extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView mTitle;
        public final TextView mDateTime;
        public final TextView mPlace;

        public VH(View view) {
            super(view);
            mTitle = (TextView) view.findViewById(R.id.event_title_textview);
            mDateTime = (TextView) view.findViewById(R.id.event_datetime_textview);
            mPlace = (TextView) view.findViewById(R.id.event_place_textview);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Log.v(LOG_TAG, "OnClick ViewHolder");
            int adapterPosition = getAdapterPosition();
            Event event = mModels.get(adapterPosition);
            mClickHandler.onClick(event.getKey(), this);
        }
    }

    public interface EventAdapterOnClickHandler {
        void onClick(String key, VH holder);
    }
}
