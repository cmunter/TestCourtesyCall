package com.munternet.app.courtesycall.ui.call;

import com.munternet.app.courtesycall.model.AlarmModel;

/**
 * Listener for clicking the call card
 */
public interface OnCallItemClickListener {
    void onItemClick(AlarmModel item, boolean isChecked);
}
