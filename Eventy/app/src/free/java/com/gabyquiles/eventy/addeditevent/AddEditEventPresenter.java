package com.gabyquiles.eventy.addeditevent;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.gabyquiles.eventy.data.LoaderProvider;
import com.gabyquiles.eventy.model.FreeEvent;

import javax.inject.Inject;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Description
 *
 * @author gabrielquiles-perez
 */
public class AddEditEventPresenter implements AddEditEventContract.Presenter,
        LoaderManager.LoaderCallbacks<Cursor>{
    private final String LOG_TAG = AddEditEventPresenter.class.getSimpleName();

    @NonNull
    private final AddEditEventContract.View mEventsView;

    @NonNull
    private final LoaderProvider mLoaderProvider;

    @NonNull
    private LoaderManager mLoaderManager;

    @Nullable
    private String mEventId;

    @Inject
    public AddEditEventPresenter(@Nullable String eventId, @NonNull LoaderProvider provider,
                                 @NonNull LoaderManager manager,
                                 @NonNull AddEditEventContract.View eventsView) {
        mEventId = eventId;
        mLoaderProvider = checkNotNull(provider);
        mLoaderManager = checkNotNull(manager);
        mEventsView = checkNotNull(eventsView);
        mEventsView.setPresenter(this);
    }

    @Override
    public void start() {
        if (!isNewEvent()) {
            populateEvent();
        }
    }

    @Override
    public void saveEvent(){//String title, String description) {
//        TODO: Fix
        if (isNewEvent()) {
//            createEvent(title, description);
        } else {
//            updateEvent(title, description);
        }
    }

    @Override
    public void populateEvent() {
        if (isNewEvent()) {
            throw new RuntimeException("populateTask() was called but task is new.");
        }
//        TODO: Get Event
//        mTasksRepository.getTask(mTaskId, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return mLoaderProvider.createEventLoader(mEventId);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToLast()) {
            FreeEvent event = (FreeEvent) FreeEvent.from(data);
//            mEventsView.setDescription(task.getDescription());
            mEventsView.setTitle(event.getTitle());
        } else {
            // NO-OP, add mode.
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private boolean isNewEvent() {
        return mEventId == null;
    }

    private void createTask(String title, String description) {
//        TODO
//        Task newTask = new Task(title, description);
//        if (newTask.isEmpty()) {
//            mAddTaskView.showEmptyTaskError();
//        } else {
//            mTasksRepository.saveTask(newTask);
//            mAddTaskView.showTasksList();
//        }
    }

    private void updateTask(String title, String description) {
        if (isNewEvent()) {
            throw new RuntimeException("updateTask() was called but task is new.");
        }
//        TODO: Save event
//        mTasksRepository.saveTask(new Task(title, description, mTaskId));
        mEventsView.showEventsList(); // After an edit, go back to the list.
    }

//    @Override
//    public void onEventLoaded(Event event) {
//
//    }
//
//    @Override
//    public void onDataNotAvailable() {
//
//    }

}
