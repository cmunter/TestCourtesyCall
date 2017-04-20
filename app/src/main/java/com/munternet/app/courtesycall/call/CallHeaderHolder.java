package com.munternet.app.courtesycall.call;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

/**
 * Created by chrtistianmunter on 3/16/17.
 */

public class CallHeaderHolder extends RecyclerView.ViewHolder {

    private static final String TAG = "CallHeaderHolder";
    private static final boolean DEBUG_VIEWS_LOG = true;

    public CallHeaderHolder(View itemView) {
        super(itemView);
        if(DEBUG_VIEWS_LOG) Log.i(TAG, "::CallHeaderHolder " + getAdapterPosition());

    }

    public void bindHolder() {
        if(DEBUG_VIEWS_LOG) Log.i(TAG, "::CallHeaderHolder bindHolder ");
    }

}
