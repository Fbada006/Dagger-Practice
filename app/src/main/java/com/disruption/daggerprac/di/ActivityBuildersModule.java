package com.disruption.daggerprac.di;

import com.disruption.daggerprac.di.auth.AuthModule;
import com.disruption.daggerprac.di.auth.AuthScope;
import com.disruption.daggerprac.di.auth.AuthViewModelsModule;
import com.disruption.daggerprac.di.main.MainFragmentBuildersModule;
import com.disruption.daggerprac.di.main.MainModule;
import com.disruption.daggerprac.di.main.MainScope;
import com.disruption.daggerprac.di.main.MainViewModelModule;
import com.disruption.daggerprac.ui.auth.AuthActivity;
import com.disruption.daggerprac.ui.main.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuildersModule {

    @AuthScope
    @ContributesAndroidInjector(
            modules = {AuthViewModelsModule.class, AuthModule.class}
    )
    abstract AuthActivity contributeAuthActivity();

    @MainScope
    @ContributesAndroidInjector(
            modules = {MainFragmentBuildersModule.class, MainViewModelModule.class, MainModule.class}
    )
    abstract MainActivity contributeMainActivity();
}
