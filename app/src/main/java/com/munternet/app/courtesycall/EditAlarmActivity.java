package com.munternet.app.courtesycall;

import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.munternet.app.courtesycall.models.AlarmModel;

import net.danlew.android.joda.DateUtils;

import org.joda.time.DateTime;

public class EditAlarmActivity extends AppCompatActivity {

    private static final String TAG = "EditAlarmActivity";
    private static final boolean DEBUG_EDIT_ALARM_ACTIVITY_LOG = true;

    public static String ALARM_ID_EXTRA = "alarmIdExtra";

    private DatabaseReference databaseUserQuery;

    private AlarmModel alarmModel;

    private EditText alarmLabelText;
    private Button alarmTimeButton;
    private Button alarmDateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_alarm);
        initViews();


        String alarmId = getIntent().getExtras().getString(ALARM_ID_EXTRA,"");

        // NOTE: savedInstanceState is null the first time the activity is started, at orientation change it's !null

        if(DEBUG_EDIT_ALARM_ACTIVITY_LOG) Log.i(TAG, "::onCreate " + alarmId + ", " + savedInstanceState);
        if(!alarmId.isEmpty()) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            databaseUserQuery = database.getReference("alarms").child(alarmId);
            databaseUserQuery.addListenerForSingleValueEvent(databaseValueEventListener());



        }
    }

    @Override
    protected void onPause() {
        if(DEBUG_EDIT_ALARM_ACTIVITY_LOG) Log.i(TAG, "::onPause");

        String alarmLabel = alarmLabelText.getText().toString();
        if(!alarmLabel.isEmpty()) {
            alarmModel.setLabel(alarmLabel);
            databaseUserQuery.setValue(alarmModel);
        }

        super.onPause();
    }

    private ValueEventListener databaseValueEventListener() {
        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                alarmModel = dataSnapshot.getValue(AlarmModel.class);
                if(DEBUG_EDIT_ALARM_ACTIVITY_LOG) Log.i(TAG, "Value is: " + alarmModel.getId());

                populateViews();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        };
    }

    private void initViews() {
        alarmLabelText = (EditText) findViewById(R.id.alarmItemLabelText);
        alarmTimeButton = (Button) findViewById(R.id.alarmItemTimeButton);
        alarmDateButton = (Button) findViewById(R.id.alarmItemDateButton);
    }

    private void populateViews() {
        alarmLabelText.setText(alarmModel.getLabel());

        String date = alarmModel.getDateString(this);
        alarmDateButton.setText(date);

        String time = alarmModel.getTimeString(this);
        alarmTimeButton.setText(time);
        alarmTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });
    }

    private void showTimePickerDialog() {
        boolean is24hClock = true;

        DateTime dateTime = new DateTime(alarmModel.getTimeInMillis());
        //String result = DateUtils.formatDateTime(context, dateTime, DateUtils.FORMAT_SHOW_WEEKDAY | DateUtils.FORMAT_SHOW_DATE |DateUtils.FORMAT_SHOW_TIME);

        int mMinute = dateTime.getMinuteOfHour();
        int mHour = dateTime.getHourOfDay();
        final TimePickerDialog timePickerDialog = new TimePickerDialog(this, null, mHour, mMinute, is24hClock);
        timePickerDialog.show();
    }
}
