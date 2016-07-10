package com.gabyquiles.eventy.events;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;

import com.gabyquiles.eventy.R;
import com.gabyquiles.eventy.addeditevent.AddEditEventActivity;
import com.gabyquiles.eventy.addeditevent.AddEditEventFragment;
import com.gabyquiles.eventy.model.Event;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Fragment that shows the list of next events
 *
 * @author gabrielquiles-perez
 */
public class EventsFragment extends Fragment implements EventsContract.View{
    private final String LOG_TAG = EventsFragment.class.getSimpleName();

    private EventsContract.Presenter mPresenter;

    /**
     * Listener for clicks on tasks in the ListView.
     */
    EventsContract.EventItemListener mItemListener = new EventsContract.EventItemListener() {
        @Override
        public void onEventClick(Event clickedEvent) {
            mPresenter.openEventDetails(clickedEvent);
        }

        @Override
        public void onDeleteEvent(Event deleteEvent) {
            mPresenter.deleteEvent(deleteEvent);
        }
    };

    //Views
    @BindView(R.id.empty_textview)
    TextView mEmptyView;
    @BindView(R.id.event_list)
    RecyclerView mRecyclerView;

    private EventsAdapter mAdapter;

    public static EventsFragment newInstance() {
        return new EventsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mAdapter = new EventsAdapter(getActivity(), mItemListener, AbsListView.CHOICE_MODE_NONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(@NonNull EventsContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_event_list, container, false);
        ButterKnife.bind(this, rootView);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }

    @OnClick(R.id.add_event_fab)
    public void addEvent() {
        mPresenter.addNewEvent();
    }

    @Override
    public void showEvents(Cursor events) {
        mAdapter.swapCursor(events);
        mRecyclerView.setVisibility(View.VISIBLE);
        mEmptyView.setVisibility(View.GONE);
    }

    @OnClick(R.id.add_event_fab)
    @Override
    public void showAddEvent() {

        showEventDetails(null);
    }

    @Override
    public void showEventDetails(String eventId) {
        Intent intent = new Intent(getContext(), AddEditEventActivity.class);
        if (eventId != null) {
            intent.putExtra(AddEditEventFragment.ARGUMENT_EDIT_EVENT_ID, eventId);
        }
        startActivity(intent);
    }

    @Override
    public void showNoEvents() {
        showNoEventsView(getString(R.string.no_events));
    }

    @Override
    public void showSuccessfullySavedMessage() {

    }

    @Override
    public EventsContract.EventItemListener getItemListener() {
        return mItemListener;
    }

    private void showNoEventsView(String message) {
        mRecyclerView.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.VISIBLE);
        mEmptyView.setText(message);
    }
}
