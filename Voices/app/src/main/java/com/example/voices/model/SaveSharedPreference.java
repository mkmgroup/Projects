package com.example.voices.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Juan on 22/02/2018.
 */

public class SaveSharedPreference
{
    static final String PREF_USER_NAME= "username";
    static final String PREF_USER_PASS= "password";
    static final String PREF_USER_ASSETS_ON_FILES= "ool";

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setUserNameAndPassWord(Context ctx, String userName, String password)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_NAME, userName);
        editor.putString(PREF_USER_PASS, password);
        editor.commit();
    }

    public static void setAssetsOnFileToTrue(Context ctx, String userName, String password)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_NAME, userName);
        editor.putString(PREF_USER_PASS, password);
        editor.commit();
    }

    public static String getUserName(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_USER_NAME, "");
    }
    public static String getPassword(Context context){
        return  getSharedPreferences(context).getString(PREF_USER_PASS, "");
    }
}