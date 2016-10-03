package com.gabyquiles.eventy;

import android.content.Context;

import com.google.firebase.analytics.FirebaseAnalytics;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Description
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
    @Singleton
    Context provideContext() {
        return mContext;
    }

    @Provides
    @Singleton
    public FirebaseAnalytics getAnalytics(Context context) {
        return FirebaseAnalytics.getInstance(context);
    }

}
