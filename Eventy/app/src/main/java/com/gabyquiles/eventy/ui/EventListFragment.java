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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Fragment that shows the list of next events
 *
 * @author gabrielquiles-perez
 */
public class EventListFragment extends Fragment {

    static final String FIREBASE_URI = "URI";

    //Views
    @BindView(R.id.empty_textview) TextView mEmptyView;
    @BindView(R.id.event_list) RecyclerView mRecyclerView;

    private EventAdapter mAdapter;
    private DatabaseReference mFirebase;
    private FirebaseAuth mAuth;
    private Uri mFirebaseUrl;

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
//        Bundle arguments = getArguments();
//        if (arguments != null) {
//            mFirebaseUrl = arguments.getParcelable(FIREBASE_URI);
//            if(mFirebaseUrl != null) {
//                // Should receive the user base url. Have to add events in order to point to correct data.
//                mFirebaseUrl = mFirebaseUrl.buildUpon().appendPath("events").build();
//            }
//        }

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_event_list, container, false);
        ButterKnife.bind(this, rootView);

        mFirebase = FirebaseDatabase.getInstance().getReference().child(getString(R.string.firebase_users_path));
        mFirebase = mFirebase.child(getString(R.string.user_id));
        mFirebase = mFirebase.child("events");
//        AuthData  authData = mFirebase.getAuth();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        if(authData != null) {
            mAdapter = new EventAdapter(mFirebase, new EventAdapter.EventAdapterOnClickHandler() {
                @Override
                public void onClick(String key) {
//                    Uri eventUri = mFirebaseUrl.buildUpon().appendPath(key).build();
//                    ((Callback) getActivity()).showEventDetails(eventUri);
                }
            });

//        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
//            @Override
//            public void onItemRangeInserted(int positionStart, int itemCount) {
//                super.onItemRangeInserted(positionStart, itemCount);
//                int friendlyMessageCount = mAdapter.getItemCount();
////                int lastVisiblePosition = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
//                // If the recycler view is initially being loaded or the user is at the bottom of the list, scroll
//                // to the bottom of the list to show the newly added message.
//                if (lastVisiblePosition == -1 ||
//                        (positionStart >= (friendlyMessageCount - 1) && lastVisiblePosition == (positionStart - 1))) {
//                    mMessageRecyclerView.scrollToPosition(positionStart);
//                }
//            }
//        });
            mRecyclerView.setAdapter(mAdapter);
//        }

        return rootView;
    }

    @OnClick(R.id.add_event_fab)
    public void addEvent() {
        ((Callback) getActivity()).showEventDetails(mFirebaseUrl);
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
