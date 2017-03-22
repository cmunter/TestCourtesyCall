package com.munternet.app.courtesycall.alarm;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.munternet.app.courtesycall.EditAlarmActivity;
import com.munternet.app.courtesycall.R;

/**
 * Created by chrtistianmunter on 3/16/17.
 */

public class AlarmEntryHolder extends RecyclerView.ViewHolder {

    private static final String TAG = "AlarmEntryHolder";
    private static final boolean DEBUG_VIEWS_LOG = true;

    private TextView labelText;
    private TextView relativeTimeText;
    private TextView timeText;
    private SwitchCompat alarmActiveSwitch;

    public AlarmEntryHolder(View itemView) {
        super(itemView);

        labelText = (TextView) itemView.findViewById(R.id.alarmItemLabelText);
        relativeTimeText = (TextView) itemView.findViewById(R.id.alarmItemRelativeTime);
        timeText = (TextView) itemView.findViewById(R.id.alarmItemTime);

        alarmActiveSwitch = (SwitchCompat) itemView.findViewById(R.id.alarmItemActivatedSwitch);
        alarmActiveSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(DEBUG_VIEWS_LOG) Log.i(TAG, "::AlarmEntryHolder " + getAdapterPosition());
            }
        });

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it
                    if(DEBUG_VIEWS_LOG) Log.i(TAG, "::AlarmEntryHolder onClick " + position);
                    // TODO: get context or handle this is fragment
                    labelText.getContext().startActivity(new Intent(labelText.getContext(), EditAlarmActivity.class));
                }
            }
        });


    }

    public void setLabelText(String label) {
        labelText.setText(label);
    }

    public void setRelativeTimeText(String date) {
        relativeTimeText.setText(date);
    }

    public void setTimeText(String date) {
        timeText.setText(date);
    }

    public void setAlarmActiveSwitch(boolean active) {
        alarmActiveSwitch.setChecked(active);
    }

}
