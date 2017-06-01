package com.munternet.app.courtesycall.ui.sinch.calling;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.munternet.app.courtesycall.app.calling.AudioPlayer;
import com.munternet.app.courtesycall.app.calling.SinchService;
import com.munternet.app.courtesycall.ui.MainActivity;
import com.munternet.app.courtesycall.R;
import com.munternet.app.courtesycall.ui.utils.WindowUtil;
import com.sinch.android.rtc.MissingPermissionException;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallEndCause;
import com.sinch.android.rtc.calling.CallListener;

import java.util.List;
import java.util.Map;

import static com.munternet.app.courtesycall.app.constants.CallIntentExtrasConstants.ALARM_LABEL;
import static com.munternet.app.courtesycall.app.constants.DevelopmentConstants.SILENCE_RINGTONE;

public class IncomingCallScreenActivity extends BaseActivity {

    static final String TAG = IncomingCallScreenActivity.class.getSimpleName();
    private static final boolean DEBUG = true;

    private String mCallId;
    private AudioPlayer mAudioPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sinch_incoming);
        WindowUtil.setupWindowParamsForOnTop(this);

        Button answer = (Button) findViewById(R.id.answerButton);
        answer.setOnClickListener(mClickListener);
        Button decline = (Button) findViewById(R.id.declineButton);
        decline.setOnClickListener(mClickListener);

        mAudioPlayer = new AudioPlayer(this);
        if(!SILENCE_RINGTONE) mAudioPlayer.playRingtone();
        mCallId = getIntent().getStringExtra(SinchService.CALL_ID);
    }

    @Override
    protected void onServiceConnected() {
        if(DEBUG) Log.i(TAG, "::onServiceConnected headerValue");
        Call call = getSinchServiceInterface().getCall(mCallId);
        if (call != null) {
            call.addCallListener(new SinchCallListener());

//            for(String headerKey : call.getHeaders().keySet()) {
//                if(DEBUG) Log.i(TAG, "::onServiceConnected headerKey: " + headerKey);
//            }
//            for(String headerValue : call.getHeaders().values()) {
//                if(DEBUG) Log.i(TAG, "::onServiceConnected headerValue: " + headerValue);
//            }

            Map<String, String> headerMap = call.getHeaders();
            String alarmLabel = headerMap.get(ALARM_LABEL);
            TextView remoteUser = (TextView) findViewById(R.id.remoteUser);
            //remoteUser.setText(call.getRemoteUserId());
            remoteUser.setText(alarmLabel);
        } else {
            Log.e(TAG, "Started with invalid callId, aborting");
            finish();
        }
    }

    private void answerClicked() {
        mAudioPlayer.stopRingtone();
        Call call = getSinchServiceInterface().getCall(mCallId);
        if (call != null) {
            try {
                call.answer();
                Intent intent = new Intent(this, CallScreenActivity.class);
                intent.putExtra(SinchService.CALL_ID, mCallId);
                startActivity(intent);
            } catch (MissingPermissionException e) {
                ActivityCompat.requestPermissions(this, new String[]{e.getRequiredPermission()}, 0);
            }
        } else {
            finish();
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "You may now answer the call", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "This application needs permission to use your microphone to function properly.", Toast
                    .LENGTH_LONG).show();
        }
    }

    private void declineClicked() {
        mAudioPlayer.stopRingtone();
        Call call = getSinchServiceInterface().getCall(mCallId);
        if (call != null) {
            call.hangup();
        }
        finish();
    }

    private class SinchCallListener implements CallListener {

        @Override
        public void onCallEnded(Call call) {
            CallEndCause cause = call.getDetails().getEndCause();
            Log.i(TAG, "Call ended, cause: " + cause.toString());
            mAudioPlayer.stopRingtone();

            // Call isn't declined by receiver, Show notification
            if(cause!=CallEndCause.DENIED && cause!=CallEndCause.HUNG_UP) {
                showMissedAlarmNotification(IncomingCallScreenActivity.this);
            }

            finish();
        }

        @Override
        public void onCallEstablished(Call call) {
            Log.i(TAG, "Call established");
        }

        @Override
        public void onCallProgressing(Call call) {
            Log.i(TAG, "Call progressing");
        }

        @Override
        public void onShouldSendPushNotification(Call call, List<PushPair> pushPairs) {
            Log.i(TAG, "onShouldSendPushNotification");

            // Send a push through your push provider here, e.g. GCM
        }
    }

    private void showMissedAlarmNotification(Context mContext) {

        // TODO Show this notification if the incoming call is unasnwered
        Context context = mContext;

        int MISSED_ALARM_NOTIFICATION_ID = 100;
        int MISSED_ALARM_REQUEST_CODE = 100;

        final Intent openMainActivityIntent = new Intent(context, MainActivity.class);
        PendingIntent mainActivityIntent = PendingIntent.getActivity(context, MISSED_ALARM_REQUEST_CODE, openMainActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_bell_outline_24dp)
                .setContentTitle("Missed call")
                .setContentText(getString(R.string.app_name))
                .setAutoCancel(true)
                .setContentIntent(mainActivityIntent);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            mBuilder.setColor(context.getColor(R.color.colorAccent));
        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(MISSED_ALARM_NOTIFICATION_ID, mBuilder.build());
    }

    private OnClickListener mClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.answerButton:
                    answerClicked();
                    break;
                case R.id.declineButton:
                    declineClicked();
                    break;
            }
        }
    };
}
