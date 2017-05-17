package com.munternet.app.courtesycall.call;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.munternet.app.courtesycall.R;
import com.munternet.app.courtesycall.models.AlarmModel;

/**
 *
 */
public class CallEntryHolder extends RecyclerView.ViewHolder {

    private static final String TAG = "CallEntryHolder";
    private static final boolean DEBUG_VIEWS_LOG = true;

    private TextView labelText;
    private TextView relativeTimeText;
    private TextView timeText;
    private AppCompatCheckBox alarmActiveSwitch;

    private AlarmModel alarmModel;

    public CallEntryHolder(View itemView) {
        super(itemView);

        labelText = (TextView) itemView.findViewById(R.id.callItemLabelText);
        relativeTimeText = (TextView) itemView.findViewById(R.id.callItemRelativeTime);
        timeText = (TextView) itemView.findViewById(R.id.callItemTime);
        alarmActiveSwitch = (AppCompatCheckBox) itemView.findViewById(R.id.callItemAssignCheck);
    }

    public void bindHolder(AlarmModel alarm, final OnCallItemClickListener mListener) {
        Context context = labelText.getContext();
        alarmModel = alarm;

        setLabelText(alarmModel.getLabel());
        setRelativeTimeText(alarmModel.getRelativeTimeString(context));
        setTimeText(alarmModel.getTimeString(context));

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it
                    if(DEBUG_VIEWS_LOG) Log.i(TAG, "::CallEntryHolder onClick " + position);
                    boolean isChecked = !alarmActiveSwitch.isChecked();
                    mListener.onItemClick(alarmModel, isChecked);
                    alarmActiveSwitch.setChecked(isChecked);
                }
            }
        });

        if(!alarm.getAsigneeUserId().isEmpty()) {
            alarmActiveSwitch.setChecked(true);
        }
    }

    private void setLabelText(String label) {
        labelText.setText(label);
    }

    private void setRelativeTimeText(String date) {
        relativeTimeText.setText(date);
    }

    private void setTimeText(String date) {
        timeText.setText(date);
    }

}
