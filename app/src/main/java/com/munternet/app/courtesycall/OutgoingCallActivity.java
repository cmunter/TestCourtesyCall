package com.munternet.app.courtesycall;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.munternet.app.courtesycall.constants.CallIntentExtrasConstants;
import com.munternet.app.courtesycall.sinch.calling.BaseActivity;
import com.munternet.app.courtesycall.sinch.calling.CallScreenActivity;
import com.munternet.app.courtesycall.sinch.calling.SinchService;
import com.sinch.android.rtc.MissingPermissionException;
import com.sinch.android.rtc.calling.Call;

import java.util.HashMap;
import java.util.Map;

import static com.munternet.app.courtesycall.constants.DevelopmentConstants.SILENCE_RINGTONE;

public class OutgoingCallActivity extends BaseActivity {

    private static final boolean DEBUG = true;
    private final String TAG = OutgoingCallActivity.class.getName();

    private Ringtone mRingtone;
    private Vibrator mVibrator;
    private final long[] mVibratePattern = { 0, 500, 500 };
    private Button performCallButton;

    private float[] lastAcceleration;
    private float[] currentAcceleration;
    private SensorManager sensorManager;
    private boolean isMovementDetected = false;
    private boolean isTimeThresholdPassedWithoutMovement = false;
    private SensorEventListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (DEBUG) Log.i(TAG, "::onCreate");

        setContentView(R.layout.activity_outgoing_call);

        if(!SILENCE_RINGTONE) ringtonePlay();

        final int userId = getIntent().getExtras().getInt(CallIntentExtrasConstants.USER_ID);
        String alarmLabel = getIntent().getExtras().getString(CallIntentExtrasConstants.ALARM_LABEL);

        final Map<String, String> headerMap = new HashMap<>();
        headerMap.put(CallIntentExtrasConstants.ALARM_LABEL, alarmLabel);

        performCallButton = (Button) findViewById(R.id.performCallButton);
        performCallButton.setEnabled(false);
        performCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performCall(userId, headerMap);
                finish();
            }
        });

        TextView alarmLabelText = (TextView) findViewById(R.id.infoText);
        alarmLabelText.setText(alarmLabel);

        ImageView bellImage = (ImageView) findViewById(R.id.bellImage);
        Animation mAnimation = AnimationUtils.loadAnimation(this, R.anim.bell_ringing_animation);
        bellImage.startAnimation(mAnimation);

        startDetectMovement();
    }

    @Override
    protected void onServiceConnected() {
        if (DEBUG) Log.i(TAG, "::onServiceConnected");
        performCallButton.setEnabled(true);
    }

    private void performCall(int receiverUserId, Map<String, String> headerMap) {
        String userName = "Call" + receiverUserId;
        try {
            Call call = getSinchServiceInterface().callUser(userName, headerMap);

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
            // TODO: this should proceede to performing the outgoing call
        } else {
            Toast.makeText(this, "This application needs permission to use your microphone to function properly.", Toast
                    .LENGTH_LONG).show();
        }
    }

    private void ringtonePlay() {
        Uri mAlarmSound = Settings.System.DEFAULT_RINGTONE_URI;
        mRingtone = RingtoneManager.getRingtone(OutgoingCallActivity.this, mAlarmSound);
        mVibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

        mRingtone.play();
        mVibrator.vibrate(mVibratePattern, 0);
    }

    @Override
    protected void onPause() {
        stopRingtone();
        stopDetectMovement();
        super.onPause();
    }

    private void stopRingtone() {
        if(mRingtone!=null) mRingtone.stop();
        if(mVibrator!=null) mVibrator.cancel();
    }

    private void startDetectMovement() {
        final float MOVEMENT_THRESHOLD = 5;
        final int TIME_THRESHOLD = 2000;

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // Try and detect if the device is already in motion
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!isMovementDetected) isTimeThresholdPassedWithoutMovement = true;
            }
        }, TIME_THRESHOLD);

        listener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                    currentAcceleration = event.values.clone();
                    if(lastAcceleration==null) lastAcceleration = currentAcceleration;

                    // Movement detection
                    float x = currentAcceleration[0] - lastAcceleration[0];
                    float y = currentAcceleration[1] - lastAcceleration[1];
                    float z = currentAcceleration[2] - lastAcceleration[2];

                    if(x>MOVEMENT_THRESHOLD || x<(-MOVEMENT_THRESHOLD)) {
                        isMovementDetected = true;
                    } else if (y>MOVEMENT_THRESHOLD || y<(-MOVEMENT_THRESHOLD)) {
                        isMovementDetected = true;
                    } else if (z>MOVEMENT_THRESHOLD || z<(-MOVEMENT_THRESHOLD)) {
                        isMovementDetected = true;
                    }

                    if (DEBUG) Log.i(TAG, "::onSensorChanged() x: " + x + ", y: " + y + ",z: " + z);

                    if(isMovementDetected && isTimeThresholdPassedWithoutMovement) {
                        if (DEBUG) Log.i(TAG, "::onSensorChanged() isMovementDetected");
                        stopRingtone();
                        sensorManager.unregisterListener(listener);
                    }
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) { }
        };

        sensorManager.registerListener(listener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void stopDetectMovement() {
        sensorManager.unregisterListener(listener);
    }
}
