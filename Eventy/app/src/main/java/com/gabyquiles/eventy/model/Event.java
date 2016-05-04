package com.gabyquiles.eventy.model;

import java.util.Date;

/**
 * Represents an event
 */
public class Event {
    private String mTitle;
    private Date mDate;
    private String mPlace;

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
}
