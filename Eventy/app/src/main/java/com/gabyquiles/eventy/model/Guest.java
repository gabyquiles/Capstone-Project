package com.gabyquiles.eventy.model;

/**
 * Represents an invited guest
 *
 * @author gabrielquiles-perez
 */
public class Guest {
    private final String LOG_TAG = Guest.class.getSimpleName();

    public static int INVITED = 1;
    public static int GOING = 2;
    public static int MAYBE = 3;
    public static int NOT_GOING = 4;

    private String mName;
    private String mEmail;
    private int mStatus;
    private String mThing;

    public Guest(String name, String email) {
        mName = name;
        mEmail = email;
        mStatus = INVITED;
        mThing = null;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        this.mEmail = email;
    }

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int status) {
        this.mStatus = status;
    }

    public String getThing() {
        return mThing;
    }

    public void setThing(String thing) {
        this.mThing = thing;
    }
}
