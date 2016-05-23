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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Fragment that shows the list of next events
 *
 * @author gabrielquiles-perez
 */
public class EventListFragment extends Fragment {

    //Views
    @BindView(R.id.empty_textview) TextView mEmptyView;
    @BindView(R.id.event_list) RecyclerView mRecyclerView;

    private EventAdapter mAdapter;
    private DatabaseReference mFirebase;
    private FirebaseUser mUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAdapter.cleanup();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_event_list, container, false);
        ButterKnife.bind(this, rootView);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        mUser = auth.getCurrentUser();

        mFirebase = FirebaseDatabase.getInstance().getReference().child(getString(R.string.firebase_users_path));
        mFirebase = mFirebase.child(mUser.getUid());
        mFirebase = mFirebase.child("events");

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if(mUser != null) {
            // Filter out event and sort by date
            long today = Calendar.getInstance().getTimeInMillis();
            Query filteredEvents = mFirebase.orderByChild("date").startAt(today);
            mAdapter = new EventAdapter(filteredEvents, mEmptyView, new EventAdapter.EventAdapterOnClickHandler() {
                @Override
                public void onClick(String key) {
                    Uri eventUri = Uri.parse(mFirebase.toString()).buildUpon().appendPath(key).build();
                    ((Callback) getActivity()).showEventDetails(eventUri);
                }

                @Override
                public void deleteEvent(String key) {
                    Uri eventUri = Uri.parse(mFirebase.toString()).buildUpon().appendPath(key).build();
                    FirebaseDatabase.getInstance().getReferenceFromUrl(eventUri.toString()).removeValue();
                }
            });
            mRecyclerView.setAdapter(mAdapter);
        }

        return rootView;
    }

    @OnClick(R.id.add_event_fab)
    public void addEvent() {
        Uri newEventUri = Uri.parse(mFirebase.toString());
        ((Callback) getActivity()).showEventDetails(newEventUri);
    }

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    interface Callback {
        /**
         * Callback for when an event has been selected or when a new event will be added
         * @param uri Base uri that contains event details
         */
        void showEventDetails(Uri uri);
    }
}
