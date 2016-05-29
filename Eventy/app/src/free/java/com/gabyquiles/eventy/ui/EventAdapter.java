package com.gabyquiles.eventy.ui;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.gabyquiles.eventy.R;
import com.gabyquiles.eventy.Utility;
import com.gabyquiles.eventy.data.EventContract;
import com.gabyquiles.eventy.model.Event;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * {@link EventAdapter} exposes a list of upcoming events
 * from a {@link android.database.Cursor} to a {@link RecyclerView}
 *
 * @author gabrielquiles-perez
 */
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventHolder>
        implements LoaderManager.LoaderCallbacks<Cursor>  {
    private final String LOG_TAG = EventAdapter.class.getSimpleName();


    private static final String[] EVENT_COLUMNS = {
            // In this case the id needs to be fully qualified with a table name, since
            // the content provider joins the location & weather tables in the background
            // (both have an _id column)
            // On the one hand, that's annoying.  On the other, you can search the weather table
            // using the location set by the user, which is only in the Location table.
            // So the convenience is worth it.
            EventContract.EventEntry.TABLE_NAME + "." + EventContract.EventEntry._ID,
            EventContract.EventEntry.COLUMN_TITLE,
            EventContract.EventEntry.COLUMN_DATE,
            EventContract.EventEntry.COLUMN_PLACE_NAME
    };

    // These indices are tied to FORECAST_COLUMNS.  If FORECAST_COLUMNS changes, these
    // must change.
    static final int COL_EVENT_ID = 0;
    static final int COL_EVENT_TITLE = 1;
    static final int COL_EVENT_DATE = 2;
    static final int COL_EVENT_PLACE = 3;

    private Context mContext;
    private Cursor mCursor;
    private View mEmptyView;
//    final private ItemChoiceManager mICM;

    public EventAdapter(Context context, View emptyView) {
        mContext = context;
        mEmptyView = emptyView;
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
            eventHolder.mTitle.setText(mCursor.getString(COL_EVENT_TITLE));
            long dateInMillis = mCursor.getLong(COL_EVENT_DATE);
            String dateStr = Utility.formatFullDate(dateInMillis);
            eventHolder.mDateTime.setText(dateStr);
            eventHolder.mPlace.setText(mCursor.getString(COL_EVENT_PLACE));
        }


//        eventHolder.mTitle.setText(event.getTitle());
//        eventHolder.mPlace.setText(event.getPlaceName());
//        eventHolder.mDateTime.setText(Utility.formatFullDate(event.getDate()));
//        int confirmed_guests = event.getGuestsCountByStatus(Guest.GOING);
//        int total_guests = event.getGuestsCount();
//        eventHolder.mConfirmedGuestsCount.setText(Integer.valueOf(confirmed_guests).toString());
//        eventHolder.mConfirmedGuestsCount.setContentDescription(mContext.getString(R.string.confirmed_guests_count_description, confirmed_guests));
//        eventHolder.mTotalGuestsCount.setText(Integer.valueOf(total_guests).toString());
//        eventHolder.mTotalGuestsCount.setContentDescription(mContext.getString(R.string.total_guests_count_description, total_guests));
//        eventHolder.setClickHandler(this);
    }

    @Override
    public int getItemCount() {
        if ( null == mCursor ) return 0;
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        mEmptyView.setVisibility(getItemCount() == 0 ? View.VISIBLE : View.GONE);
        notifyDataSetChanged();
    }

    public Cursor getCursor() {
        return mCursor;
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


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String sortOrder = EventContract.EventEntry.COLUMN_DATE + " ASC";

        Uri eventUri = EventContract.EventEntry.buildEventWithDateUri(System.currentTimeMillis());

        return new CursorLoader(mContext,
                eventUri,
                EVENT_COLUMNS,
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
// Swap the new cursor in.  (The framework will take care of closing the
        // old cursor once we return.)
        swapCursor(data);

//        updateEmptyView();
        if ( data.getCount() == 0 ) {
//            mContext.supportStartPostponedEnterTransition();
        } else {
//            mRecyclerView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//                @Override
//                public boolean onPreDraw() {
//                    // Since we know we're going to get items, we keep the listener around until
//                    // we see Children.
//                    if (mRecyclerView.getChildCount() > 0) {
//                        mRecyclerView.getViewTreeObserver().removeOnPreDrawListener(this);
//                        int position = mAdapter.getSelectedItemPosition();
//                        if (position == RecyclerView.NO_POSITION &&
//                                -1 != mInitialSelectedDate) {
//                            Cursor data = mAdapter.getCursor();
//                            int count = data.getCount();
//                            int dateColumn = data.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_DATE);
//                            for ( int i = 0; i < count; i++ ) {
//                                data.moveToPosition(i);
//                                if ( data.getLong(dateColumn) == mInitialSelectedDate ) {
//                                    position = i;
//                                    break;
//                                }
//                            }
//                        }
//                        if (position == RecyclerView.NO_POSITION) position = 0;
//                        // If we don't need to restart the loader, and there's a desired position to restore
//                        // to, do so now.
////                        mRecyclerView.smoothScrollToPosition(position);
////                        RecyclerView.ViewHolder vh = mRecyclerView.findViewHolderForAdapterPosition(position);
////                        if (null != vh && mAutoSelectView) {
////                            selectView(vh);
////                        }
////                        if(mHoldForTransition) {
////                            getActivity().supportStartPostponedEnterTransition();
////                        }
//                        return true;
//                    }
//                    return false;
//                }
//            });
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        swapCursor(null);
    }

}
