package com.gabyquiles.eventy.signin;

import dagger.Module;
import dagger.Provides;

/**
 * Description
 *
 * @author gabrielquiles-perez
 */
@Module
public class SignInPresenterModule {
    private final String LOG_TAG = SignInPresenterModule.class.getSimpleName();

    private final SignInContract.View mView;

    public SignInPresenterModule(SignInContract.View view) {
        mView = view;
    }

    @Provides
    SignInContract.View provideEventsContractView() {
        return mView;
    }
}
