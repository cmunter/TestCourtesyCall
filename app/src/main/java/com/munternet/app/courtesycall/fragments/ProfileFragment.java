package com.munternet.app.courtesycall.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.munternet.app.courtesycall.R;

/**
 * Created by chrtistianmunter on 3/17/17.
 */

public class ProfileFragment extends Fragment {

    private static final String TAG = ProfileFragment.class.getSimpleName();
    private static final boolean DEBUG_LIVE_FRAGMENT_LOG = false;

    public static ProfileFragment newInstance() {
        if (DEBUG_LIVE_FRAGMENT_LOG) Log.d(TAG, "::newInstance");
        return new ProfileFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        return rootView;
    }
}
