package com.gabyquiles.eventy.events;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;

import com.gabyquiles.eventy.R;
import com.gabyquiles.eventy.Utility;
import com.gabyquiles.eventy.model.BaseEvent;
import com.gabyquiles.eventy.model.Event;
import com.gabyquiles.eventy.ui.ItemChoiceManager;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description
 * @author gabrielquiles-perez
 */
public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.EventHolder>  {
    private final String LOG_TAG = EventsAdapter.class.getSimpleName();

    private Cursor mCursor;
    private EventsContract.EventItemListener mListener;
    final private ItemChoiceManager mICM;

    Context mContext;

    public EventsAdapter(Context context,
                         EventsContract.EventItemListener handler, int choiceMode) {
        mContext = context;
        mListener = handler;
        mICM = new ItemChoiceManager(this);
        mICM.setChoiceMode(choiceMode);
    }

    @Override
    public EventHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if(viewGroup instanceof RecyclerView) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.event_list_item, viewGroup, false);
            view.setFocusable(true);
            return new EventHolder(view);
        } else {
            throw new RuntimeException("Not bound to RecyclerViewSelection");
        }
    }

    @Override
    public void onBindViewHolder(EventHolder eventHolder, int position) {
        if(mCursor.moveToPosition(position)) {
            BaseEvent event = Event.from(mCursor);
            eventHolder.mTitle.setText(event.getTitle());
            String dateStr = Utility.formatFullDate(event.getDate());
            eventHolder.mDateTime.setText(dateStr);
            eventHolder.mPlace.setText(event.getPlaceName());
            mICM.onBindViewHolder(eventHolder, position);
        }
    }

    @Override
    public int getItemCount() {
        if ( null == mCursor ) return 0;
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    public void deleteEvent(int position) {
        mCursor.moveToPosition(position);
        BaseEvent event = Event.from(mCursor);
        mListener.onDeleteEvent(event);
    }

    public void clickEvent(int position) {
        mCursor.moveToPosition(position);
        BaseEvent event = Event.from(mCursor);
        mListener.onEventClick(event.getKey());
    }

    public class EventHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.card_toolbar) Toolbar mToolbar;
        @BindView(R.id.event_title_textview) TextView mTitle;
        @BindView(R.id.event_datetime_textview) TextView mDateTime;
        @BindView(R.id.event_place_textview) TextView mPlace;


        public EventHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            mToolbar.inflateMenu(R.menu.card_menu);
            if (mToolbar != null) {
                mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        //Delete event
                        if (id == R.id.action_delete_event) {
                            deleteEvent(getAdapterPosition());
                            return true;
                        }
                        return false;
                    }
                });
            }

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickEvent(getAdapterPosition());
                }
            });
            mToolbar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickEvent(getAdapterPosition());
                }
            });
        }
    }
}
