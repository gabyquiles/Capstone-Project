package com.gabyquiles.eventy.events;

import android.database.Cursor;
import android.support.annotation.NonNull;

import com.gabyquiles.eventy.BasePresenter;
import com.gabyquiles.eventy.BaseView;
import com.gabyquiles.eventy.model.Event;

/**
 * Description
 *
 * @author gabrielquiles-perez
 */
public interface EventsContract {
    interface View extends BaseView<Presenter> {
        void showEvents(Cursor events);

        void showAddEvent();

        void showEventDetails(String eventId);

        void showNoEvents();

        void showSuccessfullySavedMessage();

        EventItemListener getItemListener();
    }

    interface Presenter extends BasePresenter{
        void addNewEvent();

        void openEventDetails(@NonNull Event event);

    }

    public interface EventItemListener {
        void onEventClick(Event clickedEvent);
        void onDeleteEvent(Event deleteEvent);
    }
}
