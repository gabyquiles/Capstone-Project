package com.gabyquiles.eventy.model;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.Exclude;

/**
 * Description
 *
 * @author gabrielquiles-perez
 */
public class Event extends BaseEvent {
    private final String LOG_TAG = BaseEvent.class.getSimpleName();

    private Double mLatitude;
    private Double mLongitude;

    @Exclude
    protected String mKey;

    public void setPlace(Place place) {
        setCoords(place.getLatLng());
    }

    public void setCoords(LatLng coords) {
        if (coords != null) {
            this.mLatitude = coords.latitude;
            this.mLongitude = coords.longitude;
        } else {
            mLatitude = null;
            mLongitude = null;
        }
    }

    @Exclude
    public LatLng getCoordinates() {
        if (mLatitude != null && mLongitude != null) {
            return new LatLng(mLatitude, mLongitude);
        }
        return null;
    }

    public void setLatitude(double lat) {
        this.mLatitude = lat;
    }

    public Double getLatitude() {
        return mLatitude;
    }

    public void setLongitude(double lon) {
        this.mLongitude = lon;
    }

    public Double getLongitude() {
        return mLongitude;
    }


    @Exclude
    public String getKey() {
        return mKey;
    }

    @Exclude
    public int getGuestsCount() {
        return mGuestList.size();
    }
}
