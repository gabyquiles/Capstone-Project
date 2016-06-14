package com.gabyquiles.eventy.addeditevent;

import android.content.Intent;

import com.gabyquiles.eventy.BasePresenter;
import com.gabyquiles.eventy.BaseView;
import com.gabyquiles.eventy.model.Event;
import com.gabyquiles.eventy.model.Guest;

import java.util.List;

/**
 * Description
 *
 * @author gabrielquiles-perez
 */
public interface AddEditEventContract {
    interface View extends BaseView<Presenter> {
        void showEventsList();

        void setTitle(String title);
        void setDate(long date);
        void setTime(long date);
        void setPlaceName(String placeName);
        void refreshGuests(List<Guest> guestsList);
        void refreshThings(List<String> thingsList);
        void addGuest(Guest guest);
    }

    interface Presenter extends BasePresenter {
        void saveEvent(String title, long date, String place, List<Guest> guests, List<String> things);
        void sendInvites();
        void addGuest(Guest guest);

        void result(int requestCode, int resultCode, Intent data);
        void populateEvent();
        void errorSelectingGuest();
    }
}
