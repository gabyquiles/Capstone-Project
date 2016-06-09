package com.gabyquiles.eventy.events;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gabyquiles.eventy.R;
import com.gabyquiles.eventy.model.Event;
import com.gabyquiles.eventy.ui.EventDetailsActivity;
import com.gabyquiles.eventy.ui.EventListAdapterFactory;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Description
 *
 * @author gabrielquiles-perez
 */
public class EventsFragment extends Fragment implements EventsContract.View{
    private final String LOG_TAG = EventsFragment.class.getSimpleName();

    private EventsContract.Presenter mPresenter;

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
        mAdapter = (new EventListAdapterFactory()).getAdapter(getActivity(), mEmptyView);
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
//        mAdapter = (new EventListAdapterFactory()).getAdapter(getActivity(), mEmptyView);
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
//        TODO: Hide no events message
//        TODO: Show recycler view
    }

    @Override
    public void showAddEvent() {

        Intent eventIntent = new Intent(getContext(), EventDetailsActivity.class);

        eventIntent.setData(null);
        ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity());
        ActivityCompat.startActivity(getActivity(), eventIntent, activityOptions.toBundle());
    }

    @Override
    public void showEventDetails(String eventId) {

    }

    @Override
    public void showNoEvents() {

    }

    @Override
    public void showSuccessfullySavedMessage() {

    }
}
