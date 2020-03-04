package com.disruption.daggerprac.ui.main.profile;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.disruption.daggerprac.SessionManager;
import com.disruption.daggerprac.models.User;
import com.disruption.daggerprac.ui.auth.AuthResource;

import javax.inject.Inject;

public class ProfileViewModel extends ViewModel {
    private static final String TAG = "ProfileViewModel";

    private final SessionManager mSessionManager;

    @Inject
    public ProfileViewModel(SessionManager sessionManager) {
        Log.e(TAG, "Profile View Model is ready ---------");
        mSessionManager = sessionManager;
    }

    public LiveData<AuthResource<User>> getAuthenticatedUser(){
        return mSessionManager.getAuthenticatedUser();
    }
}
