package com.munternet.app.courtesycall.fragments;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.munternet.app.courtesycall.CallReceiver;
import com.munternet.app.courtesycall.CallService;
import com.munternet.app.courtesycall.EditAlarmActivity;
import com.munternet.app.courtesycall.IncommingCallService;
import com.munternet.app.courtesycall.MainActivity;
import com.munternet.app.courtesycall.R;
import com.munternet.app.courtesycall.alarm.AlarmAdapter;
import com.munternet.app.courtesycall.utils.PreferenceUtil;
import com.munternet.app.courtesycall.views.AlarmItemViewDividerDecoration;
import com.munternet.app.courtesycall.models.AlarmModel;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.List;

import static com.munternet.app.courtesycall.EditAlarmActivity.ALARM_ID_EXTRA;

/**
 * Created by chrtistianmunter on 3/16/17.
 */

public class AlarmFragment extends Fragment {

    private static final String TAG = AlarmFragment.class.getSimpleName();
    private static final boolean DEBUG_ALARM_FRAGMENT_LOG = true;

    private FloatingActionButton fab;

    private IncommingCallService incommingCallService;

    private List<AlarmModel> alarmList = new ArrayList<>();
    private RecyclerView recyclerView;
    private AlarmAdapter alarmAdapter;

    public static AlarmFragment newInstance() {
        if (DEBUG_ALARM_FRAGMENT_LOG) Log.d(TAG, "::newInstance");
        return new AlarmFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_alert, container, false);

        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), EditAlarmActivity.class);
                startActivity(intent);

                // showMissedAlarmNotification();
                // startCallService();
                //setTestAlarm1MinFromNow();
            }
        });

        // incommingCallService = new IncommingCallService();

        alarmAdapter = new AlarmAdapter(alarmList);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(alarmAdapter);

        int bottomBarHeight = getResources().getDimensionPixelSize(R.dimen.alarm_list_bottom_padding);
        recyclerView.addItemDecoration(new AlarmItemViewDividerDecoration(bottomBarHeight));

        prepareAlarmData();

        return rootView;
    }

    private void prepareAlarmData() {
        if (DEBUG_ALARM_FRAGMENT_LOG) Log.i(TAG, "::prepareAlarmData");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        int userId = PreferenceUtil.readAccountPreferences(getActivity());
        Query databaseUserQuery = database.getReference("alarms")
                .orderByChild("userId")
                .equalTo(String.valueOf(userId));

        // TODO verify if this is only called when this user has changes and not other users
        databaseUserQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (DEBUG_ALARM_FRAGMENT_LOG) Log.i(TAG, "::onDataChange");

                alarmList.clear();
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    AlarmModel value = postSnapshot.getValue(AlarmModel.class);
                    if (DEBUG_ALARM_FRAGMENT_LOG) Log.i(TAG, "::onDataChange " + value.getLabel() + ", " + value.getTimeInMillis());
                    alarmList.add(value);
                }

                Collections.sort(alarmList, new Comparator<AlarmModel>() {
                    @Override
                    public int compare(AlarmModel o1, AlarmModel o2) {
                        if(o1.getTimeInMillis()>(o2.getTimeInMillis())) {
                            return 1;
                        } else {
                            return -1;
                        }

                    }
                });
                alarmAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void startCallService() {
        Snackbar.make(fab, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable(){
            @Override
            public void run() {
                Log.i("Service", "::run()");
                //incommingCallService.startActionFoo(MainActivity.this, null, null);
                Intent intent = new Intent(getActivity(), CallService.class);
                getActivity().startService(intent);
            }
        }, 10000);
    }

    private void showMissedAlarmNotification() {
        int MISSED_ALARM_NOTIFICATION_ID = 100;
        int MISSED_ALARM_REQUEST_CODE = 100;

        final Intent openMainActivityIntent = new Intent(getActivity(), MainActivity.class);
        PendingIntent mainActivityIntent = PendingIntent.getActivity(getActivity(), MISSED_ALARM_REQUEST_CODE, openMainActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getActivity())
                .setSmallIcon(R.drawable.ic_bell_outline_24dp)
                .setContentTitle("Missed call")
                .setContentText(getString(R.string.app_name))
                .setAutoCancel(true)
                .setContentIntent(mainActivityIntent);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            mBuilder.setColor(getActivity().getColor(R.color.colorAccent));
        }

        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(MISSED_ALARM_NOTIFICATION_ID, mBuilder.build());
    }


    private void setTestAlarm1MinFromNow() {

        Context context = getActivity();
        AlarmManager mAlarmManager = (AlarmManager)context.getSystemService(context.ALARM_SERVICE);
        //long mDate = System.currentTimeMillis() + (1000*1);
        long mDate = System.currentTimeMillis() + (1000*60*2);


        Intent intent = new Intent(context, CallReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mAlarmManager.set(AlarmManager.RTC_WAKEUP, mDate, sender);
        Log.i("MUNTER", "::setTestAlarm1MinFromNow()");

        Snackbar.make(fab, "Test call 2 mins from now", Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }

}
