package com.munternet.app.courtesycall.app.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by chrtistianmunter on 4/20/17.
 */

public class PreferenceUtil {

    private static final String COURTESY_CALL_PREFERENCES = "CourtesyCallPreferences";
    private static final String USER_NAME = "userName";
    private static final String USER_ID = "userId";
    private static final String ALARM_TIP = "alarmTip";
    private static final String CALL_TIP = "callTip";

    public static void saveAccountPreferences(Context context, String name, int id ) {
        SharedPreferences.Editor editor = context.getSharedPreferences(COURTESY_CALL_PREFERENCES, Activity.MODE_PRIVATE).edit();
        editor.putString(USER_NAME, name);
        editor.putInt(USER_ID, id);
        editor.apply();
    }

    public static int readUserIdPreferences(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(COURTESY_CALL_PREFERENCES, Activity.MODE_PRIVATE);
        int idName = prefs.getInt(USER_ID, 0);
        return idName;
    }

    public static String readUserNamePreferences(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(COURTESY_CALL_PREFERENCES, Activity.MODE_PRIVATE);
        String name = prefs.getString(USER_NAME, "");
        return name;
    }

    public static void saveAlarmHeaderTipShown(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(COURTESY_CALL_PREFERENCES, Activity.MODE_PRIVATE).edit();
        editor.putBoolean(ALARM_TIP, true);
        editor.apply();
    }

    public static void saveCallHeaderTipShown(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(COURTESY_CALL_PREFERENCES, Activity.MODE_PRIVATE).edit();
        editor.putBoolean(CALL_TIP, true);
        editor.apply();
    }

    public static boolean isAlarmHeaderTipShown(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(COURTESY_CALL_PREFERENCES, Activity.MODE_PRIVATE);
        Boolean shown = prefs.getBoolean(ALARM_TIP, false);
        return shown;
    }

    public static boolean isCallHeaderTipShown(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(COURTESY_CALL_PREFERENCES, Activity.MODE_PRIVATE);
        Boolean shown = prefs.getBoolean(CALL_TIP, false);
        return shown;
    }
}
