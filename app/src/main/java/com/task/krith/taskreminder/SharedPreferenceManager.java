package com.task.krith.taskreminder;

import android.content.SharedPreferences;

import java.util.Set;

import javax.inject.Inject;

/**
 * Created by krith on 12/11/16.
 */

public class SharedPreferenceManager {

    public static SharedPreferences sharedPreferences = null;
    public static final String LOCAL_CACHE_CHECKLIST = "local_cache_checklist";

    @Inject
    public SharedPreferenceManager(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public static boolean putString(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        return editor.commit();
    }

    public static String getString(String key) {
        return sharedPreferences.getString(key, null);
    }

    public static boolean putInt(String key, int value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        return editor.commit();
    }

    public static int getInt(String key) {
        return sharedPreferences.getInt(key, 0);
    }

    public static boolean putLong(String key, long value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key, value);
        return editor.commit();
    }

    public static long getLong(String key) {
        return sharedPreferences.getLong(key, 0);
    }

    public static boolean putFloat(String key, float value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(key, value);
        return editor.commit();
    }

    public static float getFloat(String key) {
        return sharedPreferences.getLong(key, 0);
    }

    public static boolean putBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        return editor.commit();
    }

    public static boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    public static boolean putStringSet(String key, Set<String> value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(key, value);
        return editor.commit();
    }

    public static Set<String> getStringSet(String key) {
        return sharedPreferences.getStringSet(key, null);
    }

    public static boolean removeValue(String key) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        return editor.commit();
    }

    public static boolean removeAll() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        return editor.commit();
    }
}
