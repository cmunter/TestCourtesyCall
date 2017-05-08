package com.munternet.app.courtesycall;

import android.os.SystemClock;
import android.support.v7.util.SortedList;
import android.util.Log;

import com.munternet.app.courtesycall.call.CallAdapter;
import com.munternet.app.courtesycall.models.AlarmModel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */


@RunWith(MockitoJUnitRunner.class)
public class ExampleUnitTest {

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void call_adapter_test_order() throws Exception {

//        CallAdapter callAdapter = new CallAdapter();
//
        long currentTime = System.currentTimeMillis();
        String userId = "me";
//
        AlarmModel alarm1 = new AlarmModel("101", "First", currentTime+1000, userId);
        AlarmModel alarm2 = new AlarmModel("102", "Second", currentTime+2000, userId);
        AlarmModel alarm3 = new AlarmModel("103", "Third", currentTime+3000, userId);
        AlarmModel alarm4 = new AlarmModel("104", "Fourth", currentTime+4000, userId);
        AlarmModel alarm5 = new AlarmModel("105", "Fifth", currentTime+5000, userId);
//
//        callAdapter.add(alarm1);
//        callAdapter.add(alarm2);
//        callAdapter.add(alarm3);
//        callAdapter.add(alarm4);
//        callAdapter.add(alarm5);
//
//        assertEquals(alarm2, callAdapter.get(2));



        SortedList<AlarmModel> alarmList;
        alarmList = new SortedList<>(AlarmModel.class, new SortedList.Callback<AlarmModel>() {

            @Override
            public int compare(AlarmModel o1, AlarmModel o2) {
                int result;
                if(o1.getTimeInMillis()==(o2.getTimeInMillis())) {
                    result = 0;
                } else if(o1.getTimeInMillis()>(o2.getTimeInMillis())) {
                    result = 1;
                } else {
                    result = -1;
                }

                return result;
            }

            @Override
            public void onInserted(int position, int count) {
//                if(DEBUG) Log.i(TAG, "::onInserted position: " + position + " count: " + count);
//                notifyItemRangeInserted(position+1, count);
            }

            @Override
            public void onRemoved(int position, int count) {
//                if(DEBUG) Log.i(TAG, "::onRemoved position: " + position + ", count: " + count);
//                notifyItemRangeRemoved(position+1, count);
            }

            @Override
            public void onMoved(int fromPosition, int toPosition) {
//                if(DEBUG) Log.i(TAG, "::onMoved fromPosition: " + fromPosition + ", toPosition: " + toPosition);
//                notifyItemMoved(fromPosition+1, toPosition+1);
            }

            @Override
            public void onChanged(int position, int count) {
//                if(DEBUG) Log.i(TAG, "::onChanged position: " + position + ", count: " + count);
//                notifyItemRangeChanged(position+1, count);
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
//                if(DEBUG) Log.i(TAG, "::areContentsTheSame: " + result + ",  oldItem: " + oldItem + ", newItem: " + newItem);
                return result;
            }

            @Override
            public boolean areItemsTheSame(AlarmModel item1, AlarmModel item2) {
                boolean result = item1.getId().equalsIgnoreCase(item2.getId());
//                if(DEBUG) Log.i(TAG, "::areItemsTheSame: " + result + ", item1: " + item1 + ", item2: " + item2);
                return result;
            }
        });

        alarmList.add(alarm1);
        alarmList.add(alarm2);
        alarmList.add(alarm3);
        alarmList.add(alarm4);

        assertEquals(alarm1, alarmList.get(0));
        assertEquals(alarm2, alarmList.get(1));
        assertEquals(alarm3, alarmList.get(2));
        assertEquals(alarm4, alarmList.get(3));


        alarm4.setTimeInMillis(currentTime+1050);
        alarmList.add(alarm4);

        assertEquals(alarm1, alarmList.get(0));
        assertEquals(alarm4, alarmList.get(1));
        assertEquals(alarm2, alarmList.get(2));
        assertEquals(alarm3, alarmList.get(3));

    }

}