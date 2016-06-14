package com.gabyquiles.eventy;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * Description
 *
 * @author gabrielquiles-perez
 */
@Module
public class ApplicationModule {
    private final String LOG_TAG = ApplicationModule.class.getSimpleName();


    private final Context mContext;

    public ApplicationModule(Context context) {
        mContext = context;
    }

    @Provides
    Context provideContext() {
        return mContext;
    }
}
