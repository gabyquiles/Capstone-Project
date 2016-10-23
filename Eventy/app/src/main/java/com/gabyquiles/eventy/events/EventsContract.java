package com.gabyquiles.eventy.events;

import android.database.Cursor;
import android.support.annotation.NonNull;

import com.gabyquiles.eventy.BasePresenter;
import com.gabyquiles.eventy.BaseView;
import com.gabyquiles.eventy.model.BaseEvent;

/**
 * Description
 *
 * @author gabrielquiles-perez
 */
public interface EventsContract {
    interface View extends BaseView<Presenter> {
        void showEvents();

        void showAddEvent();

        void showEventDetails(String eventId);

        void showNoEvents();

        void showSuccessfullySavedMessage();

//        EventItemListener getItemListener();

        void setAdapter(EventsAdapter adapter);

        void showLoginActivity();
    }

    interface Presenter extends BasePresenter{
        void addNewEvent();

        void openEventDetails(@NonNull BaseEvent event);

        void deleteEvent(@NonNull BaseEvent event);

        void menuSelected(int itemId);
    }

    interface EventItemListener {
        void onEventClick(String key);
        void onDeleteEvent(BaseEvent deleteEvent);
    }
}
