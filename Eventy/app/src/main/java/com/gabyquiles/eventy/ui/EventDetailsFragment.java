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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.gabyquiles.eventy.BuildConfig;
import com.gabyquiles.eventy.R;
import com.gabyquiles.eventy.Utility;
import com.gabyquiles.eventy.model.Event;
import com.gabyquiles.eventy.model.Guest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
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

//    Views
    @BindView(R.id.event_title) TextView mTitle;
    @BindView(R.id.event_date) TextView mDate;
    @BindView(R.id.event_time) TextView mTime;
    @BindView(R.id.event_address) TextView mPlace;

    private DatabaseReference mFirebase;
    private Uri mFirebaseUri;
    private Event mEvent;
    private GoogleMap mMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mEvent = new Event();
        Bundle arguments = getArguments();
        if(arguments != null && arguments.getParcelable(EVENT_URI) != null) {
            mFirebaseUri= arguments.getParcelable(EVENT_URI);
            if(mFirebaseUri != null) {
//                mFirebase = new FirebaseDatabase(mFirebaseUri.toString());
                mFirebase = FirebaseDatabase.getInstance().getReferenceFromUrl(mFirebaseUri.toString());
                mFirebase.addValueEventListener(this);
            }
        }

        View rootView = inflater.inflate(R.layout.fragment_event_details, container, false);
        ButterKnife.bind(this, rootView);

        mDate.setText(Utility.formatShortDate(mEvent.getDate()));
        mTime.setText(Utility.formatTime(mEvent.getDate()));

        SupportMapFragment frag = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map));//.getMapAsync(this);
        frag.getMapAsync(this);
//        frag.getView().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent mapIntent = new Intent(getActivity(), MapsActivity.class);
//                ActivityCompat.startActivity(getActivity(), mapIntent, null);
//            }
//        });
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
            mPlace.setText(mEvent.getPlace());
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

    @OnClick(R.id.save_button)
    public void save() {
        mEvent.setTitle(mTitle.getText().toString());
        mEvent.setPlace(mPlace.getText().toString());

        String key = mEvent.getKey();
        if(key == null) {
            mFirebase.removeEventListener(this);
            mFirebase = mFirebase.push();
            mFirebase.setValue(mEvent);
            mEvent.setKey(mFirebase.getKey());
            mFirebase.addValueEventListener(this);
        } else {
            mFirebase.setValue(mEvent);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney, Australia, and move the camera.
        LatLng sydney = new LatLng(18, -66);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15));
    }

    @OnClick(R.id.add_guests_button)
    public void addGuests() {
        Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
        pickContactIntent.setType(ContactsContract.CommonDataKinds.Email.CONTENT_TYPE); // Show user only contacts w/ phone numbers
        startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);
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
            int i =1;
            // Make sure the request was successful
            if (resultCode == Activity.RESULT_OK) {
                String fullNameIdx = ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME;
                String emailIdx = ContactsContract.CommonDataKinds.Email.ADDRESS;

                // The user picked a contact.
                Uri contactUri = data.getData();
                String[] fields = { emailIdx, fullNameIdx };

                Cursor cursor = getContext().getContentResolver().query(contactUri, fields, null, null, null);
                if(cursor != null) {
                    if (cursor.moveToFirst()) {
                        int fullnameColumn = cursor.getColumnIndex(fullNameIdx);
                        String fullName = cursor.getString(fullnameColumn);
                        int emailColumn = cursor.getColumnIndex(emailIdx);
                        String email = cursor.getString(emailColumn);

                        Guest guest = new Guest(fullName, email);
                        mEvent.addGuest(guest);
                    }
                    cursor.close();
                }
            }
        }
    }
}
