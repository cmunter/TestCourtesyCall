package com.munternet.app.courtesycall;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class CallService extends Service {

    private WindowManager windowManager;

    public CallService() {
        Log.i("Service", "::CallService()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i("Service", "::onStartCommand()");

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        final View incomingCallView = layoutInflater.inflate(R.layout.activity_outgoing_call, null);


        // TYPE_SYSTEM_ALERT, TYPE_SYSTEM_ERROR, TYPE_SYSTEM_OVERLAY, LayoutParams.TYPE_PHONE

//        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
//                WindowManager.LayoutParams.WRAP_CONTENT,
//                WindowManager.LayoutParams.WRAP_CONTENT,
//                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
//                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
//                PixelFormat.TRANSLUCENT);


        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG,
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                        | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                PixelFormat.TRANSLUCENT
        );

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        windowManager.addView(incomingCallView, params);

        Button answerButton = (Button) incomingCallView.findViewById(R.id.performCallButton);
        answerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Service", "::onClick() answerButton");
                windowManager.removeView(incomingCallView);
            }
        });

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}