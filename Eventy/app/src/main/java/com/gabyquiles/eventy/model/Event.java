package com.gabyquiles.eventy.model;

import java.util.Date;

/**
 * Represents an event
 */
public class Event {
    private String mTitle;
    private Date mDate;
    private String mPlace;

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public Date getmDate() {
        return mDate;
    }

    public void setmDate(Date mDate) {
        this.mDate = mDate;
    }

    public String getmPlace() {
        return mPlace;
    }

    public void setmPlace(String mPlace) {
        this.mPlace = mPlace;
    }
}
