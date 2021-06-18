package com.nero.instagram_clone.preferencehelper;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.GestureDetector;

public class PreferenceHelper {

    private static final String PREF_NAME = "insta_clone";

    public static SharedPreferences sharedPreferences;

    public static SharedPreferences getSharedPreferences(Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        }
        return sharedPreferences;
    }

    public static void writeIntToPreference(String key, int value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static void writeStringToPreference(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void writeBooleanToPreference(String key, boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static boolean readBooleanFromPreference(String key) {
        return sharedPreferences.getBoolean(key, true);
    }

    public static String readStringFromPreference(String key) {
        return sharedPreferences.getString(key, "");
    }

    public static int readIntFromPreference(String key) {
        return sharedPreferences.getInt(key, 0);
    }





}
