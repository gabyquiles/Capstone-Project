package com.gabyquiles.eventy.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.gabyquiles.eventy.R;
import com.gabyquiles.eventy.model.Event;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A placeholder fragment containing a simple view.
 */
public class EventDetailsActivityFragment extends Fragment {
//    Views
    @BindView(R.id.event_title) TextView mTitle;
    @BindView(R.id.event_place_textview) TextView mPlace;


    Firebase mDB;
    Event mEvent;

    public EventDetailsActivityFragment() {
        mEvent = new Event();
        //TODO: Should I abstract the DB interactions? Maybe yes
        mDB = new Firebase("https://eventy.firebaseio.com/events/data");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_event_details, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @OnClick(R.id.save_button)
    public void save() {
        mEvent.setmTitle((String) mTitle.getText());
    }

}
