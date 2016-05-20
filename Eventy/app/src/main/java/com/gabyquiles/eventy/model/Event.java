package com.gabyquiles.eventy.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Represents an event
 *
 * @author gabrielquiles-perez
 */

public class Event implements Parcelable{
    private String mTitle;
    private long mDate;
    private String mPlace;
    private List<Guest> mGuestList;
    private List<String> mThingList;

    @Exclude
    private String mKey;

    public Event() {
//        TODO: Validate before saving
        Calendar cal = Calendar.getInstance();
        mDate = cal.getTimeInMillis();
        mGuestList = new ArrayList<>();
        mThingList = new ArrayList<>();
    }

    public Event(Parcel in) {
        mKey = in.readString();
        mTitle = in.readString();
        mPlace = in.readString();
        mDate = in.readLong();
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public long getDate() {
        return mDate;
    }

    public void setDate(long date) {
        this.mDate = date;
    }

    public String getPlace() {
        return mPlace;
    }

    public void setPlace(String place) {
        this.mPlace = place;
    }

    public String getKey() {
        return mKey;
    }

    public void setKey(String key) {
        this.mKey = key;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }

        if(!(o instanceof Event)) {
            return false;
        }

        Event e = (Event) o;
//        TODO: Check that all properties are being checked
        if(!mTitle.equals(e.mTitle)) {
            return false;
        }
        if(mDate != e.mDate) {
            return false;
        }
        if(!mPlace.equals(e.mPlace)) {
            return false;
        }
        if((mKey != null && e.mKey != null) && !mKey.equals(e.mKey)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = mTitle.hashCode();
        result = 31 * result + (new Long(mDate)).hashCode();
        result = 31 * result + mPlace.hashCode();
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(mKey);
        parcel.writeString(mTitle);
        parcel.writeString(mPlace);
        parcel.writeLong(mDate);
    }

    public final static Parcelable.Creator<Event> CREATOR = new Parcelable.Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel parcel) {
            return new Event(parcel);
        }

        @Override
        public Event[] newArray(int i) {
            return new Event[0];
        }
    };

    public List<Guest> getGuestList() {
        return mGuestList;
    }

    public void setGuestList(List<Guest> guestList) {
        this.mGuestList = guestList;
    }

    public void addGuest(Guest guest) {
        // Add a guest if it is not already on the list
        if(mGuestList.indexOf(guest) == -1) {
            mGuestList.add(guest);
        }
    }

    public List<String> getThingList() {
        return mThingList;
    }

    public void setThingList(List<String> thingList) {
        this.mThingList = thingList;
    }

    public void addThing(String thing) {
        mThingList.add(thing);
    }

    public int getGuestsCount() {
        return mGuestList.size();
    }

    public int getGuestsCountByStatus(int status) {
        int counter = 0;
        for (Guest guest : mGuestList) {
            if(guest.getStatus() == status) {
                counter++;
            }
        }
        return counter;
    }
}
