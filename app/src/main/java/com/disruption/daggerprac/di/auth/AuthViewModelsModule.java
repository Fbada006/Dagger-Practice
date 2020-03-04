package com.disruption.daggerprac.di.auth;

import androidx.lifecycle.ViewModel;

import com.disruption.daggerprac.di.ViewModelKey;
import com.disruption.daggerprac.ui.auth.AuthViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class AuthViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(AuthViewModel.class)
    public abstract ViewModel bindAuthViewModel(AuthViewModel authViewModel);
}
