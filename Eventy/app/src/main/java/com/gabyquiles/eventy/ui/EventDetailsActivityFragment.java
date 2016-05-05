package com.gabyquiles.eventy.ui;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.firebase.client.Firebase;
import com.gabyquiles.eventy.R;
import com.gabyquiles.eventy.model.Event;

import java.text.DateFormatSymbols;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnFocusChange;
import butterknife.OnItemClick;
import butterknife.OnTouch;

/**
 * A placeholder fragment containing a simple view.
 */
public class EventDetailsActivityFragment extends Fragment {
//    Views
    @BindView(R.id.event_title) TextView mTitle;
    @BindView(R.id.event_date) TextView mDate;
    @BindView(R.id.event_time) TextView mTime;
    @BindView(R.id.event_address) TextView mPlace;


    Firebase mDB;
    Event mEvent;
    String mId;

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
        mEvent.setTitle(mTitle.getText().toString());
        mEvent.setPlace(mPlace.getText().toString());
        Firebase eventRef = mDB.child("events");
        if(mId == null) {
            eventRef = eventRef.push();
        } else {
            eventRef = eventRef.child(mId);
        }
        eventRef.setValue(mEvent);
        mId = eventRef.getKey();
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
