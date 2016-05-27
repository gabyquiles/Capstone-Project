package com.gabyquiles.eventy.ui;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gabyquiles.eventy.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description
 *
 * @author gabrielquiles-perez
 */
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventHolder>  {
    private final String LOG_TAG = EventAdapter.class.getSimpleName();

    @Override
    public EventHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(EventHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class EventHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.card_toolbar) Toolbar mToolbar;
        @BindView(R.id.event_title_textview) TextView mTitle;
        @BindView(R.id.event_datetime_textview) TextView mDateTime;
        @BindView(R.id.event_place_textview) TextView mPlace;
        @BindView(R.id.confirmed_guests_count_textview) TextView mConfirmedGuestsCount;
        @BindView(R.id.total_guests_count_textview) TextView mTotalGuestsCount;
        @BindView(R.id.guests_divider) TextView mGuestsDivider;
        private EventAdapter mHandler;

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
//                        TODO:
//                        mHandler.onDeleteMenu(getAdapterPosition());
                        return true;
                    }
                    return false;
                }
            });
//            TODO:
//            view.setOnClickListener(this);

        }
    }
}
