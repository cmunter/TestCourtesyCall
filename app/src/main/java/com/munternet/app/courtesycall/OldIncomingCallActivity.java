package com.munternet.app.courtesycall;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.munternet.app.courtesycall.sinch.calling.BaseActivity;
import com.munternet.app.courtesycall.sinch.calling.CallScreenActivity;
import com.munternet.app.courtesycall.sinch.calling.SinchService;
import com.sinch.android.rtc.MissingPermissionException;
import com.sinch.android.rtc.calling.Call;

public class OldIncomingCallActivity extends BaseActivity {

    private static final boolean DEBUG = true;
    private final String TAG = OldIncomingCallActivity.class.getName();

    private Ringtone mRingtone;
    private Vibrator mVibrator;
    private final long[] mVibratePattern = { 0, 500, 500 };
    private Button answerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (DEBUG) Log.i(TAG, "::onCreate");

        setupWindowParams();



        final int userId = getIntent().getExtras().getInt("USER_ID");

        setContentView(R.layout.activity_incoming_call);
        answerButton = (Button) findViewById(R.id.answerButton);
        answerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answerButton.setText("User: " + userId);
                performCall(userId);
                finish();
            }
        });

        ImageView bellImage = (ImageView) findViewById(R.id.bellImage);
        Animation mAnimation = AnimationUtils.loadAnimation(this, R.anim.bell_ringing_animation);
        bellImage.startAnimation(mAnimation);
        //ringtonePlay();
    }

    @Override
    protected void onServiceConnected() {
        if (DEBUG) Log.i(TAG, "::onServiceConnected");
        answerButton.setText("connected : ");
        //mCallButton.setEnabled(true);
    }

    private void performCall(int recieverUserId) {
        String userName = "Call" + recieverUserId;
        if (userName.isEmpty()) {
            Toast.makeText(this, "Please enter a user to call", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            Call call = getSinchServiceInterface().callUser(userName);
            if (call == null) {
                // Service failed for some reason, show a Toast and abort
                Toast.makeText(this, "Service is not started. Try stopping the service and starting it again before "
                        + "placing a call.", Toast.LENGTH_LONG).show();
                return;
            }
            String callId = call.getCallId();
            Intent callScreen = new Intent(this, CallScreenActivity.class);
            callScreen.putExtra(SinchService.CALL_ID, callId);
            startActivity(callScreen);
        } catch (MissingPermissionException e) {
            ActivityCompat.requestPermissions(this, new String[]{e.getRequiredPermission()}, 0);
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "You may now place a call", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "This application needs permission to use your microphone to function properly.", Toast
                    .LENGTH_LONG).show();
        }
    }

    private void setupWindowParams() {
        getWindow().addFlags(
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

    private void ringtonePlay() {
        Uri mAlarmSound = Settings.System.DEFAULT_RINGTONE_URI;
        mRingtone = RingtoneManager.getRingtone(OldIncomingCallActivity.this, mAlarmSound);
        mVibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

        mRingtone.play();
        mVibrator.vibrate(mVibratePattern, 0);
    }

    @Override
    protected void onPause() {
        if(mRingtone!=null) mRingtone.stop();
        if(mVibrator!=null) mVibrator.cancel();

        super.onPause();
    }
}
