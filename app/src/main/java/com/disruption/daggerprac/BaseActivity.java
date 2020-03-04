package com.disruption.daggerprac;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.disruption.daggerprac.ui.auth.AuthActivity;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public abstract class BaseActivity extends DaggerAppCompatActivity {
    private static final String TAG = "BaseActivity";

    @Inject
    public SessionManager mSessionManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        subscribeObservers();
    }

    private void subscribeObservers() {
        mSessionManager.getAuthenticatedUser().observe(this, userAuthResource -> {
            if (userAuthResource != null) {
                assert userAuthResource.data != null;
                switch (userAuthResource.status) {
                    case AUTHENTICATED:
                        Log.e(TAG, "subscribeObservers:------------ authenticated " + userAuthResource.data.getEmail());
                        break;
                    case ERROR:
                        Log.e(TAG, "subscribeObservers:------------ error " + userAuthResource.message);
                        break;
                    case LOADING:

                        break;
                    case NOT_AUTHENTICATED:
                        navLogScreen();
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + userAuthResource.status);
                }
            }
        });
    }

    private void navLogScreen() {
        Intent intent = new Intent(this, AuthActivity.class);
        startActivity(intent);
        finish();
    }
}
