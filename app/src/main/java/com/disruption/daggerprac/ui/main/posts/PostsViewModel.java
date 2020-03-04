package com.disruption.daggerprac.ui.main.posts;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.disruption.daggerprac.SessionManager;
import com.disruption.daggerprac.models.Post;
import com.disruption.daggerprac.models.User;
import com.disruption.daggerprac.network.main.MainApi;
import com.disruption.daggerprac.ui.main.Resource;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class PostsViewModel extends ViewModel {

    private final MainApi mMainApi;
    private final SessionManager mSessionManager;
    private MediatorLiveData<Resource<List<Post>>> posts;

    @Inject
    public PostsViewModel(MainApi mainApi, SessionManager sessionManager) {
        mMainApi = mainApi;
        mSessionManager = sessionManager;
    }

    public LiveData<Resource<List<Post>>> observePosts() {
        if (posts == null) {
            posts = new MediatorLiveData<>();
            posts.setValue(Resource.loading(null));
            User user = mSessionManager.getAuthenticatedUser().getValue().data;
            assert user != null;
            int userId = user.getId();

            final LiveData<Resource<List<Post>>> source = LiveDataReactiveStreams.fromPublisher(
                    mMainApi.getPostsByUser(userId)
                            .onErrorReturn(throwable -> {
                                Post post = new Post();
                                post.setId(-1);
                                List<Post> posts = new ArrayList<>();
                                posts.add(post);
                                return posts;
                            })
                            .map((Function<List<Post>, Resource<List<Post>>>) posts -> {
                                if (posts.size() > 0) {
                                    if (posts.get(0).getId() == -1) {
                                        return Resource.error("Something went wrong", null);
                                    }
                                }
                                return Resource.success(posts);
                            })
                            .subscribeOn(Schedulers.io())
            );
            posts.addSource(source, listResource -> {
                posts.setValue(listResource);
                posts.removeSource(source);
            });
        }
        return posts;
    }
}
