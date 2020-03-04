package com.disruption.daggerprac.di.main;

import com.disruption.daggerprac.ui.main.posts.PostsFragment;
import com.disruption.daggerprac.ui.main.profile.ProfileFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MainFragmentBuildersModule {

    @ContributesAndroidInjector
    abstract ProfileFragment contributesProfileFragment();

    @ContributesAndroidInjector
    abstract PostsFragment contributesPostsFragment();
}
