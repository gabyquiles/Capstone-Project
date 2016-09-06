package com.gabyquiles.eventy.data.source;

import android.content.Context;

import com.gabyquiles.eventy.data.source.local.EventsLocalDataSource;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Description
 *
 * @author gabrielquiles-perez
 */
@Module
public class EventsRepositoryModule {
    private final String LOG_TAG = EventsRepositoryModule.class.getSimpleName();

//    @Singleton
    @Provides
    EventsDataSource provideLocalDataSource(Context context) {
        return new EventsLocalDataSource(context.getContentResolver());
    }
}
