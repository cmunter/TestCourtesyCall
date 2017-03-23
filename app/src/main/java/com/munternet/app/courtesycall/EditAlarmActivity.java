package com.munternet.app.courtesycall;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.munternet.app.courtesycall.models.AlarmModel;

public class EditAlarmActivity extends AppCompatActivity {

    private static final String TAG = "EditAlarmActivity";
    private static final boolean DEBUG_EDIT_ALARM_ACTIVITY_LOG = true;

    public static String ALARM_ID_EXTRA = "alarmIdExtra";

    private DatabaseReference databaseUserQuery;

    private AlarmModel alarmModel;

    private EditText alarmLabelText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_alarm);

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

                alarmLabelText = (EditText) findViewById(R.id.alarmItemLabelText);
                alarmLabelText.setText(alarmModel.getLabel());

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        };
    }
}
