package com.gabyquiles.eventy.addeditevent;

import android.support.annotation.Nullable;

import dagger.Module;
import dagger.Provides;

/**
 * This is a Dagger module. We use this to pass in the View dependency to the
 * {@link AddEditEventPresenter}.
 */
@Module
public class AddEditEventPresenterModule {

    private final AddEditEventContract.View mView;

    private String mEventId;

    public AddEditEventPresenterModule(AddEditEventContract.View view, @Nullable String eventId) {
        mView = view;
        mEventId = eventId;
    }

    @Provides
    AddEditEventContract.View provideAddEditEventContractView() {
        return mView;
    }

    @Provides
    @Nullable
    String provideEventId() {
        return mEventId;
    }
}
