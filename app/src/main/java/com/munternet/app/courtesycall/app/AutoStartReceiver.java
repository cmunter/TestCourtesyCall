package com.munternet.app.courtesycall.app;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.munternet.app.courtesycall.app.constants.CallIntentExtrasConstants;
import com.munternet.app.courtesycall.app.calling.SinchService;
import com.munternet.app.courtesycall.app.utils.PreferenceUtil;

/**
 * Created by chrtistianmunter on 4/27/17.
 */

public class AutoStartReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        int userId = PreferenceUtil.readUserIdPreferences(context);

        if(userId>0) {
            Intent startServiceIntent = new Intent(context, SinchService.class);
            startServiceIntent.putExtra(CallIntentExtrasConstants.USER_ID, userId);
            context.startService(startServiceIntent);
        }
    }
}
