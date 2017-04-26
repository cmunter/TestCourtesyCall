package com.munternet.app.courtesycall.utils;

import android.app.Activity;
import android.view.WindowManager;

/**
 * Created by chrtistianmunter on 4/26/17.
 */

public class WindowUtil {

    public static void setupWindowParamsForOnTop(Activity activity) {
        activity.getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);


        //        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
//                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
//                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
//                        |WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
//        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        //wm.addView(yourView, params);


        // TYPE_SYSTEM_ALERT, TYPE_SYSTEM_ERROR, TYPE_SYSTEM_OVERLAY, LayoutParams.TYPE_PHONE

    }
}
