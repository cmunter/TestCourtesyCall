package com.munternet.app.courtesycall.ui.welcomeintro;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.munternet.app.courtesycall.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Viewpager showing the on-boarding (welcome intro)
 */
public class WelcomeActivity extends AppCompatActivity {

    private static final boolean DEBUG = true;
    private static final String TAG = WelcomeActivity.class.getSimpleName();

    @BindView(R.id.nextFab)
    FloatingActionButton nextButton;

    @BindView(R.id.infoText)
    TextView infoText;

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @BindView(R.id.viewPagerDots)
    TabLayout viewPagerDots;

    private CustomPagerAdapter pagerAdapter;

    private Timer autoTextTimer;
    private final long AUTO_TEXT_DELAY = 6000;
    private List<String> autoTextStrings = new ArrayList<>();
    private int autoTextStringsIndex = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);

        infoText.setText("Created");

        initViewPager();

        startView01Animation();
    }

    private void startView01Animation() {
        autoTextStringsIndex = 0;
        autoTextStrings.clear();
        autoTextStrings.add(getString(R.string.welcome_view_01_suggestion_books));
        autoTextStrings.add(getString(R.string.welcome_view_01_suggestion_language));
        autoTextStrings.add(getString(R.string.welcome_view_01_suggestion_something_else));

        autoTextTimer = new Timer();
        scheduleAutoTextTimer();
    }

    private void scheduleAutoTextTimer() {
        autoTextTimer.schedule(new TimerTask() {

            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                                  @Override
                                  public void run() {
                                      if(DEBUG) Log.i(TAG, "::scheduleAutoTextTimer " + autoTextStringsIndex + ", " + autoTextStrings.get(autoTextStringsIndex));

                                      pagerAdapter.setView01AutoText(autoTextStrings.get(autoTextStringsIndex));

                                      autoTextStringsIndex++;
                                      if(autoTextStrings.size()>autoTextStringsIndex) {
                                          scheduleAutoTextTimer();
                                      } else {
                                          autoTextTimer.cancel();
                                      }
                                  }
                              });
            }
        }, AUTO_TEXT_DELAY);
    }

    private void cancelAutoTextTimer() {
        if(autoTextTimer!=null) autoTextTimer.cancel();
    }

    private int previousPagePosition = 0;

    private void initViewPager() {
        pagerAdapter = new CustomPagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(DEBUG) Log.i(TAG, "::onPageScrolled " + position);
            }

            @Override
            public void onPageSelected(int position) {
                if(DEBUG) Log.i(TAG, "::onPageSelected " + position + ", size: " + viewPager.getAdapter().getCount());

                if(position!=CustomPagerAdapter.WELCOME_VIEW_01) {
                    cancelAutoTextTimer();
                }

                if(isFinalPosition(position)) {
                    if(DEBUG) Log.i(TAG, "::onPageSelected Last page");
                    // Scrolled to Last page
                    nextButton.setImageResource(R.drawable.ic_close_24dp);
                    nextButton.animate().rotationBy(360f).start();
                } else if (is2ndToFinalPosition(position) && isFinalPosition(previousPagePosition)) {
                    // Scrolled back from Last page
                    if(DEBUG) Log.i(TAG, "::onPageSelected scrolled back from Last page");

                    nextButton.setImageResource(R.drawable.ic_arrow_forward_24dp);
                    nextButton.animate().rotationBy(360f).start();
                }

                previousPagePosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if(DEBUG) Log.i(TAG, "::onPageScrollStateChanged " + state);
            }
        });

        viewPagerDots.setupWithViewPager(viewPager, true);
    }

    private boolean isFinalPosition(int position) {
        return position==viewPager.getAdapter().getCount()-1;
    }

    private boolean is2ndToFinalPosition(int position) {
        return position==viewPager.getAdapter().getCount()-2;
    }

    @OnClick(R.id.nextFab)
    public void clickNext() {
        if(DEBUG) Log.i(TAG, "::clickNext");
        infoText.setText("Next");

        int currentIndex = viewPager.getCurrentItem();
        if(currentIndex<viewPager.getAdapter().getCount()-1)  {
            if(DEBUG) Log.i(TAG, "::clickNext " + currentIndex + ", " + viewPager.getChildCount());
            viewPager.setCurrentItem(currentIndex+1);
        }
        else {
            if(DEBUG) Log.i(TAG, "::clickNext finish");
            finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        cancelAutoTextTimer();
    }
}
