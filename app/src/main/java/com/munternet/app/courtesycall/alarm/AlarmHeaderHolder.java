package com.munternet.app.courtesycall.alarm;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import static com.munternet.app.courtesycall.EditAlarmActivity.ALARM_ID_EXTRA;

/**
 * Created by chrtistianmunter on 3/16/17.
 */

public class AlarmHeaderHolder extends RecyclerView.ViewHolder {

    private static final String TAG = "AlarmHeaderHolder";
    private static final boolean DEBUG_VIEWS_LOG = true;

    public AlarmHeaderHolder(View itemView) {
        super(itemView);
        if(DEBUG_VIEWS_LOG) Log.i(TAG, "::AlarmHeaderHolder " + getAdapterPosition());

    }

    public void bindHolder() {
        if(DEBUG_VIEWS_LOG) Log.i(TAG, "::AlarmHeaderHolder bindHolder ");
    }

}
