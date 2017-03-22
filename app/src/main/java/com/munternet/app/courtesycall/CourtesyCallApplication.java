package com.munternet.app.courtesycall;

import android.app.Application;

import net.danlew.android.joda.JodaTimeAndroid;

/**
 * Created by chrtistianmunter on 3/16/17.
 */

public class CourtesyCallApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        JodaTimeAndroid.init(this);
    }
}
