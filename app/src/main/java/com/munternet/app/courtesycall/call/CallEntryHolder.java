package com.munternet.app.courtesycall.call;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.munternet.app.courtesycall.PerformOutgoingCallReceiver;
import com.munternet.app.courtesycall.R;
import com.munternet.app.courtesycall.constants.CallIntentExtrasConstants;
import com.munternet.app.courtesycall.models.AlarmModel;

/**
 * Created by chrtistianmunter on 3/16/17.
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
        alarmActiveSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(DEBUG_VIEWS_LOG) Log.i(TAG, "::CallEntryHolder " + getAdapterPosition());

                if(isChecked) {
                    setAlarm();
                } else {
                    // TODO remove alarm again
                }

            }
        });

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it
                    if(DEBUG_VIEWS_LOG) Log.i(TAG, "::CallEntryHolder onClick " + position);
                    // TODO: get context or handle this is fragment
                    // labelText.getContext().startActivity(new Intent(labelText.getContext(), EditAlarmActivity.class));
                }
            }
        });
    }

    public void bindHolder(AlarmModel alarm) {
        Context context = labelText.getContext();
        alarmModel = alarm;

        setLabelText(alarmModel.getLabel());
        setRelativeTimeText(alarmModel.getRelativeTimeString(context));
        setTimeText(alarmModel.getTimeString(context));
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

    private void setAlarm() {

        Context context = labelText.getContext();
        AlarmManager mAlarmManager = (AlarmManager)context.getSystemService(Activity.ALARM_SERVICE);
        //long mDate = System.currentTimeMillis() + (1000*10);
//        long mDate = System.currentTimeMillis() + (1000*60*2);
        long mDate = alarmModel.getTimeInMillis();


        int userId = Integer.parseInt(alarmModel.getUserId());
        Log.i("MUNTER", "::setAlarm() " + userId);

        Intent intent = new Intent(context, PerformOutgoingCallReceiver.class);
        intent.putExtra(CallIntentExtrasConstants.USER_ID, userId);
        intent.putExtra(CallIntentExtrasConstants.ALARM_LABEL, alarmModel.getLabel());

        // TODO use the Alarm ID in the requestcode to make it possible to remove the pending intent
        PendingIntent sender = PendingIntent.getBroadcast(context, 1000, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mAlarmManager.set(AlarmManager.RTC_WAKEUP, mDate, sender);
    }

}
