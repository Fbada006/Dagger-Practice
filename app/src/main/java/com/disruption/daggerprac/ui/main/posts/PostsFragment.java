package com.disruption.daggerprac.ui.main.posts;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.disruption.daggerprac.R;
import com.disruption.daggerprac.util.VerticalSpaceItemDecoration;
import com.disruption.daggerprac.viewmodels.ViewModelProviderFactory;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class PostsFragment extends DaggerFragment {
    private static final String TAG = "PostsFragment";

    private PostsViewModel mPostsViewModel;
    private RecyclerView mRecyclerView;

    @Inject
    PostRecyclerAdapter mPostRecyclerAdapter;

    @Inject
    ViewModelProviderFactory providerFactory;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_posts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mPostsViewModel = new ViewModelProvider(this, providerFactory).get(PostsViewModel.class);

        initRecView();
        subscribeObservers();
    }

    private void subscribeObservers() {
        mPostsViewModel.observePosts().removeObservers(getViewLifecycleOwner());

        mPostsViewModel.observePosts().observe(getViewLifecycleOwner(), listResource -> {
            if (listResource != null) {
                switch (listResource.status) {
                    case LOADING: {
                        Log.d(TAG, "onChanged: PostsFragment: LOADING...");
                        break;
                    }

                    case SUCCESS: {
                        Log.d(TAG, "onChanged: PostsFragment: got posts.");
                        mPostRecyclerAdapter.setPosts(listResource.data);
                        break;
                    }

                    case ERROR: {
                        Log.d(TAG, "onChanged: PostsFragment: ERROR... " + listResource.message);
                        break;
                    }
                }
            }
        });
    }

    private void initRecView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        VerticalSpaceItemDecoration verticalSpaceItemDecoration = new VerticalSpaceItemDecoration(15);
        mRecyclerView.addItemDecoration(verticalSpaceItemDecoration);
        mRecyclerView.setAdapter(mPostRecyclerAdapter);
    }
}
