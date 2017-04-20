package com.munternet.app.courtesycall.utils;

import android.app.Activity;
import android.content.SharedPreferences;

/**
 * Created by chrtistianmunter on 4/20/17.
 */

public class PreferenceUtil {

    private static final String COURTESY_CALL_PREFERENCES = "CourtesyCallPreferences";

    public static void saveAccountPreferences(Activity activity, String name, int id ) {
        SharedPreferences.Editor editor = activity.getSharedPreferences(COURTESY_CALL_PREFERENCES, Activity.MODE_PRIVATE).edit();
        editor.putString("name", name);
        editor.putInt("idName", id);
        editor.apply();
    }

    public static int readAccountPreferences(Activity activity) {
        SharedPreferences prefs = activity.getSharedPreferences(COURTESY_CALL_PREFERENCES, Activity.MODE_PRIVATE);
//        String restoredText = prefs.getString("text", null);
//        if (restoredText != null) {
        String name = prefs.getString("name", "");
        int idName = prefs.getInt("idName", -1);
        return idName;
//        }
//        return -1;
    }
}
