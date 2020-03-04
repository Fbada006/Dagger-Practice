package com.disruption.daggerprac.ui.auth;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.ViewModel;

import com.disruption.daggerprac.SessionManager;
import com.disruption.daggerprac.models.User;
import com.disruption.daggerprac.network.auth.AuthApi;

import javax.inject.Inject;

import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class AuthViewModel extends ViewModel {
    private static final String TAG = "AuthViewModel";
    private final AuthApi mAuthApi;

    private final SessionManager mSessionManager;

    @Inject
    public AuthViewModel(AuthApi authApi, SessionManager sessionManager) {
        this.mAuthApi = authApi;
        this.mSessionManager = sessionManager;
    }

    public void authenticateWithApi(int userId) {
        mSessionManager.authenticateWithId(queryUserId(userId));
    }

    private LiveData<AuthResource<User>> queryUserId(int userId) {
        return LiveDataReactiveStreams.fromPublisher(
                mAuthApi.getUser(userId)

                        .onErrorReturn(throwable -> {
                            User errorUser = new User();
                            errorUser.setId(-1);
                            return errorUser;
                        })

                        .map((Function<User, AuthResource<User>>) user -> {
                            if (user.getId() == -1) {
                                return AuthResource.error("Could not authenticate", null);
                            }

                            return AuthResource.authenticated(user);
                        })

                        .subscribeOn(Schedulers.io())
        );
    }

    public LiveData<AuthResource<User>> observeAuthState() {
        return mSessionManager.getAuthenticatedUser();
    }
}
