package com.munternet.app.courtesycall;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

public class IncomingCallActivity extends AppCompatActivity {

    private Ringtone mRingtone;
    private Vibrator mVibrator;
    private final long[] mVibratePattern = { 0, 500, 500 };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupWindowParams();

        setContentView(R.layout.activity_incoming_call);
        Button answerButton = (Button) findViewById(R.id.answerButton);
        answerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageView bellImage = (ImageView) findViewById(R.id.bellImage);
        Animation mAnimation = AnimationUtils.loadAnimation(this, R.anim.bell_ringing_animation);
        bellImage.startAnimation(mAnimation);
        ringtonePlay();
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
        mRingtone = RingtoneManager.getRingtone(IncomingCallActivity.this, mAlarmSound);
        mVibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

        mRingtone.play();
        mVibrator.vibrate(mVibratePattern, 0);
    }

    @Override
    protected void onPause() {
        mRingtone.stop();
        mVibrator.cancel();

        super.onPause();
    }
}
