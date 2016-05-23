package com.gabyquiles.eventy.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;
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
    private String mPlaceName;
    private Double mLat;
    private Double mLon;
    private List<Guest> mGuestList;
    private List<String> mThingList;

    @Exclude
    private String mKey;

    public Event() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        mDate = cal.getTimeInMillis();
        mGuestList = new ArrayList<>();
        mThingList = new ArrayList<>();
    }

    public Event(Parcel in) {
        mKey = in.readString();
        mTitle = in.readString();
        mPlaceName = in.readString();
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

    public void setPlaceName(String placeName) {
        this.mPlaceName = placeName;
    }

    public String getPlaceName() {
        return mPlaceName;
    }

    public void setPlace(Place place) {
        setCoords(place.getLatLng());
    }

    public void setCoords(LatLng coords) {
        if (coords != null) {
            this.mLat = coords.latitude;
            this.mLon = coords.longitude;
        } else {
            mLat = null;
            mLon = null;
        }
    }

    @Exclude
    public LatLng getCoordinates() {
        if (mLat != null && mLon != null) {
            return new LatLng(mLat, mLon);
        }
        return null;
    }

    public void setLatitude(double lat) {
        this.mLat = lat;
    }

    public Double getLatitude() {
        return mLat;
    }

    public void setLongitude(double lon) {
        this.mLon = lon;
    }

    public Double getLongitude() {
        return mLon;
    }

    @Exclude
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
        if(!mPlaceName.equals(e.mPlaceName)) {
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
        result = 31 * result + mPlaceName.hashCode();
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
        parcel.writeString(mPlaceName);
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
