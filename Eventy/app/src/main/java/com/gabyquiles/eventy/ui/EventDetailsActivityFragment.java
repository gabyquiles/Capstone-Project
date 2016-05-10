package com.gabyquiles.eventy.ui;

import android.app.DatePickerDialog;
import android.support.v4.app.Fragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.gabyquiles.eventy.R;
import com.gabyquiles.eventy.firebase.FirebaseManager;
import com.gabyquiles.eventy.model.Event;

import java.text.DateFormatSymbols;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;

/**
 * Fragment that shows the details of an event
 *
 * @author gabrielquiles-perez
 */
public class EventDetailsActivityFragment extends Fragment implements ValueEventListener{
    private final String LOG_TAG = EventDetailsActivityFragment.class.getSimpleName();

    static final String EVENT_URI = "event_uri";

//    Views
    @BindView(R.id.event_title) TextView mTitle;
    @BindView(R.id.event_date) TextView mDate;
    @BindView(R.id.event_time) TextView mTime;
    @BindView(R.id.event_address) TextView mPlace;

    FirebaseManager mDBManager;
    Event mEvent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mEvent = new Event();
        //TODO: Should I abstract the Firebase interactions? yes
        Bundle arguments = getArguments();
        if(arguments != null && arguments.getParcelable(EVENT_URI) != null) {
            String firebase_url = arguments.getParcelable(EVENT_URI).toString();
            mDBManager = new FirebaseManager(firebase_url);
            mDBManager.addValueEventListener(this);
        } else {
            mDBManager = new FirebaseManager(null);
        }


        View rootView = inflater.inflate(R.layout.fragment_event_details, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        mEvent = dataSnapshot.getValue(Event.class);
        mTitle.setText(mEvent.getTitle());
        mPlace.setText(mEvent.getPlace());
    }

    @Override
    public void onCancelled(FirebaseError firebaseError) {
        Log.v(LOG_TAG, "Floor update canceled: " + firebaseError.getMessage());

    }

    @OnClick(R.id.save_button)
    public void save() {
        mEvent.setTitle(mTitle.getText().toString());
        mEvent.setPlace(mPlace.getText().toString());
        mEvent = mDBManager.save(mEvent);
        mDBManager.addValueEventListener(this);
    }

    @OnFocusChange(R.id.event_date)
    public void showDatePicker(boolean focused) {
        if(focused) {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DATE);

            DatePickerDialog datePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int new_year, int new_month, int new_day) {
                    String monthString = new DateFormatSymbols().getMonths()[new_month];
                    mDate.setText(monthString + " " + new_day + ", " + new_year);
                }
            }, year, month, day);

            datePicker.show();
        }
    }

    @OnFocusChange(R.id.event_time)
    public void showTimePicker(boolean focused) {
        if(focused) {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minutes = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int new_hour, int new_minutes) {
                    String noon_indicator = "am";
                    if(new_hour == 0) {
                        new_hour = 12;
                    } else if(new_hour == 12) {
                        noon_indicator = "pm";
                    } else if (new_hour > 12) {

                        new_hour -= 12;
                        noon_indicator = "pm";
                    }
                    mTime.setText(new_hour + ":" + new_minutes + " " + noon_indicator);
                }
            }, hour, minutes, false);
            timePicker.show();
        }
    }
}
