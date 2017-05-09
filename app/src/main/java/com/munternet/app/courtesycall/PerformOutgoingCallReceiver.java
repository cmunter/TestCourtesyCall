package com.munternet.app.courtesycall;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.munternet.app.courtesycall.constants.CallIntentExtrasConstants;

/**
 * Created by chrtistianmunter on 2/17/17.
 */

public class PerformOutgoingCallReceiver extends BroadcastReceiver {

    private final String TAG = "PerformOutgoingCallReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        // TODO Test UserId is still available after the app is killed

        int userId = intent.getExtras().getInt(CallIntentExtrasConstants.USER_ID);
        String alarmLabel = intent.getExtras().getString(CallIntentExtrasConstants.ALARM_LABEL);

        //Log.i(TAG, "::onReceive() " + userId + ", " + intent);

        Intent incomingCallIntent = new Intent(context, OutgoingCallActivity.class);
        incomingCallIntent.putExtra(CallIntentExtrasConstants.USER_ID, userId);
        incomingCallIntent.putExtra(CallIntentExtrasConstants.ALARM_LABEL, alarmLabel);
        incomingCallIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(incomingCallIntent);
    }
}
