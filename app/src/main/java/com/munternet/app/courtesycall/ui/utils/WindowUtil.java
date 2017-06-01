package com.munternet.app.courtesycall.ui.utils;

import android.app.Activity;
import android.view.WindowManager;

public class WindowUtil {

    public static void setupWindowParamsForOnTop(Activity activity) {
        activity.getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        // TYPE_SYSTEM_ALERT, TYPE_SYSTEM_ERROR, TYPE_SYSTEM_OVERLAY, LayoutParams.TYPE_PHONE
    }
}
