package com.gabyquiles.eventy.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Calendar;
import java.util.Date;

/**
 * Represents an event
 *
 * @author gabrielquiles-perez
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Event implements Parcelable{
    private String mTitle;
    private Date mDate;
    private String mPlace;

    @JsonIgnore
    private String mKey;

    public Event() {
        Calendar cal = Calendar.getInstance();
        mDate = cal.getTime();
    }

    public Event(Parcel in) {
        mKey = in.readString();
        mTitle = in.readString();
        mPlace = in.readString();

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(in.readLong());
        mDate = cal.getTime();
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date mDate) {
        this.mDate = mDate;
    }

    public String getPlace() {
        return mPlace;
    }

    public void setPlace(String mPlace) {
        this.mPlace = mPlace;
    }

    public String getKey() {
        return mKey;
    }

    public void setKey(String mKey) {
        this.mKey = mKey;
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
        if(!mDate.equals(e.mDate)) {
            return false;
        }
        if(!mPlace.equals(e.mPlace)) {
            return false;
        }
        if(!mKey.equals(e.mKey)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = mTitle.hashCode();
        result = 31 * result + mDate.hashCode();
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
        parcel.writeLong(mDate.getTime());
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
}
