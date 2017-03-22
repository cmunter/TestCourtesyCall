package com.munternet.app.courtesycall;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.munternet.app.courtesycall.fragments.AlarmFragment;
import com.munternet.app.courtesycall.fragments.CallFragment;
import com.munternet.app.courtesycall.fragments.ProfileFragment;

public class MainActivity extends AppCompatActivity {

    private static final boolean DEBUG_TAB_ACTIVITY_LOG = false;
    private static final String TAG = MainActivity.class.getSimpleName();

    private BottomNavigationView mBottomBar;

    static final public String userId = "1002";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupTabs();

        // checkCanDrawOverlay();

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

    private Drawable getDrawableFromResource(int drawableResourceId) {
        return ContextCompat.getDrawable(this, drawableResourceId);
    }

    private void checkCanDrawOverlay() {
        if(Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(MainActivity.this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, 1234);
        }
    }

}
