package com.munternet.app.courtesycall.views;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by chrtistianmunter on 3/17/17.
 */

public class AlarmItemViewDividerDecoration extends RecyclerView.ItemDecoration {

    private final int bottomBarHeight;

    public AlarmItemViewDividerDecoration(int bottomBarHeight) {
        this.bottomBarHeight = bottomBarHeight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildAdapterPosition(view) == parent.getAdapter().getItemCount() - 1) {
            outRect.bottom = bottomBarHeight;
        }
    }
}
