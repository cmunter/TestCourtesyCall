package com.munternet.app.courtesycall.call;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.munternet.app.courtesycall.R;
import com.munternet.app.courtesycall.utils.PreferenceUtil;

/**
 * Created by chrtistianmunter on 3/16/17.
 */

public class CallHeaderHolder extends RecyclerView.ViewHolder {

    private static final String TAG = "CallHeaderHolder";
    private static final boolean DEBUG_VIEWS_LOG = true;

    public CallHeaderHolder(final View itemView) {
        super(itemView);
        if(DEBUG_VIEWS_LOG) Log.i(TAG, "::CallHeaderHolder " + getAdapterPosition());

        final CardView cardView = (CardView) itemView.findViewById(R.id.callTipCard);
        Boolean isHeaderTipShown = PreferenceUtil.isCallHeaderTipShown(itemView.getContext());
        if(!isHeaderTipShown) {
            cardView.setVisibility(View.VISIBLE);
            Button tipButton = (Button) itemView.findViewById(R.id.callHeaderTipButton);
            tipButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PreferenceUtil.saveCallHeaderTipShown(itemView.getContext());
                    cardView.setVisibility(View.GONE);
                }
            });
        }
    }

    public void bindHolder() {
        if(DEBUG_VIEWS_LOG) Log.i(TAG, "::CallHeaderHolder bindHolder ");
    }

}
