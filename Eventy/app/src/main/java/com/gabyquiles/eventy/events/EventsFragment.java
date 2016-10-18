package com.gabyquiles.eventy.events;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gabyquiles.eventy.R;
import com.gabyquiles.eventy.addeditevent.AddEditEventActivity;
import com.gabyquiles.eventy.addeditevent.AddEditEventFragment;
import com.gabyquiles.eventy.signin.SignInActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Fragment that shows the list of next events
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

        // Set up floating action button
        FloatingActionButton fab =
                (FloatingActionButton) getActivity().findViewById(R.id.add_event_fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.addNewEvent();
            }
        });

        return rootView;
    }

    @Override
    public void showEvents(Cursor events) {
        mRecyclerView.setVisibility(View.VISIBLE);
        mEmptyView.setVisibility(View.GONE);
    }

    public void showAddEvent() {

        showEventDetails(null);
    }
//
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
    public void setAdapter(EventsAdapter adapter) {
        mAdapter = adapter;
        mRecyclerView.setAdapter(mAdapter);
    }

    private void showNoEventsView(String message) {
        mRecyclerView.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.VISIBLE);
        mEmptyView.setText(message);
    }

    @Override
    public void showLoginActivity() {
        Intent signinIntent = new Intent(getActivity(), SignInActivity.class);
        startActivity(signinIntent);
        getActivity().finish();
    }
}
