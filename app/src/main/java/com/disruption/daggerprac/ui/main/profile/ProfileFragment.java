package com.disruption.daggerprac.ui.main.profile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.disruption.daggerprac.R;
import com.disruption.daggerprac.models.User;
import com.disruption.daggerprac.ui.auth.AuthResource;
import com.disruption.daggerprac.viewmodels.ViewModelProviderFactory;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class ProfileFragment extends DaggerFragment {
    private static final String TAG = "ProfileFragment";

    @Inject
    ViewModelProviderFactory providerFactory;

    private ProfileViewModel mProfileViewModel;
    private TextView email, username, website;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mProfileViewModel = new ViewModelProvider(this, providerFactory).get(ProfileViewModel.class);
        email = view.findViewById(R.id.email);
        username = view.findViewById(R.id.username);
        website = view.findViewById(R.id.website);
        subscribeObservers();
    }

    private void subscribeObservers() {
        mProfileViewModel.getAuthenticatedUser().removeObservers(getViewLifecycleOwner());

        mProfileViewModel.getAuthenticatedUser().observe(getViewLifecycleOwner(), userAuthResource -> {
            if (userAuthResource != null) {
                switch (userAuthResource.status) {
                    case AUTHENTICATED: {
                        Log.d(TAG, "onChanged: ProfileFragment: AUTHENTICATED... " +
                                "Authenticated as: " + userAuthResource.data.getEmail());
                        setUserDetails(userAuthResource.data);
                        break;
                    }

                    case ERROR: {
                        Log.d(TAG, "onChanged: ProfileFragment: ERROR...");
                        setErrorDetails(userAuthResource.message);
                        break;
                    }
                }
            }
        });
    }

    private void setErrorDetails(String message) {
        email.setText(message);
        username.setText("error");
        website.setText("error");
    }

    private void setUserDetails(User user) {
        email.setText(user.getEmail());
        username.setText(user.getUsername());
        website.setText(user.getWebsite());
    }
}
