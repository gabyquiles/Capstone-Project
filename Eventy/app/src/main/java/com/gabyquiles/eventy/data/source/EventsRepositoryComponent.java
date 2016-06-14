package com.gabyquiles.eventy.data.source;

import com.gabyquiles.eventy.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Description
 *
 * @author gabrielquiles-perez
 */
@Singleton
@Component(modules = {EventsRepositoryModule.class, ApplicationModule.class})
public interface EventsRepositoryComponent {

    EventsRepository getEventsRepository();
}
