package com.gabyquiles.eventy.events;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.gabyquiles.eventy.model.BaseEvent;
import com.gabyquiles.eventy.model.BaseGuest;
import com.google.firebase.database.Query;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.gabyquiles.eventy.R;
import com.gabyquiles.eventy.Utility;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * {@link EventsAdapter} exposes a list of upcoming events
 * from a {@link com.google.firebase.database.FirebaseDatabase} to a {@link RecyclerView}
 *
 * @author gabrielquiles-perez
 */
public class EventsAdapter extends FirebaseRecyclerAdapter<BaseEvent, EventsAdapter.EventHolder> {
    private final String LOG_TAG = EventsAdapter.class.getSimpleName();
    private EventsContract.EventItemListener mListener;

    @Inject Context mContext;

    public EventsAdapter(Query ref, EventsContract.EventItemListener clickHandler) {
        super(BaseEvent.class, R.layout.event_list_item, EventHolder.class, ref);
        mListener = clickHandler;
    }

    @Override
    protected void populateViewHolder(EventHolder eventHolder, BaseEvent event, int i) {
        eventHolder.mTitle.setText(event.getTitle());
        eventHolder.mPlace.setText(event.getPlaceName());
        eventHolder.mDateTime.setText(Utility.formatFullDate(event.getDate()));
        int confirmed_guests = event.getGuestsCountByStatus(BaseGuest.GOING);
        int total_guests = event.getGuestsCount();
        eventHolder.mConfirmedGuestsCount.setText(Integer.valueOf(confirmed_guests).toString());
//        eventHolder.mConfirmedGuestsCount.setContentDescription(mContext.getString(R.string.confirmed_guests_count_description, confirmed_guests));
        eventHolder.mTotalGuestsCount.setText(Integer.valueOf(total_guests).toString());
//        eventHolder.mTotalGuestsCount.setContentDescription(mContext.getString(R.string.total_guests_count_description, total_guests));
        eventHolder.setClickHandler(this);
    }


    @Override
    public int getItemCount() {
        int count = super.getItemCount();
        return count;
    }

    public void onClick(int position) {
        String key = getRef(position).getKey();
        mListener.onEventClick(null);
    }

    public void onDeleteMenu(int position) {
        String key = getRef(position).getKey();
        mListener.onDeleteEvent(null);
    }

    public static class EventHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.card_toolbar) Toolbar mToolbar;
        @BindView(R.id.event_title_textview) TextView mTitle;
        @BindView(R.id.event_datetime_textview) TextView mDateTime;
        @BindView(R.id.event_place_textview) TextView mPlace;
        @BindView(R.id.confirmed_guests_count_textview) TextView mConfirmedGuestsCount;
        @BindView(R.id.total_guests_count_textview) TextView mTotalGuestsCount;
        @BindView(R.id.guests_divider) TextView mGuestsDivider;
        private EventsAdapter mHandler;

        public EventHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            mToolbar.inflateMenu(R.menu.card_menu);
            mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener(){
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    int id = item.getItemId();

                    //Logout from the app
                    if (id == R.id.action_delete_event) {
                        mHandler.onDeleteMenu(getAdapterPosition());
                        return true;
                    }
                    return false;
                }
            });
            view.setOnClickListener(this);

        }

        public void setClickHandler(EventsAdapter handler) {
            mHandler = handler;
        }

        @Override
        public void onClick(View view) {
            mHandler.onClick(getAdapterPosition());

        }
    }
}
