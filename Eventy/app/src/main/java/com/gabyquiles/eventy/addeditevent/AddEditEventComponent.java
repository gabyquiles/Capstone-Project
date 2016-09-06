package com.gabyquiles.eventy.addeditevent;

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
                AddEditEventPresenterModule.class,
                EventsRepositoryModule.class
        }
)
public interface AddEditEventComponent {

    AddEditEventActivity inject(AddEditEventActivity activity);
}