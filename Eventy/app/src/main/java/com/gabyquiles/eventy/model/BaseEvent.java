package com.gabyquiles.eventy.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Represents an event
 *
 * @author gabrielquiles-perez
 */

public class BaseEvent implements Parcelable{
    protected String mTitle;
    protected long mDate;
    protected String mPlaceName;
    protected List<Guest> mGuestList;
    protected List<String> mThingList;

    protected String mKey;

    public BaseEvent() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        mDate = cal.getTimeInMillis();
        mGuestList = new ArrayList<>();
        mThingList = new ArrayList<>();
        mTitle = "";
        mPlaceName = "";
    }

    public BaseEvent(Parcel in) {
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

        if(!(o instanceof BaseEvent)) {
            return false;
        }

        BaseEvent e = (BaseEvent) o;
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
        result = 31 * result + Long.valueOf(mDate).hashCode();
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

    public final static Parcelable.Creator<BaseEvent> CREATOR = new Parcelable.Creator<BaseEvent>() {
        @Override
        public BaseEvent createFromParcel(Parcel parcel) {
            return new BaseEvent(parcel);
        }

        @Override
        public BaseEvent[] newArray(int i) {
            return new BaseEvent[0];
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
