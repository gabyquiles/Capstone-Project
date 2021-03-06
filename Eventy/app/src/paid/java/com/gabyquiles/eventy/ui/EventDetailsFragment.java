package com.gabyquiles.eventy.ui;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.gabyquiles.eventy.BuildConfig;
import com.gabyquiles.eventy.R;
import com.gabyquiles.eventy.Utility;
import com.gabyquiles.eventy.model.Event;
import com.gabyquiles.eventy.model.Guest;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import butterknife.OnTextChanged;

/**
 * Fragment that shows the details of an event
 *
 * @author gabrielquiles-perez
 */
public class EventDetailsFragment extends Fragment implements ValueEventListener,
        OnMapReadyCallback {
    private final String LOG_TAG = EventDetailsFragment.class.getSimpleName();

    static final String EVENT_URI = "event_uri";
    static final int PICK_CONTACT_REQUEST = 1;  // The request code
    static final int PICK_ADDRESS_REQUEST = 2;

//    Views
    @BindView(R.id.event_title) EditText mTitle;
    @BindView(R.id.event_date) EditText mDate;
    @BindView(R.id.event_time) EditText mTime;
    @BindView(R.id.event_address) EditText mAddress;
    @BindView(R.id.new_thing) EditText mNewThing;
    @BindView(R.id.guest_list) RecyclerView mGuestList;
    @BindView(R.id.things_list) RecyclerView mThingsList;

    SupportMapFragment mMapFragment;

    private DatabaseReference mFirebase;
    private Event mEvent;
    private GoogleMap mMap;
    private GuestsAdapter mGuestAdapter;
    private ThingsAdapter mThingsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mEvent = new Event();
        Bundle arguments = getArguments();
        if(arguments != null && arguments.getParcelable(EVENT_URI) != null) {
            Uri firebaseUri = arguments.getParcelable(EVENT_URI);
            if(firebaseUri != null) {
                mFirebase = FirebaseDatabase.getInstance().getReferenceFromUrl(firebaseUri.toString());

            }
        } else {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            mFirebase = FirebaseDatabase.getInstance().getReference().child(getString(R.string.firebase_users_path));
            mFirebase = mFirebase.child(user.getUid());
            mFirebase = mFirebase.child("events");
        }
        mFirebase.addValueEventListener(this);

        View rootView = inflater.inflate(R.layout.fragment_event_details, container, false);
        ButterKnife.bind(this, rootView);

        mDate.setText(Utility.formatShortDate(mEvent.getDate()));
        mTime.setText(Utility.formatTime(mEvent.getDate()));

        mMapFragment = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map));
        if (mMapFragment != null) {
            View view = mMapFragment.getView();
            if (view != null) {
                view.setVisibility(View.GONE);
            }
            mMapFragment.getMapAsync(this);
        }

        mGuestList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mGuestAdapter = new GuestsAdapter(getActivity(), mEvent.getGuestList(), null);
        mGuestList.setAdapter(mGuestAdapter);

        mThingsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mThingsAdapter = new ThingsAdapter(getActivity(), mEvent.getThingList(), null);
        mThingsList.setAdapter(mThingsAdapter);

        return rootView;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        mEvent = dataSnapshot.getValue(Event.class);
        if(!dataSnapshot.getKey().equals("events") && mEvent != null) {
            mEvent.setKey(dataSnapshot.getKey());

            mTitle.setText(mEvent.getTitle());
            mDate.setText(Utility.formatShortDate(mEvent.getDate()));
            mTime.setText(Utility.formatTime(mEvent.getDate()));
            mAddress.setText(mEvent.getPlaceName());
            updateMap();
            updateThingsList(null);
            updateGuestList(null);
        } else {
            mEvent = new Event();
        }
    }

    @Override
    public void onCancelled(DatabaseError firebaseError) {
        if(BuildConfig.DEBUG) {
            Log.v(LOG_TAG, "Update canceled: " + firebaseError.getMessage());
        }
    }

    public void save() {
        String eventTitle = mTitle.getText().toString().isEmpty()?getString(R.string.no_title):mTitle.getText().toString();
        mEvent.setTitle(eventTitle);
        mEvent.setPlaceName(mAddress.getText().toString());

        DatabaseReference.CompletionListener savingListener = new DatabaseReference.CompletionListener(){

            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                String message;
                if (databaseError != null) {
                    message = getString(R.string.event_saved_error);
//                    TODO: Record with analytics
                } else {
                    message = getString(R.string.event_saved);
                }
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }
        };


        String key = mEvent.getKey();
        if(key == null) {
            mFirebase.removeEventListener(this);
            mFirebase = mFirebase.push();
            mFirebase.setValue(mEvent, savingListener);
            mEvent.setKey(mFirebase.getKey());
            mFirebase.addValueEventListener(this);
        } else {
            mFirebase.setValue(mEvent, savingListener);
        }
    }

    @OnClick(R.id.search_address_btn)
    public void pickAddress() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            startActivityForResult(builder.build(getActivity()), PICK_ADDRESS_REQUEST);
        } catch (Exception e) {
//            TODO: Catch correct exceptions
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (mMap != null) {
            updateMap();
        }
    }

    @OnClick(R.id.add_guests_button)
    public void addGuests() {
        Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
        pickContactIntent.setType(ContactsContract.CommonDataKinds.Email.CONTENT_TYPE); // Show user only contacts w/ phone numbers
        startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);
    }

    @OnClick(R.id.add_things_button)
    public void addThings() {
        String thing = mNewThing.getText().toString();
        updateThingsList(thing);
    }

    @OnFocusChange(R.id.event_date)
    public void showDatePicker(boolean focused) {
        if(focused) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(mEvent.getDate());
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DATE);

            DatePickerDialog datePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int new_year, int new_month, int new_day) {
                    long timestamp = Utility.createDate(mEvent.getDate(), new_year, new_month, new_day);
                    mDate.setText(Utility.formatShortDate(timestamp));
                    mEvent.setDate(timestamp);
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
                    long timestamp = Utility.createTime(mEvent.getDate(), new_hour, new_minutes);
                    mTime.setText(Utility.formatTime(timestamp));
                    mEvent.setDate(timestamp);
                }
            }, hour, minutes, false);
            timePicker.show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == PICK_CONTACT_REQUEST) {
            // Make sure the request was successful
            if (resultCode == Activity.RESULT_OK) {
                String fullNameIdx = ContactsContract.Contacts.DISPLAY_NAME;
                String emailIdx = ContactsContract.CommonDataKinds.Email.ADDRESS;

                // The user picked a contact.
                Uri contactUri = data.getData();
                String[] fields = { emailIdx, fullNameIdx};

                Cursor cursor = getContext().getContentResolver().query(contactUri, fields, null, null, null);
                if(cursor != null) {
                    if (cursor.moveToFirst()) {
                        int fullnameColumn = cursor.getColumnIndex(fullNameIdx);
                        String fullName = cursor.getString(fullnameColumn);
                        int emailColumn = cursor.getColumnIndex(emailIdx);
                        String email = cursor.getString(emailColumn);

                        Guest guest = new Guest(fullName, email);
                        updateGuestList(guest);
                    }
                    cursor.close();
                }
            }
        } else if (requestCode == PICK_ADDRESS_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                Place place = PlacePicker.getPlace(getActivity(), data);
                mEvent.setPlace(place);
                updateMap();
            }
        }
    }

    private void updateGuestList(Guest guest) {
        if (guest != null) {
            mEvent.addGuest(guest);
        }
        mGuestAdapter.updateList(mEvent.getGuestList());
    }

    private void updateThingsList(String thing) {
        if (thing != null) {
            mEvent.addThing(thing);
        }
        mThingsAdapter.updateList(mEvent.getThingList());
    }

    private void updateMap() {
        LatLng location = mEvent.getCoordinates();
        if (location != null) {
            if (mMap != null) {
                mMap.addMarker(new MarkerOptions().position(location));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
                if (mMapFragment != null) {
                    View view = mMapFragment.getView();
                    if (view != null) {
                        view.setVisibility(View.VISIBLE);
                    }
                }
            }
        } else {
            if (mMapFragment != null) {
                View view = mMapFragment.getView();
                if (view != null) {
                    view.setVisibility(View.GONE);
                }
            }
        }
    }
}
