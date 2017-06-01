package com.munternet.app.courtesycall.ui.fragments;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.util.SortedList;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.munternet.app.courtesycall.app.calling.PerformOutgoingCallReceiver;
import com.munternet.app.courtesycall.R;
import com.munternet.app.courtesycall.ui.call.CallAdapter;
import com.munternet.app.courtesycall.ui.call.OnCallItemClickListener;
import com.munternet.app.courtesycall.app.constants.CallIntentExtrasConstants;
import com.munternet.app.courtesycall.oldcode.Temp;
import com.munternet.app.courtesycall.app.utils.PreferenceUtil;
import com.munternet.app.courtesycall.model.AlarmModel;
import com.munternet.app.courtesycall.ui.views.CallItemViewDividerDecoration;

import org.joda.time.DateTime;


/**
 *
 */
public class CallFragment extends Fragment {

    private static final String TAG = CallFragment.class.getSimpleName();
    private static final boolean DEBUG = true;

    private RecyclerView recyclerView;
    private CallAdapter callAdapter;
    private View emptyView;

    // private DatabaseReference databaseReference;
    private Query databaseQuery;
    private ValueEventListener databaseEventListener;
    private ChildEventListener childEventListener;

    private OnCallItemClickListener itemClickListener;

    private int userId = -1;

    public static CallFragment newInstance() {
        if (DEBUG) Log.i(TAG, "::newInstance");
        return new CallFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_call, container, false);

        itemClickListener = new OnCallItemClickListener() {
            @Override
            public void onItemClick(AlarmModel item, boolean isChecked) {
                if (DEBUG) Log.i(TAG, "::onItemClick " + item + ", " + isChecked);
                if(isChecked) {
                    setAlarmManagerAlarm(item);
                    updateFirebaseAlarmEntry(item, String.valueOf(userId));
                } else {
                    removeAlarmManagerAlarm(item);
                    updateFirebaseAlarmEntry(item, "");
                }
            }
        };
        callAdapter = new CallAdapter(itemClickListener);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(callAdapter);

        Drawable dividerDrawable = ContextCompat.getDrawable(getActivity(), R.drawable.list_divider);
        RecyclerView.ItemDecoration dividerItemDecoration = new CallItemViewDividerDecoration(dividerDrawable);
        recyclerView.addItemDecoration(dividerItemDecoration);

        emptyView = rootView.findViewById(R.id.callEmptyView);
        ImageButton overflowButton = (ImageButton) rootView.findViewById(R.id.overflowButton);
        overflowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Temp.testCallToUser1004(getActivity());
                //((MainActivity)getActivity()).stopSinchCallClient();
            }
        });

        userId = PreferenceUtil.readUserIdPreferences(getActivity());

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        databaseQuery = database.getReference("alarms")
                .orderByChild("timeInMillis")
                .startAt(DateTime.now().getMillis())    // limit to alarms after this time TODO: is there a problem with time zones?
                .limitToFirst(30);

        databaseEventListener = databaseValueEventListener();
        childEventListener = childEventListener();

        databaseListenForChanges();

        return rootView;
    }

    private void databaseListenForChanges() {
//        databaseQuery.addValueEventListener(databaseEventListener);
        databaseQuery.addChildEventListener(childEventListener);
    }

    private ValueEventListener databaseValueEventListener() {
        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        };
    }

    private ChildEventListener childEventListener() {
        return new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                AlarmModel value = dataSnapshot.getValue(AlarmModel.class);
                if (DEBUG) Log.i(TAG, "::onChildAdded: " + value);
                int alarmUserId = -1;
                try {
                    alarmUserId = Integer.parseInt(value.getUserId());
                } catch (NumberFormatException e) {
                    // Ignore for now
                }
                // Do not show your own alarms
                if(alarmUserId!=userId && !isAssignedToOtherUser(value)) {

                    //Check if another user already has been assigned to the alarm
                    if(value.getAsigneeUserId().isEmpty() || value.getAsigneeUserId().contentEquals(String.valueOf(userId))) {
                        callAdapter.add(value);
                        hideEmptyView();
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                AlarmModel value = dataSnapshot.getValue(AlarmModel.class);
                if (DEBUG) Log.i(TAG, "::onChildChanged: " + value);

                int alarmUserId = -1;
                try {
                    alarmUserId = Integer.parseInt(value.getUserId());
                } catch (NumberFormatException e) {
                    // Ignore for now
                }

                // Do not show your own alarms
                if(alarmUserId==userId) {
                    if (DEBUG) Log.i(TAG, "::onChildChanged: " + value + ", remove: own alarm");
                    callAdapter.remove(value);
                }
                // Remove alarms other users has assigned
                else if (isAssignedToOtherUser(value)) {
                    if (DEBUG) Log.i(TAG, "::onChildChanged: " + value + ", remove: assigned to other user");
                    callAdapter.remove(value);
                }
                // Time, label or assignedUser has changed
                else {
                    int index = callAdapter.indexOf(value);
                    if (DEBUG) Log.i(TAG, "::onChildChanged: " + value + ", index: " + index + ", string: " + s);

                    if(index == SortedList.INVALID_POSITION) {
                        callAdapter.add(value);
                    }

                    // TODO Missing use cases if alarm times are changed
                    // maybe add alarm here
                    // if(index>-1)callAdapter.updateItemAt(index, value);
                }
            }

            /*
             * Alarms that are assigned to other users shouldn't be shown
             * Are this user or no user assigned to this alarm.
             */
            private boolean isAssignedToOtherUser(AlarmModel value) {
                if(value.getAsigneeUserId().isEmpty()) {
                    return false;
                } else {
                    int assignedUserId = -1;
                    try {
                        assignedUserId = Integer.parseInt(value.getAsigneeUserId());
                    } catch (NumberFormatException e) {
                        // Ignore for now
                        return false;
                    }
                    if(assignedUserId==userId)
                        return false;
                    else
                        return true;
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                AlarmModel value = dataSnapshot.getValue(AlarmModel.class);
                if (DEBUG) Log.i(TAG, "::onChildRemoved: " + value);
                callAdapter.remove(value);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                AlarmModel value = dataSnapshot.getValue(AlarmModel.class);
                int index = callAdapter.indexOf(value);
                if (DEBUG) Log.i(TAG, "::onChildMoved: " + value + ", Index: " + index + ", string: " + s);

                if(index>=0)callAdapter.updateItemAt(index, value);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (DEBUG) Log.i(TAG, "::onCancelled: " + databaseError.getMessage());
            }
        };
    }

    private void hideEmptyView() {
        emptyView.setVisibility(View.GONE);
    }

    /**
     * Will handle concurrency operations where several user tries to "take" the same alarm.
     * Use case: another user has created an alarm and this user whants to call the other user at the alarm time
     *
     *
     * See "Save data as transactions" https://firebase.google.com/docs/database/android/read-and-write
     *
     * @param childId the node of the selected alarm
     */
    private void takeAlarm(final String childId) {
        databaseQuery.getRef().runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                AlarmModel alarmModel = mutableData.child(childId).getValue(AlarmModel.class);
                if(alarmModel==null) {
                    // TODO how should this be handled in UI?

                    return Transaction.success(mutableData);
                }

                /*
                // Eksample code for assigning this user to the alarm
                else if(alarmModel.handledByUser.isEmpty) {
                    alarmModel.handledByUser = getUid();
                }
                mutableData.child(childId).setValue(alarmModel);
                return Transaction.success(mutableData);

                */
                return null;
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                if (DEBUG) Log.i(TAG, "::onComplete:" + databaseError);
            }
        });
    }

    private void setAlarmManagerAlarm(AlarmModel alarmModel) {
        Log.i("MUNTER", "::setAlarmManagerAlarm() " + alarmModel);

        Context context = getActivity();
        AlarmManager mAlarmManager = (AlarmManager)context.getSystemService(Activity.ALARM_SERVICE);
        long mDate = alarmModel.getTimeInMillis();
        mAlarmManager.set(AlarmManager.RTC_WAKEUP, mDate, createAlarmIntent(alarmModel));
    }

    private void removeAlarmManagerAlarm(AlarmModel alarmModel) {
        Context context = getActivity();
        AlarmManager mAlarmManager = (AlarmManager)context.getSystemService(Activity.ALARM_SERVICE);
        mAlarmManager.cancel(createAlarmIntent(alarmModel));
    }

    private void updateFirebaseAlarmEntry(AlarmModel alarmModel, String assignedUserId) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference("alarms").child(alarmModel.getId()).child("asigneeUserId").setValue(assignedUserId);
    }

    private PendingIntent createAlarmIntent(AlarmModel alarmModel) {
        Context context = getActivity();
        int userId = Integer.parseInt(alarmModel.getUserId());
        Intent intent = new Intent(context, PerformOutgoingCallReceiver.class);

        // TODO test that it works by indentifying a pendingIntent by setting the action. Use-case: assign alarm and un-assign the alarm
        intent.setAction(alarmModel.getId());
        intent.putExtra(CallIntentExtrasConstants.USER_ID, userId);
        intent.putExtra(CallIntentExtrasConstants.ALARM_LABEL, alarmModel.getLabel());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1000, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }

    @Override
    public void onPause() {
        databaseQuery.removeEventListener(databaseEventListener);
        databaseQuery.removeEventListener(childEventListener);
        super.onPause();
    }
}
