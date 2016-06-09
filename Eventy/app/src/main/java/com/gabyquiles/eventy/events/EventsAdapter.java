package com.gabyquiles.eventy.events;

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
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gabyquiles.eventy.R;
import com.gabyquiles.eventy.Utility;
import com.gabyquiles.eventy.data.EventContract;
import com.gabyquiles.eventy.model.Event;
import com.gabyquiles.eventy.ui.ItemChoiceManager;
import com.gabyquiles.eventy.ui.RecyclerViewAdapterFactory;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description
 *
 * @author gabrielquiles-perez
 */
public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.EventHolder>
        implements LoaderManager.LoaderCallbacks<Cursor>  {
    private final String LOG_TAG = EventsAdapter.class.getSimpleName();


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

    // These indices are tied to EVENT_COLUMNS.  If EVENT_COLUMNS changes, these
    // must change.
    static final int COL_EVENT_ID = 0;
    static final int COL_EVENT_TITLE = 1;
    static final int COL_EVENT_DATE = 2;
    static final int COL_EVENT_PLACE = 3;

    private Context mContext;
    private Cursor mCursor;
    private View mEmptyView;
    private  RecyclerViewAdapterFactory.AdapterOnClickHandler mClickHandler;
    final private ItemChoiceManager mICM;

    public EventsAdapter(Context context, View emptyView,
                        RecyclerViewAdapterFactory.AdapterOnClickHandler handler, int choiceMode) {
        mContext = context;
        mEmptyView = emptyView;
        mClickHandler = handler;
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
            eventHolder.mTitle.setText(mCursor.getString(COL_EVENT_TITLE));
            long dateInMillis = mCursor.getLong(COL_EVENT_DATE);
            String dateStr = Utility.formatFullDate(dateInMillis);
            eventHolder.mDateTime.setText(dateStr);
            eventHolder.mPlace.setText(mCursor.getString(COL_EVENT_PLACE));
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

    public Cursor getCursor() {
        return mCursor;
    }

    public class EventHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, Toolbar.OnMenuItemClickListener {
        @BindView(R.id.card_toolbar) Toolbar mToolbar;
        @BindView(R.id.event_title_textview) TextView mTitle;
        @BindView(R.id.event_datetime_textview) TextView mDateTime;
        @BindView(R.id.event_place_textview) TextView mPlace;


        public EventHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            mToolbar.inflateMenu(R.menu.card_menu);
            mToolbar.setOnMenuItemClickListener(this);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            mClickHandler.onClick(mCursor.getString(COL_EVENT_ID));
            mICM.onClick(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            int id = item.getItemId();

            //Logout from the app
            if (id == R.id.action_delete_event) {
                int adapterPosition = getAdapterPosition();
                mCursor.moveToPosition(adapterPosition);
                mClickHandler.delete(mCursor.getString(COL_EVENT_ID));
                return true;
            }
            return false;
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
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        swapCursor(null);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        mICM.onRestoreInstanceState(savedInstanceState);
    }

    public void onSaveInstanceState(Bundle outState) {
        mICM.onSaveInstanceState(outState);
    }

    public int getSelectedItemPosition() {
        return mICM.getSelectedItemPosition();
    }

}
