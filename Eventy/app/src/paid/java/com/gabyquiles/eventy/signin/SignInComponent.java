package com.gabyquiles.eventy.signin;

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
                SignInPresenterModule.class
        }
)
public interface SignInComponent {
        SignInActivity inject(SignInActivity activity);
}
