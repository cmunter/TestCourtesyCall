package com.munternet.app.courtesycall;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

import net.danlew.android.joda.JodaTimeAndroid;

/**
 * Created by chrtistianmunter on 3/16/17.
 */

public class CourtesyCallApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        JodaTimeAndroid.init(this);
        FirebaseApp.initializeApp(CourtesyCallApplication.this);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
