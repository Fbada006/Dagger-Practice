package com.disruption.daggerprac.network.main;

import com.disruption.daggerprac.models.Post;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MainApi {

    @GET("posts")
    Flowable<List<Post>> getPostsByUser(@Query("userId") int id);
}
