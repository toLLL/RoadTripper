package com.usa.dev.drawerlayout.Helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.usa.dev.drawerlayout.API.RoadTripperClient;
import com.usa.dev.drawerlayout.API.ServiceGenerator;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Author: Philippe Volpe
 * Date: 13/01/2016
 * Purpose: Handling all request
 */
public class RequestManager {

    /************************************************
     * Start - Objects
     ************************************************/

    public static class User {
        public String accessToken;
    }

    /************************************************
     * END - Objects
     ************************************************/

    private static RequestManager mInstance = new RequestManager();
    private RoadTripperClient mClient;

    private RequestManager() {
        mClient = ServiceGenerator.createService(RoadTripperClient.class);
    }

    public static RequestManager getInstance() {
        if (mInstance == null)
            mInstance = new RequestManager();
        return mInstance;
    }

    /**
     * Log the user
     * @param facebookToken A string representing a facebook access token
     */
    public void login(String facebookToken) {
        Call<User> call = mClient.login("Basic "+PreferencesManager.getInstance().getAndroidSecret(),facebookToken);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Response<User> response, Retrofit retrofit) {
                User user = response.body();

                switch (response.code()) {
                    case 200:
                        // Updating shared preferences with new accessToken
                        PreferencesManager preferencesManager = PreferencesManager.getInstance();
                        preferencesManager.setAccessToken(user.accessToken);
                        verifyToken();
                        renewToken();
                        break;
                    case 400:
                        Log.d("RequestManager", "login.onResponse: Invalid request (400)");
                        break;
                    case 404:
                        Log.d("RequestManager", "login.onResponse: Unauthorized request: "+response.message());
                        break;
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("RequestManager", "login.onFailure: "+t.getMessage());
            }
        });
    }

    /**
     * Simple server ping.
     * @return true if server is running, false otherwise.
     */
    public boolean ping() {
        return true;
    }

    /**
     * Update the server side facebook token
     * @param newToken The new token
     */
    public void updateFacebookToken(String newToken) {
        Call<Void> call = mClient.updateFacebookToken("Bearer " + PreferencesManager.getInstance().getAccessToken(), newToken);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Response<Void> response, Retrofit retrofit) {
                switch (response.code()) {
                    case 200:
                        Log.d("RequestManager", "updateFacebookToken.onResponse: Operation succeed");
                        break;
                    case 400:
                        Log.d("RequestManager", "updateFacebookToken.onResponse: Invalid request");
                        break;
                    case 401:
                        Log.d("RequestManager", "updateFacebookToken.onResponse: Unauthorized request");
                        break;
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("RequestManager", "updateFacebookToken.onFailure: "+t.getMessage());
            }
        });
    }

    public void verifyToken() {
        Call<Void>  call = mClient.verifyToken("Bearer " + PreferencesManager.getInstance().getAccessToken());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Response<Void> response, Retrofit retrofit) {
                switch (response.code()) {
                    case 200:
                        Log.d("RequestManager", "verifyToken.onResponse: Valid token");
                        break;
                    case 401:
                        Log.d("RequestManager", "verifyToken.onResponse: Invalid token");
                        renewToken();
                        break;
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("RequestManager", "verifyToken.onFailure: "+t.getMessage());
            }
        });
    }

    public void renewToken() {
        Call<String> call = mClient.renewToken("Basic "+PreferencesManager.getInstance().getAndroidSecret(), PreferencesManager.getInstance().getAccessToken());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                switch (response.code()) {
                    case 200:
                        Log.d("RequestManager", "renewToken.onResponse: Operation succeed - Updating access token");
                        PreferencesManager.getInstance().setAccessToken(response.body());
                        break;
                    case 400:
                        Log.e("RequestManager", "renewToken.onResponse: Invalid request");
                        break;
                    case 401:
                        Log.e("RequestManager", "renewToken.onResponse: Unauthorized request");
                        break;
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("RequestManager", "RequestManager.onFailure: "+t.getMessage());
            }
        });
    }
}
