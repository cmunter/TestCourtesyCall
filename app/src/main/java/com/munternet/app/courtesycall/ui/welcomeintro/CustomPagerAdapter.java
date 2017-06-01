package com.munternet.app.courtesycall.ui.welcomeintro;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dragankrstic.autotypetextview.AutoTypeTextView;
import com.munternet.app.courtesycall.R;

public class CustomPagerAdapter extends PagerAdapter {

    private Context mContext;
    public static int WELCOME_VIEW_01 = 0;
    public static int WELCOME_VIEW_02 = 1;
    public static int WELCOME_VIEW_03 = 2;

    private static int PAGES = 3;

    private AutoTypeTextView view01AutoText;

    public CustomPagerAdapter(Context context) {
        mContext = context;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        //CustomPagerEnum customPagerEnum = CustomPagerEnum.values()[position];
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout;

        if(position == WELCOME_VIEW_01) {
            layout = (ViewGroup) inflater.inflate(R.layout.welcome_view_01, collection, false);
            view01AutoText = (AutoTypeTextView) layout.findViewById(R.id.autoTypeText);
        } else if (position == WELCOME_VIEW_02) {
            layout = (ViewGroup) inflater.inflate(R.layout.welcome_view_02, collection, false);
        } else {
            layout = (ViewGroup) inflater.inflate(R.layout.welcome_view_03, collection, false);
        }

        collection.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return PAGES;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public void setView01AutoText(String text) {
        if(view01AutoText!=null) view01AutoText.setTextAutoTyping(text);
    }
}
