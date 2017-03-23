package com.munternet.app.courtesycall.models;

import android.content.Context;

import net.danlew.android.joda.DateUtils;

import org.joda.time.DateTime;

/**
 * Firebase model for the alarm
 */

public class AlarmModel {

    private String id;
    private String label;
    private long timeInMillis;
    private String asigneeUserId = "";
    private String userId = "";

    // TODO remove this from the firebase model, only activated alarms should be synced to firebase
    private boolean activated = false;

    // Default constructor required for firebase
    public AlarmModel() { }

    public AlarmModel(String mId, String mLabel, long mTimeInMillis, String mUserId) {
        id = mId;
        label = mLabel;
        timeInMillis = mTimeInMillis;
        userId = mUserId;
        activated = true;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public long getTimeInMillis() {
        return timeInMillis;
    }

    public void setTimeInMillis(long timeInMillis) {
        this.timeInMillis = timeInMillis;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAsigneeUserId() {
        return asigneeUserId;
    }

    public void setAsigneeUserId(String asigneeUserId) {
        this.asigneeUserId = asigneeUserId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRelativeTimeString(Context context) {
        DateTime dateTime = new DateTime(timeInMillis);
        CharSequence timeChar = DateUtils.getRelativeTimeSpanString(context, dateTime);
        return timeChar.toString();
    }

    public String getTimeAndDateString(Context context) {
        DateTime dateTime = new DateTime(timeInMillis);
        String result = DateUtils.formatDateTime(context, dateTime, DateUtils.FORMAT_SHOW_WEEKDAY | DateUtils.FORMAT_SHOW_DATE |DateUtils.FORMAT_SHOW_TIME);
        return result;
    }


    public String getDateString(Context context) {
        DateTime dateTime = new DateTime(timeInMillis);
        String result = DateUtils.formatDateTime(context, dateTime, DateUtils.FORMAT_SHOW_DATE);
        return result;
    }

    public String getTimeString(Context context) {
        DateTime dateTime = new DateTime(timeInMillis);
        String result = DateUtils.formatDateTime(context, dateTime, DateUtils.FORMAT_SHOW_TIME);
        return result;
    }

}
