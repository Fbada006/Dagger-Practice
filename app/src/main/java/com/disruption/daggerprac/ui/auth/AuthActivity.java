package com.disruption.daggerprac.ui.auth;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.RequestManager;
import com.disruption.daggerprac.R;
import com.disruption.daggerprac.ui.main.MainActivity;
import com.disruption.daggerprac.viewmodels.ViewModelProviderFactory;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public class AuthActivity extends DaggerAppCompatActivity {

    private static final String TAG = "AuthActivity";

    private EditText userId;

    @Inject
    ViewModelProviderFactory providerFactory;

    @Inject
    Drawable logo;

    @Inject
    RequestManager requestManager;

    private AuthViewModel mAuthViewModel;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        userId = findViewById(R.id.user_id_input);
        mProgressBar = findViewById(R.id.progress_bar);

        mAuthViewModel = new ViewModelProvider(this, providerFactory).get(AuthViewModel.class);

        subscribeObservers();

        findViewById(R.id.login_button).setOnClickListener(view -> {
            attemptLogin();
        });

        setLogo();
    }

    private void subscribeObservers() {
        mAuthViewModel.observeAuthState().observe(this, userAuthResource -> {
            if (userAuthResource != null) {
                switch (userAuthResource.status) {
                    case AUTHENTICATED:
                        showProgressBar(false);
                        onLoginSuccess();
                        break;
                    case ERROR:
                        showProgressBar(false);
                        break;
                    case LOADING:
                        showProgressBar(true);
                        break;
                    case NOT_AUTHENTICATED:
                        showProgressBar(false);
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + userAuthResource.status);
                }
            }
        });
    }

    private void onLoginSuccess() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void showProgressBar(boolean isVisible) {
        if (isVisible) mProgressBar.setVisibility(View.VISIBLE);
        else mProgressBar.setVisibility(View.GONE);
    }

    private void attemptLogin() {
        String uId = userId.getText().toString();
        if (TextUtils.isEmpty(uId)) {
            return;
        }

        mAuthViewModel.authenticateWithApi(Integer.parseInt(uId));
    }

    private void setLogo() {
        requestManager
                .load(logo)
                .into((ImageView) findViewById(R.id.login_logo));
    }
}










