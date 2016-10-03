package com.gabyquiles.eventy;

import com.gabyquiles.eventy.addeditevent.AddEditEventComponent;
import com.gabyquiles.eventy.addeditevent.AddEditEventPresenterModule;
import com.gabyquiles.eventy.events.EventsComponent;
import com.gabyquiles.eventy.events.EventsPresenterModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Description
 *
 * @author gabrielquiles-perez
 */
@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {
    EventsComponent plus(EventsPresenterModule eventsPresenterModule);
    AddEditEventComponent plus(AddEditEventPresenterModule addEditEventComponent);
}
