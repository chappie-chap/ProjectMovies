package com.chappie.made.projectmovies.helper;

import android.content.Context;
import android.content.SharedPreferences;

public class Pref {

    private static final String PREFS = "app_pref";

    private String langPref = "langPref";
    private String dailyPref = "dailyPref";
    private String todayPref = "todayPref";

    private Context context;
    private SharedPreferences sharedPreferences;

    public Pref(Context context) {
        this.context = context;
    }

    public int getLangPref() {
        sharedPreferences = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(langPref, 0);
    }

    public void setLangPref(int value) {
        sharedPreferences = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(langPref, value);
        editor.apply();
    }

    public void setDailyPref(Boolean value) {
        sharedPreferences = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(dailyPref, value);
        editor.apply();
    }

    public boolean getDailyPref() {
        sharedPreferences = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(dailyPref, false);
    }

    public void setTodayPref(Boolean value) {
        sharedPreferences = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(todayPref, value);
        editor.apply();
    }

    public boolean getTodayPref() {
        sharedPreferences = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(todayPref, false);
    }
}
