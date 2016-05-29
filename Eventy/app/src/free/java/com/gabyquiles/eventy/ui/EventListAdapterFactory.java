package com.gabyquiles.eventy.ui;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

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
        EventAdapter adapter = new EventAdapter(context, emptyView);
        ((AppCompatActivity) context).getLoaderManager().initLoader(EVENT_LOADER, null, adapter);
        return  adapter;
    }

    @Override
    public void onClick(String key) {

    }

    @Override
    public void delete(String key) {

    }
//
//    private void updateEmptyView() {
//        if(mAdapter.getItemCount() == 0) {
//            TextView emptyView = (TextView) getView().findViewById(R.id.empty);
//            if(emptyView != null) {
//                int message = R.string.error_empty_forecaste_list;
//
//                @SunshineSyncAdapter.LocationStatus int status = Utility.getLocationStatus(getActivity());
//                switch (status) {
//                    case SunshineSyncAdapter.LOCATION_STATUS_SERVER_DOWN:
//                        message = R.string.error_server_down;
//                        break;
//                    case SunshineSyncAdapter.LOCATION_STATUS_SERVER_INVALID:
//                        message = R. string.error_server_error;
//                        break;
//                    case SunshineSyncAdapter.LOCATION_STATUS_INVALID_LOCATION:
//                        message = R.string.error_location_invalid;
//                    default:
//                        if (!Utility.isNetworkAvailable(getActivity())) {
//                            message = R.string.error_no_network_connection;
//                        }
//                }
//                emptyView.setText(message);
//            }
//        }
//    }
}
