package com.munternet.app.courtesycall.call;

import com.munternet.app.courtesycall.models.AlarmModel;

/**
 * Listener for clicking the call card
 */
public interface OnCallItemClickListener {
    void onItemClick(AlarmModel item, boolean isChecked);
}
