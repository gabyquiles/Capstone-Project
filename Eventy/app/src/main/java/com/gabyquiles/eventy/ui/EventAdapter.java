package com.gabyquiles.eventy.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.Query;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.gabyquiles.eventy.R;
import com.gabyquiles.eventy.Utility;
import com.gabyquiles.eventy.model.Event;
import com.gabyquiles.eventy.model.Guest;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * {@link EventAdapter} exposes a list of upcoming events
 * from a {@link com.google.firebase.database.FirebaseDatabase} to a {@link RecyclerView}
 *
 * @author gabrielquiles-perez
 */
public class EventAdapter extends FirebaseRecyclerAdapter<Event, EventAdapter.EventHolder> {
    private final String LOG_TAG = EventAdapter.class.getSimpleName();
    private EventAdapterOnClickHandler mClickHandler;
    private View mEmptyView;

    public EventAdapter(Query ref, View emptyView, EventAdapterOnClickHandler clickHandler) {
        super(Event.class, R.layout.event_list_item, EventHolder.class, ref);
        mClickHandler = clickHandler;
        mEmptyView = emptyView;
    }

    @Override
    protected void populateViewHolder(EventHolder eventHolder, Event event, int i) {
        eventHolder.mTitle.setText(event.getTitle());
        eventHolder.mPlace.setText(event.getPlace());
        eventHolder.mDateTime.setText(Utility.formatFullDate(event.getDate()));
        String guestsCount = event.getGuestsCountByStatus(Guest.GOING) + " / " + event.getGuestsCount();
        eventHolder.mGuestsCount.setText(guestsCount);
        eventHolder.setClickHandler(this);
    }


    @Override
    public int getItemCount() {
        int count = super.getItemCount();
        mEmptyView.setVisibility(count == 0 ? View.VISIBLE : View.GONE);
        return count;
    }

//    @Override
//    public void onBindViewHolder(EventAdapter.EventHolder viewHolder, int position) {
//        mEmptyView.setVisibility(getItemCount() == 0 ? View.VISIBLE : View.GONE);
//        onBindViewHolder(viewHolder, position);
//    }

    public void onClick(int position) {
        String key = getRef(position).getKey();
        mClickHandler.onClick(key);
    }

    public static class EventHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.event_title_textview) TextView mTitle;
        @BindView(R.id.event_datetime_textview) TextView mDateTime;
        @BindView(R.id.event_place_textview) TextView mPlace;
        @BindView(R.id.guests_count_textview) TextView mGuestsCount;
        private EventAdapter mHandler;

        public EventHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        public void setClickHandler(EventAdapter handler) {
            mHandler = handler;
        }

        @Override
        public void onClick(View view) {
            mHandler.onClick(getAdapterPosition());

        }
    }

    public interface EventAdapterOnClickHandler {
        void onClick(String key);
    }
}
