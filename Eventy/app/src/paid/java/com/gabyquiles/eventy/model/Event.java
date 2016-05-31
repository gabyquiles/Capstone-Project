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
    private final String LOG_TAG = Event.class.getSimpleName();

    private Double mLat;
    private Double mLon;

    @Exclude
    protected String mKey;

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

    @Exclude
    public int getGuestsCount() {
        return mGuestList.size();
    }
}
