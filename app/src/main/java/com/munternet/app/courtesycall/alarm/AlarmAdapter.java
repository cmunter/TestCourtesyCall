package com.munternet.app.courtesycall.alarm;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.munternet.app.courtesycall.R;
import com.munternet.app.courtesycall.models.AlarmModel;

import java.util.List;

/**
 * Created by chrtistianmunter on 3/16/17.
 */

public class AlarmAdapter extends RecyclerView.Adapter<AlarmEntryHolder> {

    private List<AlarmModel> alarmList;
    private Context context;


    public AlarmAdapter(Context mContext, List<AlarmModel> mAlarmList) {
        context = mContext;
        alarmList = mAlarmList;
    }

    @Override
    public AlarmEntryHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View alarmView = inflater.inflate(R.layout.alarm_list_item, parent, false);
        return new AlarmEntryHolder(alarmView);
    }

    @Override
    public void onBindViewHolder(AlarmEntryHolder holder, int position) {
        AlarmModel alarm = alarmList.get(position);
        holder.setLabelText(alarm.getLabel());
        holder.setRelativeTimeText(alarm.getRelativeTimeString(context));
        holder.setTimeText(alarm.getTimeString(context));
    }

    @Override
    public int getItemCount() {
        return alarmList.size();
    }
}
