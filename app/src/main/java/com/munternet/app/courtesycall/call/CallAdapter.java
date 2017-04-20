package com.munternet.app.courtesycall.call;

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

public class CallAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int HEADER_VIEW_TYPE = 0;
    private static final int CONTENT_VIEW_TYPE = 1;
    private List<AlarmModel> alarmList;
    private Context context;


    public CallAdapter(Context mContext, List<AlarmModel> mAlarmList) {
        context = mContext;
        alarmList = mAlarmList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if(viewType==HEADER_VIEW_TYPE) {
            View callView = inflater.inflate(R.layout.call_list_header, parent, false);
            return new CallHeaderHolder(callView);

        } else {
            View callView = inflater.inflate(R.layout.call_list_item, parent, false);
            return new CallEntryHolder(callView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder.getItemViewType()==HEADER_VIEW_TYPE) {
            ((CallHeaderHolder)holder).bindHolder();
        } else {
            AlarmModel alarm = alarmList.get(position-1);
            CallEntryHolder callHolder = (CallEntryHolder) holder;
            callHolder.setLabelText(alarm.getLabel());
            callHolder.setRelativeTimeText(alarm.getRelativeTimeString(context));
            callHolder.setTimeText(alarm.getTimeString(context));
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
