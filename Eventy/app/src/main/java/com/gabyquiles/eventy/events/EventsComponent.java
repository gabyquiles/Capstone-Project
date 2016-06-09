package com.gabyquiles.eventy.events;

import com.gabyquiles.eventy.data.DataModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Description
 *
 * @author gabrielquiles-perez
 */
@Singleton
@Component(modules = {DataModule.class, EventsPresenterModule.class})
public interface EventsComponent {

    void inject(EventsActivity activity);
}
