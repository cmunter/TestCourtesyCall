package com.munternet.app.courtesycall.sinch.calling;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;


public abstract class BaseActivity extends AppCompatActivity implements ServiceConnection {

    private SinchService.SinchServiceInterface mSinchServiceInterface;

    private static final boolean DEBUG = true;
    private final String TAG = BaseActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getApplicationContext().bindService(new Intent(this, SinchService.class), this,
                BIND_AUTO_CREATE);

        if (DEBUG) Log.i(TAG, "::onCreate");
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        if (DEBUG) Log.i(TAG, "::onServiceConnected");

        if (SinchService.class.getName().equals(componentName.getClassName())) {
            mSinchServiceInterface = (SinchService.SinchServiceInterface) iBinder;
            onServiceConnected();
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        if (DEBUG) Log.i(TAG, "::onServiceDisconnected");

        if (SinchService.class.getName().equals(componentName.getClassName())) {
            mSinchServiceInterface = null;
            onServiceDisconnected();
        }
    }

    protected void onServiceConnected() {
        // for subclasses
        if (DEBUG) Log.i(TAG, "::onServiceConnected");
    }

    protected void onServiceDisconnected() {
        // for subclasses
        if (DEBUG) Log.i(TAG, "::onServiceDisconnected");
    }

    protected SinchService.SinchServiceInterface getSinchServiceInterface() {
        return mSinchServiceInterface;
    }

}