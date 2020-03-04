package com.disruption.daggerprac.di.main;

import com.disruption.daggerprac.network.main.MainApi;
import com.disruption.daggerprac.ui.main.posts.PostRecyclerAdapter;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public interface MainModule {

    @MainScope
    @Provides
    static PostRecyclerAdapter provideAdapter(){
        return new PostRecyclerAdapter();
    }

    @MainScope
    @Provides
    static MainApi provideAuthApi(Retrofit retrofit) {
        return retrofit.create(MainApi.class);
    }
}
