package com.munternet.app.courtesycall;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by chrtistianmunter on 2/17/17.
 */

public class CallReceiver extends BroadcastReceiver {

    private final String TAG = "CallReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "CallReceiver.onReceive()");

        Intent incomingCallIntent = new Intent(context, IncomingCallActivity.class);
        incomingCallIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(incomingCallIntent);
    }
}
