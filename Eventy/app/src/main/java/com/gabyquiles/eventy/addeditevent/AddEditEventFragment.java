package com.gabyquiles.eventy.addeditevent;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.gabyquiles.eventy.R;
import com.gabyquiles.eventy.Utility;
import com.gabyquiles.eventy.model.Guest;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Description
 *
 * @author gabrielquiles-perez
 */
public class AddEditEventFragment extends Fragment implements AddEditEventContract.View{
    private final String LOG_TAG = AddEditEventFragment.class.getSimpleName();

    public static final String ARGUMENT_EDIT_EVENT_ID = "EDIT_EVENT_ID";
    public static final int PICK_CONTACT_REQUEST = 1;  // The request code

    private AddEditEventContract.Presenter mPresenter;
    private long mTimestamp;
    private EventDetailsLists mGuestAdapter;
    private EventDetailsLists mThingsAdapter;

    //    Views
    @BindView(R.id.event_title)
    EditText mTitle;

    @BindView(R.id.event_date)
    EditText mDate;

    @BindView(R.id.event_time)
    EditText mTime;

    @BindView(R.id.event_address)
    EditText mAddress;

    @BindView(R.id.new_thing)
    EditText mNewThing;

    @BindView(R.id.guest_list)
    RecyclerView mGuestList;

    @BindView(R.id.things_list)
    RecyclerView mThingsList;

    @BindView(R.id.send_invites_button)
    AppCompatImageButton mInvitesBtn;

    public static AddEditEventFragment newInstance() {
        return new AddEditEventFragment();
    }

    public AddEditEventFragment() {

    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(@NonNull AddEditEventContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_event_details, container, false);
        ButterKnife.bind(this, rootView);

        mGuestList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mGuestAdapter = new GuestsAdapter(getActivity());
        mGuestList.setAdapter((RecyclerView.Adapter) mGuestAdapter);

        mThingsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mThingsAdapter = new ThingsAdapter(getActivity());
        mThingsList.setAdapter((RecyclerView.Adapter) mThingsAdapter);


//        TODO: move to presenter??
//        mInterstitialAd = new InterstitialAd(getActivity());
//        mInterstitialAd.setAdUnitId(getString(R.string.admob_unit_id));
//
//        mInterstitialAd.setAdListener(new AdListener() {
//            @Override
//            public void onAdClosed() {
//                requestNewInterstitial();
//            }
//        });

//        requestNewInterstitial();

//        // Obtain the FirebaseAnalytics instance.
//        mFirebaseAnalytics = ((EventyApplication) getActivity().getApplication()).getAnalytics();
//        logEvent("Event Details");

        return rootView;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mPresenter.result(requestCode, resultCode, data);
    }

    @Override
    public void showEventsList() {
        getActivity().finish();
    }

    @Override
    public void setTitle(String title) {
        mTitle.setText(title);
    }

    @Override
    public void setDate(long date) {
        mTimestamp = date;
        mDate.setText(Utility.formatShortDate(mTimestamp));
    }

    @Override
    public void setTime(long date) {
        mTimestamp = date;
        mTime.setText(Utility.formatTime(mTimestamp));
    }

    @Override
    public void setPlaceName(String placeName) {
        mAddress.setText(placeName);
    }

    @Override
    public void refreshGuests(List<Guest> guests) {
        mGuestAdapter.updateList(guests);

    }

    @Override
    public void refreshThings(List<String> things) {
        mThingsAdapter.updateList(things);
    }

    @Override
    public void addGuest(Guest guest) {
        mGuestAdapter.addToList(guest);
    }


    @OnFocusChange(R.id.event_date)
    public void showDatePicker(boolean focused) {
        if(focused) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(mTimestamp);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DATE);

            DatePickerDialog datePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int new_year, int new_month, int new_day) {
                    mTimestamp = Utility.createDate(mTimestamp, new_year, new_month, new_day);
                    mDate.setText(Utility.formatShortDate(mTimestamp));
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
                    mTimestamp = Utility.createTime(mTimestamp, new_hour, new_minutes);
                    mTime.setText(Utility.formatTime(mTimestamp));
                }
            }, hour, minutes, false);
            timePicker.show();
        }
    }

    @OnClick(R.id.add_guests_button)
    public void addGuests() {
        Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
        pickContactIntent.setType(ContactsContract.CommonDataKinds.Email.CONTENT_TYPE); // Show user only contacts w/ phone numbers
        startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);
    }

    @OnClick(R.id.add_things_button)
    public void addNewThing() {
        String newThing = mNewThing.getText().toString();
        addThing(newThing);

    }

    @OnClick(R.id.send_invites_button)
    public void sendInvites() {
        String title = mTitle.getText().toString();
        String place = mAddress.getText().toString();
        List things = mThingsAdapter.getList();
        List guests = mGuestAdapter.getList();
        mPresenter.sendInvites(title, mTimestamp, place, guests, things );
    }

    public void addThing(String thing) {
        mThingsAdapter.addToList(thing);
    }

    public void saveEvent() {
        String title = mTitle.getText().toString();
        String place = mAddress.getText().toString();
        List things = mThingsAdapter.getList();
        List guests = mGuestAdapter.getList();
        mPresenter.saveEvent(title, mTimestamp, place, guests, things );
    }
}
