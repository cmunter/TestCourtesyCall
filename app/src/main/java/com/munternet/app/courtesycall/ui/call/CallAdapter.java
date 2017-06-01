package com.munternet.app.courtesycall.ui.call;

import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.munternet.app.courtesycall.R;
import com.munternet.app.courtesycall.model.AlarmModel;

import java.util.Arrays;
import java.util.List;

/**
 *
 *
 */
public class CallAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = CallAdapter.class.getName();
    private static final boolean DEBUG = true;

    private static final int HEADER_VIEW_TYPE = 0;
    private static final int CONTENT_VIEW_TYPE = 1;
    private SortedList<AlarmModel> alarmList;
    private final OnCallItemClickListener listener;


    public CallAdapter(OnCallItemClickListener mListener) {
        listener = mListener;
        alarmList = new SortedList<>(AlarmModel.class, new SortedList.Callback<AlarmModel>() {

            @Override
            public int compare(AlarmModel o1, AlarmModel o2) {
                int result;
                if (areItemsTheSame(o1, o2)) {
                    result = 0;
                } else if(o1.getTimeInMillis()==(o2.getTimeInMillis())) {
                    result = 0;
                } else if(o1.getTimeInMillis()>(o2.getTimeInMillis())) {
                    result = 1;
                } else {
                    result = -1;
                }
                if(DEBUG) Log.i(TAG, "::compare result: " + result + ", o1: " + o1.getLabel() + ", Time: " + o1.getTimeInMillis() + ", o2: " + o2.getLabel() + ", Time: " + o2.getTimeInMillis());

                return result;
            }

            @Override
            public void onInserted(int position, int count) {
                if(DEBUG) Log.i(TAG, "::onInserted position: " + position + " count: " + count);
                notifyItemRangeInserted(position+1, count);
            }

            @Override
            public void onRemoved(int position, int count) {
                if(DEBUG) Log.i(TAG, "::onRemoved position: " + position + ", count: " + count);
                notifyItemRangeRemoved(position+1, count);
            }

            @Override
            public void onMoved(int fromPosition, int toPosition) {
                if(DEBUG) Log.i(TAG, "::onMoved fromPosition: " + fromPosition + ", toPosition: " + toPosition);
                notifyItemMoved(fromPosition+1, toPosition+1);
            }

            @Override
            public void onChanged(int position, int count) {
                if(DEBUG) Log.i(TAG, "::onChanged position: " + position + ", count: " + count);

                notifyItemRangeChanged(position+1, count);
            }

            @Override
            public boolean areContentsTheSame(AlarmModel oldItem, AlarmModel newItem) {
                // return whether the items' visual representations are the same or not.

                boolean result = false;
                if(oldItem.getTimeInMillis() == newItem.getTimeInMillis() &&
                        oldItem.getId().contentEquals(newItem.getId()) &&
                        oldItem.getLabel().contentEquals(newItem.getLabel()) &&
                        oldItem.getUserId().contentEquals(newItem.getUserId())
                        ) {
                    result = true;
                }
                if(DEBUG) Log.i(TAG, "::areContentsTheSame: " + result + ",  oldItem: " + oldItem + ", newItem: " + newItem);
                return result;
            }

            @Override
            public boolean areItemsTheSame(AlarmModel item1, AlarmModel item2) {
                boolean result = item1.getId().equalsIgnoreCase(item2.getId());
                if(DEBUG) Log.i(TAG, "::areItemsTheSame: " + result + ", item1: " + item1 + ", item2: " + item2);
                return result;
            }
        });
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
            callHolder.bindHolder(alarm, listener);
            if(DEBUG) Log.i(TAG, "::onBindViewHolder position: " + position + ", alarm: " + alarm);
        }
    }

    @Override
    public int getItemCount() {
        return alarmList.size()+1;
    }

    public AlarmModel get(int position) {
        return alarmList.get(position);
    }

    public int add(AlarmModel item) {
        if(DEBUG) Log.i(TAG, "::add " + item);
        return alarmList.add(item);
    }

    public int indexOf(AlarmModel item) {
        int index = alarmList.indexOf(item);
        if(DEBUG) Log.i(TAG, "::indexOf item: " + item + ", index: " + index);
        return index;
    }

    public void updateItemAt(int index, AlarmModel item) {
        if(DEBUG) Log.i(TAG, "::updateItemAt index: " + index + ", item: " + item);
        alarmList.updateItemAt(index, item);
        alarmList.recalculatePositionOfItemAt(index);
    }

    public void addAll(List<AlarmModel> items) {
        if(DEBUG) Log.i(TAG, "::addAll");
        alarmList.beginBatchedUpdates();
        for (AlarmModel item : items) {
            alarmList.add(item);
        }
        alarmList.endBatchedUpdates();
    }

    public void addAll(AlarmModel[] items) {
        addAll(Arrays.asList(items));
    }

    public boolean remove(AlarmModel item) {
        if(DEBUG) Log.i(TAG, "::remove item: " +item);
        return alarmList.remove(item);
    }

    public AlarmModel removeItemAt(int index) {
        return alarmList.removeItemAt(index);
    }

    public void clear() {
        alarmList.beginBatchedUpdates();
        //remove items at end, to avoid unnecessary array shifting
        while (alarmList.size() > 0) {
            alarmList.removeItemAt(alarmList.size() - 1);
        }
        alarmList.endBatchedUpdates();
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0) return HEADER_VIEW_TYPE;
        else return CONTENT_VIEW_TYPE;
    }
}
