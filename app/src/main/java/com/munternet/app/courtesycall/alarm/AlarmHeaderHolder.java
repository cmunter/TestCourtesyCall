package com.munternet.app.courtesycall.alarm;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.munternet.app.courtesycall.R;
import com.munternet.app.courtesycall.utils.PreferenceUtil;

import static com.munternet.app.courtesycall.EditAlarmActivity.ALARM_ID_EXTRA;

/**
 * Created by chrtistianmunter on 3/16/17.
 */

public class AlarmHeaderHolder extends RecyclerView.ViewHolder {

    private static final String TAG = "AlarmHeaderHolder";
    private static final boolean DEBUG_VIEWS_LOG = true;

    public AlarmHeaderHolder(final View itemView) {
        super(itemView);
        if(DEBUG_VIEWS_LOG) Log.i(TAG, "::AlarmHeaderHolder " + getAdapterPosition());

        final CardView cardView = (CardView) itemView.findViewById(R.id.alarmTipCard);
        Boolean isHeaderTipShown = PreferenceUtil.isAlarmHeaderTipShown(itemView.getContext());
        if(!isHeaderTipShown) {
            cardView.setVisibility(View.VISIBLE);
            Button tipButton = (Button) itemView.findViewById(R.id.alarmHeaderTipButton);
            tipButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PreferenceUtil.saveAlarmHeaderTipShown(itemView.getContext());
                    cardView.setVisibility(View.GONE);
                }
            });
        }
    }

    public void bindHolder() {
        if(DEBUG_VIEWS_LOG) Log.i(TAG, "::AlarmHeaderHolder bindHolder ");
    }

}
