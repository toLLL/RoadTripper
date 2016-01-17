package com.usa.dev.drawerlayout.Helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.usa.dev.drawerlayout.R;

/**
 * Author: Philippe Volpe
 * Date: 13/01/2016
 * Purpose: Manage the preferences
 */
public class PreferencesManager {

    private static PreferencesManager sInstance;
    private final SharedPreferences mPref;
    Context mContext;

    private PreferencesManager(Context context) {
        mContext = context;
        mPref = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
    }

    public static synchronized void initializeInstance(Context context) {
        if (sInstance == null) {
            sInstance = new PreferencesManager(context);
        }
    }

    public static synchronized PreferencesManager getInstance() {
        if (sInstance == null) {
            throw new IllegalStateException(PreferencesManager.class.getSimpleName() +
                    " is not initialized, call initializeInstance(..) method first.");
        }
        return sInstance;
    }

    public String getAndroidSecret() {
        return mPref.getString(mContext.getString(R.string.saved_android_secret), mContext.getString(R.string.maps_api_key));
    }

    public void setAccessToken(String accessToken) {
        mPref.edit()
                .putString(mContext.getString(R.string.saved_access_token), accessToken)
                .apply();
    }

    public String getAccessToken() {
        return mPref.getString(mContext.getString(R.string.saved_access_token), "");
    }

    public void remove(String key) {
        mPref.edit()
                .remove(key)
                .apply();
    }

    public void clear() {
        mPref.edit()
                .clear()
                .apply();
    }
}
