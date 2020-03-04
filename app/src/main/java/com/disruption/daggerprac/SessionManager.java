package com.disruption.daggerprac;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.disruption.daggerprac.models.User;
import com.disruption.daggerprac.ui.auth.AuthResource;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SessionManager {
    private static final String TAG = "SessionManager";

    private MediatorLiveData<AuthResource<User>> mcachedUser = new MediatorLiveData<>();

    @Inject
    public SessionManager() {

    }

    public void authenticateWithId(final LiveData<AuthResource<User>> source) {
        if (mcachedUser != null) {
            mcachedUser.setValue(AuthResource.loading(null));
            mcachedUser.addSource(source, userAuthResource -> {
                mcachedUser.setValue(userAuthResource);
                mcachedUser.removeSource(source);
            });
        } else {
            Log.e(TAG, "authenticateWithId: previous session detected. Getting user from cache");
        }
    }

    public void logout() {
        Log.e(TAG, "Logging out ------------");
        mcachedUser.setValue(AuthResource.logout());
    }

    public LiveData<AuthResource<User>> getAuthenticatedUser() {
        return mcachedUser;
    }
}
