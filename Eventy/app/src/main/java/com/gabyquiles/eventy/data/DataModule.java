package com.gabyquiles.eventy.data;

import android.content.Context;
import android.support.v4.app.LoaderManager;

import dagger.Module;
import dagger.Provides;

/**
 * This is a Dagger module. We use this to pass in the multiple dependencies to the
 * {@link com.gabyquiles.eventy.events.EventsPresenter}
 */
@Module
public class DataModule {

    private final LoaderManager mLoader;
    private final Context mContext;

    public DataModule(Context context, LoaderManager loader) {
        mContext = context;
        mLoader = loader;
    }

    @Provides
    LoaderProvider getLoaderProvider() {
        return new LoaderProvider(mContext);
    }

    @Provides
    LoaderManager getLoaderManager() {
        return mLoader;
    }
}
