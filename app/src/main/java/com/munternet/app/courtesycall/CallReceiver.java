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

        // TODO Test UserId is still available after the app is killed

        int userId = intent.getExtras().getInt("USER_ID");

        Log.i(TAG, "CallReceiver.onReceive() " + userId + ", " + intent);

        Intent incomingCallIntent = new Intent(context, OutgoingCallActivity.class);
        incomingCallIntent.putExtra("USER_ID", userId);
        incomingCallIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(incomingCallIntent);
    }
}
