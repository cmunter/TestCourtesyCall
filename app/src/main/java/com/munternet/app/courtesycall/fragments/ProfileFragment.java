package com.munternet.app.courtesycall.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.munternet.app.courtesycall.MainActivity;
import com.munternet.app.courtesycall.R;
import com.munternet.app.courtesycall.utils.PreferenceUtil;

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

    @Override
    public void onAttach(Context context) {
        int userId = PreferenceUtil.readAccountPreferences(getActivity());
        if(userId==-1) {
            showWelcomeDialog();
        }
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        Button editProfileButton = (Button) rootView.findViewById(R.id.editProfileButton);
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWelcomeDialog();
            }
        });
        return rootView;
    }

    private void showWelcomeDialog() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View dialogview = inflater.inflate(R.layout.dialog_welcome, null);
        final EditText userName = (EditText) dialogview.findViewById(R.id.welcomeUserName);
        final EditText userId = (EditText) dialogview.findViewById(R.id.welcomeUserId);

        AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(getActivity());
        dialogbuilder.setTitle("Welcome");
        dialogbuilder.setView(dialogview);
        dialogbuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int id = -1;
                try {
                    id = Integer.parseInt(userId.getText().toString());
                } catch (NumberFormatException e) {
                    // Ignore for now
                }

                PreferenceUtil.saveAccountPreferences(getActivity(), userName.getText().toString(), id);
            }
        });
        AlertDialog dialog = dialogbuilder.create();
        dialog.show();
    }

}
