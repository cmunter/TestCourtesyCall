package com.munternet.app.courtesycall;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.munternet.app.courtesycall.models.AlarmModel;
import com.munternet.app.courtesycall.utils.PreferenceUtil;

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

    private String alarmId = "";

    private boolean isDeletingAlarm = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_alarm);
        initViews();

        if(getIntent()!=null && getIntent().getExtras()!=null) {
            alarmId = getIntent().getExtras().getString(ALARM_ID_EXTRA,"");
        }

        // NOTE: savedInstanceState is null the first time the activity is started, at orientation change it's !null

        if(DEBUG_EDIT_ALARM_ACTIVITY_LOG) Log.i(TAG, "::onCreate " + alarmId + ", " + savedInstanceState);
        if(!alarmId.isEmpty()) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            databaseUserQuery = database.getReference("alarms").child(alarmId);
            databaseUserQuery.addListenerForSingleValueEvent(databaseValueEventListener());
        } else {
            DateTime now = DateTime.now();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference databaseRef = database.getReference("alarms");
            String key = databaseRef.push().getKey();
            int userId = PreferenceUtil.readUserIdPreferences(EditAlarmActivity.this);
            alarmModel = new AlarmModel(key, "", now.getMillis(), String.valueOf(userId));
            databaseUserQuery = databaseRef.child(key);
            databaseUserQuery.setValue(alarmModel);

            populateViews();

            showKeyboard();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        isDeletingAlarm = false;
    }

    @Override
    protected void onPause() {
        if(DEBUG_EDIT_ALARM_ACTIVITY_LOG) Log.i(TAG, "::onPause");

        String alarmLabel = alarmLabelText.getText().toString();
        if(!isDeletingAlarm &&!alarmLabel.isEmpty()) {
            alarmModel.setLabel(alarmLabel);
            databaseUserQuery.setValue(alarmModel);
        }

        // TODO: maybe move save to onDestroy(/)
        // TODO: handle that new alarm will created when the user just opens and then closes the activity. maybe check that editText.isEmpty and then delete from firebase

        super.onPause();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        if(!alarmId.isEmpty()) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.edit_alarm_menu, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.deleteAlarm:
                databaseUserQuery.removeValue();
                isDeletingAlarm = true;
                finish();
                return true;
        }
        return false;
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
        alarmLabelText.setSelection(alarmLabelText.length());

        String date = alarmModel.getDateString(this);
        alarmDateButton.setText(date);
        alarmDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

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

        final DateTime dateTime = new DateTime(alarmModel.getTimeInMillis());

        final int mMinute = dateTime.getMinuteOfHour();
        final int mHour = dateTime.getHourOfDay();
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                DateTime newDateTime = dateTime.plusHours(selectedHour-mHour).plusMinutes(selectedMinute-mMinute);
                alarmModel.setTimeInMillis(newDateTime.getMillis());
                String time = alarmModel.getTimeString(EditAlarmActivity.this);
                alarmTimeButton.setText(time);
            }
        };

        final TimePickerDialog timePickerDialog = new TimePickerDialog(this, timeSetListener, mHour, mMinute, is24hClock);
        timePickerDialog.show();
    }

    private void showDatePickerDialog() {

        final DateTime dateTime = new DateTime(alarmModel.getTimeInMillis());
        final int mYear = dateTime.getYear();
        final int mMonth = dateTime.getMonthOfYear();
        final int mDay = dateTime.getDayOfMonth();

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                Log.i(TAG, "onDateSet DATE current month: " + mMonth + ", selected: " + month);

                DateTime newDateTime = dateTime.plusYears(year-mYear).plusMonths((month+1)-mMonth).plusDays(dayOfMonth-mDay);
                alarmModel.setTimeInMillis(newDateTime.getMillis());
                String date = alarmModel.getDateString(EditAlarmActivity.this);
                alarmDateButton.setText(date);
            }
        };
        final DatePickerDialog datePickerDialog = new DatePickerDialog(this, dateSetListener, mYear, mMonth-1, mDay);
        datePickerDialog.show();
    }

    private void showKeyboard() {
        // NOTE: It needs to be delayed because this can be called from onCreate()
        (new Handler()).postDelayed(new Runnable() {
            public void run() {
                alarmLabelText.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.showSoftInput(alarmLabelText, InputMethodManager.SHOW_IMPLICIT);
            }
        }, 200);
    }
}
