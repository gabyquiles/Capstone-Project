package com.gabyquiles.eventy.events;

import com.gabyquiles.eventy.data.source.EventsRepositoryModule;
import com.gabyquiles.eventy.util.ActivityScope;

import dagger.Subcomponent;

/**
 * Description
 *
 * @author gabrielquiles-perez
 */
@ActivityScope
@Subcomponent(
        modules = {
                EventsPresenterModule.class,
                EventsRepositoryModule.class
        }
)
public interface EventsComponent {

    EventsActivity inject(EventsActivity activity);
}
