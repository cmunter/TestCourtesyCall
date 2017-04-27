package com.munternet.app.courtesycall;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.munternet.app.courtesycall.fragments.AlarmFragment;
import com.munternet.app.courtesycall.fragments.CallFragment;
import com.munternet.app.courtesycall.fragments.ProfileFragment;
import com.munternet.app.courtesycall.sinch.calling.BaseActivity;
import com.munternet.app.courtesycall.sinch.calling.SinchService;
import com.munternet.app.courtesycall.utils.PreferenceUtil;
import com.munternet.app.courtesycall.utils.SinchUtil;
import com.sinch.android.rtc.SinchError;

/**
 * TODO: Maybe wakeup the receiving device 1 min before the alarm, to make sure the service is started and WiFi/data is on *
 */
public class MainActivity extends BaseActivity implements SinchService.StartFailedListener {

    private static final boolean DEBUG_TAB_ACTIVITY_LOG = false;
    private static final String TAG = MainActivity.class.getSimpleName();

    private BottomNavigationView mBottomBar;

    private int userId = -1;

    private static final int WRITE_STORAGE_PERMISSION_REQUEST_CODE = 111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupTabs();

        userId = PreferenceUtil.readUserIdPreferences(MainActivity.this);
        if(userId==0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, WRITE_STORAGE_PERMISSION_REQUEST_CODE);
            }
            showWelcomeDialog(MainActivity.this);
        }
    }

    @Override
    protected void onServiceConnected() {
        if (DEBUG_TAB_ACTIVITY_LOG) Log.d(TAG, "::onServiceConnected()");

        if(userId>0) {
            if (!getSinchServiceInterface().isStarted()) {
                getSinchServiceInterface().startClient(SinchUtil.sinchUserName(userId));
            }
        }
        getSinchServiceInterface().setStartListener(this);
    }

    @Override
    public void onStarted() {
        if (DEBUG_TAB_ACTIVITY_LOG) Log.d(TAG, "::onStarted()");
    }

    @Override
    public void onStartFailed(SinchError error) {
        if (DEBUG_TAB_ACTIVITY_LOG) Log.d(TAG, "::onStartFailed()" + error);
        Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show();
    }

    public static AlertDialog showWelcomeDialog(final Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_welcome, null);
        final EditText userNameView = (EditText) dialogView.findViewById(R.id.welcomeUserName);
        final EditText userIdView = (EditText) dialogView.findViewById(R.id.welcomeUserId);

        String userName = PreferenceUtil.readUserNamePreferences(context);
        int userId = PreferenceUtil.readUserIdPreferences(context);

        userNameView.setText(userName);
        if(userId>0) userIdView.setText(String.valueOf(userId));

        AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(context);
        dialogbuilder.setTitle("Welcome");
        dialogbuilder.setView(dialogView);
        dialogbuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int id = 0;
                try {
                    id = Integer.parseInt(userIdView.getText().toString());
                } catch (NumberFormatException e) {
                    // Ignore for now
                }

                PreferenceUtil.saveAccountPreferences(context, userNameView.getText().toString(), id);
            }
        });
        AlertDialog dialog = dialogbuilder.create();
        dialog.show();

        return dialog;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], int[] grantResults) {
        if (DEBUG_TAB_ACTIVITY_LOG) Log.d(TAG, "::onRequestPermissionsResult");

        switch (requestCode) {
            case WRITE_STORAGE_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (DEBUG_TAB_ACTIVITY_LOG) Log.d(TAG, "::onRequestPermissionsResult PERMISSION_GRANTED take photo");
                    // DO nothing right now. This could later be moved into a wizard
                } else {
                    if (DEBUG_TAB_ACTIVITY_LOG) Log.e(TAG, "::onRequestPermissionsResult PERMISSION_DENIED");
                }
                break;
            }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void setupTabs() {
        if (DEBUG_TAB_ACTIVITY_LOG) Log.d(TAG, "::setupTabs");

        mBottomBar = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        mBottomBar.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.action_alarm:
                                selectedFragment = AlarmFragment.newInstance();
                                break;
                            case R.id.action_call:
                                selectedFragment = CallFragment.newInstance();
                                break;
                            case R.id.action_profile:
                                selectedFragment = ProfileFragment.newInstance();
                                break;
                        }
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_layout, selectedFragment);
                        transaction.commit();
                        return true;
                    }
                });

        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, AlarmFragment.newInstance());
        transaction.commit();
    }

    public void stopSinchCallClient() {
        if (getSinchServiceInterface() != null) {
            getSinchServiceInterface().stopClient();
            Toast.makeText(this, "Stopping service.", Toast.LENGTH_LONG).show();
        }
        finish();
    }
}
