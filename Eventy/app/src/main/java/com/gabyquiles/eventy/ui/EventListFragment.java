package com.gabyquiles.eventy.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gabyquiles.eventy.R;
import com.gabyquiles.eventy.firebase.FirebaseManager;
import com.gabyquiles.eventy.firebase.FirebaseReader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Fragment that shows the list of next events
 *
 * @author gabrielquiles-perez
 */
public class EventListFragment extends Fragment {
    FirebaseManager mDBManager;

    //Views
    @BindView(R.id.empty_textview) TextView mEmptyView;
    @BindView(R.id.event_list) RecyclerView mRecyclerView;

    private EventAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDBManager = new FirebaseReader(null);
    }

    @Override
    public void onStart() {
        super.onStart();
        if(mAdapter != null) {
            mAdapter.setup();
            mDBManager.addChildEventListener(mAdapter);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mAdapter != null) {
            mDBManager.removeEventListener(mAdapter);
            mAdapter.cleanUp();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_event_list, container, false);
        ButterKnife.bind(this, rootView);
        mAdapter = new EventAdapter(getActivity(), mEmptyView, new EventAdapter.EventAdapterOnClickHandler() {

            @Override
            public void onClick(String key, EventAdapter.VH holder) {
                Uri eventUri = FirebaseManager.EVENTS_URI.buildUpon().appendPath(key).build();
                ((Callback) getActivity()).showEventDetails(eventUri);
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }

    @OnClick(R.id.add_event_fab)
    public void addEvent() {
        ((Callback) getActivity()).showEventDetails(null);
    }

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    interface Callback {
        /**
         * Callback for when an event has been selected or when a new event will be added
         * @param uri
         */
        void showEventDetails(Uri uri);
    }
}
