package com.gabyquiles.eventy.addeditevent;

import com.gabyquiles.eventy.ApplicationModule;
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
@Component(dependencies = EventsRepositoryComponent.class,
        modules = {AddEditEventPresenterModule.class, ApplicationModule.class, DataModule.class})
public interface AddEditEventComponent {

    void inject(AddEditEventActivity activity);
}