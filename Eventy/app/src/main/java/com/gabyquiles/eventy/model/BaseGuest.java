package com.gabyquiles.eventy.model;

/**
 * Represents an invited guest
 *
 * @author gabrielquiles-perez
 */
public class BaseGuest {
    private final String LOG_TAG = BaseGuest.class.getSimpleName();

    public static int INVITED = 1;
    public static int GOING = 2;
    public static int MAYBE = 3;
    public static int NOT_GOING = 4;

    private String mName;
    private String mEmail;
    private int mStatus;
    private String mThing;

    public BaseGuest() {
        mStatus = INVITED;
        mThing = null;
    }

    public BaseGuest(String name, String email, String thing, int status) {
        mName = name;
        mEmail = email;
        mThing = thing;
        mStatus = status;
    }

    public BaseGuest(String name, String email) {
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


    @Override
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }

        if(!(o instanceof BaseGuest)) {
            return false;
        }

        BaseGuest guest = (BaseGuest) o;
        if(!mName.equals(guest.mName)) {
            return false;
        }
        if(!mEmail.equals(guest.mEmail)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = mName.hashCode();
        result = 31 * result + mEmail.hashCode();
        return result;
    }
}
