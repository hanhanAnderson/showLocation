package com.hanhan.myapplication;

import android.content.Context;
import android.content.SharedPreferences;

public class KeyV {

    private SharedPreferences sharedPreferences;
    private static String PREF_NAME = "prefs";

    public KeyV() {
        // Blank
    }

    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static String getProg(Context context) {
        return getPrefs(context).getString("prog", "0");
    }

    public static void setProg(Context context, String input) {
        SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.putString("prog", input);
        editor.commit();
    }
}