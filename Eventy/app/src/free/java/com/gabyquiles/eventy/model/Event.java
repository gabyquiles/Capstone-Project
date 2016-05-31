package com.gabyquiles.eventy.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Description
 *
 * @author gabrielquiles-perez
 */
public class Event extends BaseEvent {
    private final String LOG_TAG = Event.class.getSimpleName();

    public Event() {
        super();
    }

    public Event(Parcel in) {
        super(in);
    }

    public String[] getGuestsEmail() {
        String[] emailArray = new String[mGuestList.size()];
        for(int i = 0; i < mGuestList.size(); i++) {
            emailArray[i] = mGuestList.get(i).getEmail();
        }
        return emailArray;
    }

    public String getThingsString() {
        StringBuilder strBuilder = new StringBuilder();
        int i = 0;
        for(String thing: mThingList) {
            strBuilder.append(thing);
            if(++i < mThingList.size()) {
                strBuilder.append(", ");
            }
        }
        return strBuilder.toString();
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
