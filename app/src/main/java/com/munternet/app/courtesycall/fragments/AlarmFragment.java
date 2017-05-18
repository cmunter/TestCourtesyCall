package com.munternet.app.courtesycall.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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
import com.munternet.app.courtesycall.EditAlarmActivity;
import com.munternet.app.courtesycall.R;
import com.munternet.app.courtesycall.alarm.AlarmAdapter;
import com.munternet.app.courtesycall.utils.PreferenceUtil;
import com.munternet.app.courtesycall.views.AlarmItemViewDividerDecoration;
import com.munternet.app.courtesycall.models.AlarmModel;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * TODO: if alarm is less than 1 hour, schedule refresh of list item
 */

public class AlarmFragment extends Fragment {

    private static final String TAG = AlarmFragment.class.getSimpleName();
    private static final boolean DEBUG_ALARM_FRAGMENT_LOG = true;

    private FloatingActionButton fab;

    private List<AlarmModel> alarmList = new ArrayList<>();
    private RecyclerView recyclerView;
    private AlarmAdapter alarmAdapter;

    private View emptyView;

    public static AlarmFragment newInstance() {
        if (DEBUG_ALARM_FRAGMENT_LOG) Log.i(TAG, "::newInstance");
        return new AlarmFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_alert, container, false);
        emptyView = rootView.findViewById(R.id.alertEmptyView);

        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), EditAlarmActivity.class);
                startActivity(intent);
            }
        });

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
        int userId = PreferenceUtil.readUserIdPreferences(getActivity());
        Query databaseUserQuery = database.getReference("alarms")
                .orderByChild("userId")
                .equalTo(String.valueOf(userId));

        // TODO verify if this is only called when this user has changes and not other users
        databaseUserQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (DEBUG_ALARM_FRAGMENT_LOG) Log.i(TAG, "::onDataChange");

                long currentTime = DateTime.now().getMillis(); // limit to alarms after this time TODO: is there a problem with time zones?
                alarmList.clear();
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    AlarmModel value = postSnapshot.getValue(AlarmModel.class);
                    if (DEBUG_ALARM_FRAGMENT_LOG) Log.i(TAG, "::onDataChange " + value.getLabel() + ", " + value.getTimeInMillis());
                    if(value.getTimeInMillis()>currentTime) {
                        alarmList.add(value);
                    }
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

                if(!alarmList.isEmpty()) {
                    hideEmptyView();
                } else {
                    hideHeaderView();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void hideHeaderView() {
        View headerView = recyclerView.getChildAt(0);
        if(headerView!=null) headerView.setVisibility(View.GONE);
    }

    private void hideEmptyView() {
        emptyView.setVisibility(View.GONE);
    }

}