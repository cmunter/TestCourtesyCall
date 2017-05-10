package com.munternet.app.courtesycall.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.munternet.app.courtesycall.BuildConfig;
import com.munternet.app.courtesycall.MainActivity;
import com.munternet.app.courtesycall.R;
import com.munternet.app.courtesycall.utils.PreferenceUtil;

/**
 * Created by chrtistianmunter on 3/17/17.
 */

public class ProfileFragment extends Fragment {

    private static final String TAG = ProfileFragment.class.getSimpleName();
    private static final boolean DEBUG_LIVE_FRAGMENT_LOG = true;

    private TextView userNameView;
    private TextView userIdView;

    public static ProfileFragment newInstance() {
        if (DEBUG_LIVE_FRAGMENT_LOG) Log.i(TAG, "::newInstance");
        return new ProfileFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (DEBUG_LIVE_FRAGMENT_LOG) Log.i(TAG, "::onCreateView");

        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        userNameView = (TextView) rootView.findViewById(R.id.userName);
        userIdView = (TextView) rootView.findViewById(R.id.userId);
        setProfileViews();

        Button editProfileButton = (Button) rootView.findViewById(R.id.editProfileButton);
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).showWelcomeDialog(getActivity()).setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if (DEBUG_LIVE_FRAGMENT_LOG) Log.i(TAG, "::OnDismissListener");
                        setProfileViews();
                    }
                });
            }
        });

        int versionCode = BuildConfig.VERSION_CODE;
        String versionName = BuildConfig.VERSION_NAME;

        TextView versionNumberView = (TextView) rootView.findViewById(R.id.versionNumber);
        versionNumberView.setText("Version " + versionName);

        return rootView;
    }

    private void setProfileViews() {

        String userName = PreferenceUtil.readUserNamePreferences(getActivity());
        int userId = PreferenceUtil.readUserIdPreferences(getActivity());

        userNameView.setText(userName);
        if(userId>0) userIdView.setText(String.valueOf(userId));
    }


}
