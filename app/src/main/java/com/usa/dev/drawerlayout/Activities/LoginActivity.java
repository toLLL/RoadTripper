package com.usa.dev.drawerlayout.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.usa.dev.drawerlayout.Helper.PreferencesManager;
import com.usa.dev.drawerlayout.Helper.RequestManager;
import com.usa.dev.drawerlayout.R;


public class LoginActivity extends AppCompatActivity {

    CallbackManager mCallbackManager;
    AccessTokenTracker mAccessTokenTracker;
    RequestManager mRequestManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Facebook SDK
        FacebookSdk.sdkInitialize(getApplicationContext());

        // Initialize Preferences Manager
        PreferencesManager.initializeInstance(getApplicationContext());

        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRequestManager = RequestManager.getInstance();
        mAccessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if (currentAccessToken != null)
                    mRequestManager.updateFacebookToken(currentAccessToken.getToken());
            }
        };
        mAccessTokenTracker.startTracking();
        final LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("user_friends");
        mCallbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("LoginActivity", "OnCreate.FacebookCallBack.onSuccess: Login successful!");
                registerToServer(AccessToken.getCurrentAccessToken().getToken());
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }

            @Override
            public void onCancel() {
                Log.d("LoginActivity", "onCreate.FacebookCallBack.onCancel: Login canceled.");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("LoginActivity", "onCreate.FacebookCallBack.onError: Login error");
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void registerToServer(String facebookToken) {
        mRequestManager.login(facebookToken);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mAccessTokenTracker.stopTracking();
        Log.d("LoginActivity", "onDestroy: Stop tracking ");
    }
}
