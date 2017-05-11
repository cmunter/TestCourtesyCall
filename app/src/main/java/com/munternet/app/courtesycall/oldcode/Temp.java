package com.munternet.app.courtesycall.oldcode;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.munternet.app.courtesycall.PerformOutgoingCallReceiver;
import com.munternet.app.courtesycall.OutgoingCallActivity;
import com.munternet.app.courtesycall.constants.CallIntentExtrasConstants;

/**
 * Created by chrtistianmunter on 4/26/17.
 */

public class Temp {

    private IncommingCallService incommingCallService;


    public static void testCallToUser1004(Context context) {
        Intent incomingCallIntent = new Intent(context, OutgoingCallActivity.class);
        incomingCallIntent.putExtra(CallIntentExtrasConstants.USER_ID, 4004);
        incomingCallIntent.putExtra(CallIntentExtrasConstants.ALARM_LABEL, "Alarm label for test");
        incomingCallIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(incomingCallIntent);
    }


    public void TestTemp(){


        // incommingCallService = new IncommingCallService();


        // startCallService();
        //setTestAlarm1MinFromNow();


        // checkCanDrawOverlay();

    }

    private void startCallService() {
//        Snackbar.make(fab, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
//
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable(){
//            @Override
//            public void run() {
//                Log.i("Service", "::run()");
//                //incommingCallService.startActionFoo(MainActivity.this, null, null);
//                Intent intent = new Intent(getActivity(), CallService.class);
//                getActivity().startService(intent);
//            }
//        }, 10000);
    }

    private void setTestAlarm1MinFromNow(Context context) {
        AlarmManager mAlarmManager = (AlarmManager)context.getSystemService(context.ALARM_SERVICE);
        //long mDate = System.currentTimeMillis() + (1000*1);
        long mDate = System.currentTimeMillis() + (1000*60*2);

        Intent intent = new Intent(context, PerformOutgoingCallReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mAlarmManager.set(AlarmManager.RTC_WAKEUP, mDate, sender);
        Log.i("MUNTER", "::setTestAlarm1MinFromNow()");

        //Snackbar.make(fab, "Test call 2 mins from now", Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }


    private Drawable getDrawableFromResource(Activity context, int drawableResourceId) {
        return ContextCompat.getDrawable(context, drawableResourceId);
    }

    private void checkCanDrawOverlay(Activity context) {
        if(Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(context)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + context.getPackageName()));
            context.startActivityForResult(intent, 1234);
        }
    }



}
