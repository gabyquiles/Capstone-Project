package com.gabyquiles.eventy.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;

import com.gabyquiles.eventy.R;
import com.gabyquiles.eventy.events.EventsAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Fragment that shows the list of next events
 *
 * @author gabrielquiles-perez
 */
public class EventListFragment extends Fragment {

    private boolean mAutoSelectView;
    private int mChoiceMode;
    private boolean mHoldForTransition;

    //Views
    @BindView(R.id.empty_textview) TextView mEmptyView;
    @BindView(R.id.event_list) RecyclerView mRecyclerView;

    private EventsAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // We hold for transition here just in-case the activity
        // needs to be re-created. In a standard return transition,
        // this doesn't actually make a difference.
        if (mHoldForTransition) {
            getActivity().supportPostponeEnterTransition();
        }
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        TODO:
//        mAdapter.cleanup();
    }


    @Override
    public void onInflate(Context context, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(context, attrs, savedInstanceState);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.EventListFragment, 0, 0);
        mChoiceMode = a.getInt(R.styleable.EventListFragment_android_choiceMode, AbsListView.CHOICE_MODE_NONE);
        mAutoSelectView = a.getBoolean(R.styleable.EventListFragment_autoSelectView, false);
        mHoldForTransition = a.getBoolean(R.styleable.EventListFragment_sharedElementTransition, false);
        a.recycle();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_event_list, container, false);
        ButterKnife.bind(this, rootView);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = (new EventListAdapterFactory()).getAdapter(getActivity(), mEmptyView);
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }

//    @OnClick(R.id.add_event_fab)
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
         * @param uri Base uri that contains event details
         */
        void showEventDetails(Uri uri);
    }
}
