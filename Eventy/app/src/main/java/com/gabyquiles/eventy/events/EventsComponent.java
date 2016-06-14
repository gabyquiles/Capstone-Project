package com.gabyquiles.eventy.events;

import com.gabyquiles.eventy.data.DataModule;
import com.gabyquiles.eventy.data.source.EventsRepositoryComponent;
import com.gabyquiles.eventy.util.FragmentScoped;


import dagger.Component;

/**
 * Description
 *
 * @author gabrielquiles-perez
 */
@FragmentScoped
@Component(dependencies = EventsRepositoryComponent.class, modules = {DataModule.class, EventsPresenterModule.class})
public interface EventsComponent {

    void inject(EventsActivity activity);
}
