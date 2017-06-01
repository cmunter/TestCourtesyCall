package com.munternet.app.courtesycall.ui.alarm;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.munternet.app.courtesycall.R;
import com.munternet.app.courtesycall.model.AlarmModel;

import java.util.List;

/**
 * Created by chrtistianmunter on 3/16/17.
 */

public class AlarmAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int HEADER_VIEW_TYPE = 0;
    private static final int CONTENT_VIEW_TYPE = 1;
    private List<AlarmModel> alarmList;

    public AlarmAdapter(List<AlarmModel> mAlarmList) {
        alarmList = mAlarmList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if(viewType==HEADER_VIEW_TYPE) {
            View alarmView = inflater.inflate(R.layout.alarm_list_header, parent, false);
            return new AlarmHeaderHolder(alarmView);
        } else {

            View alarmView = inflater.inflate(R.layout.alarm_list_item, parent, false);
            return new AlarmEntryHolder(alarmView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder.getItemViewType()==HEADER_VIEW_TYPE) {
            ((AlarmHeaderHolder)holder).bindHolder();
        } else {
            AlarmModel alarm = alarmList.get(position-1);
            ((AlarmEntryHolder)holder).bindHolder(alarm);
        }
    }

    @Override
    public int getItemCount() {
        return alarmList.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0) return HEADER_VIEW_TYPE;
        else return CONTENT_VIEW_TYPE;
    }
}
