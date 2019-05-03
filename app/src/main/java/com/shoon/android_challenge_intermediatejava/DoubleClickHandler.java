package com.shoon.android_challenge_intermediatejava;

import android.os.Handler;
import android.view.View;

public class DoubleClickHandler implements View.OnClickListener {

  private long DOUBLE_CLICK_INTERVAL;
  private final Handler mHandler = new Handler();

  private final DoubleClickListener doubleClickListener;

  private int clicks;

  private boolean isBusy = false;



  public DoubleClickHandler(final DoubleClickListener doubleClickListener) {
    this(doubleClickListener, 200L);
  }

  public DoubleClickHandler(final DoubleClickListener doubleClickListener, final long DOUBLE_CLICK_INTERVAL) {
    this.doubleClickListener = doubleClickListener;
    this.DOUBLE_CLICK_INTERVAL = DOUBLE_CLICK_INTERVAL;
  }

  public long getDOUBLE_CLICK_INTERVAL() {
    return DOUBLE_CLICK_INTERVAL;
  }

  public void setDOUBLE_CLICK_INTERVAL(long DOUBLE_CLICK_INTERVAL) {
    this.DOUBLE_CLICK_INTERVAL = DOUBLE_CLICK_INTERVAL;
  }

  @Override
  public void onClick(final View view) {

    if (!isBusy) {
      isBusy = true;
      clicks++;



      mHandler.postDelayed(new Runnable() {
        public final void run() {
 //         DOUBLE_CLICK_INTERVAL=doubleClickListener.setDoubleClickInterval();

          if (clicks >= 2) doubleClickListener.onDoubleClick(view);
          if (clicks == 1) doubleClickListener.onSingleClick(view);

          clicks = 0;
        }
      }, DOUBLE_CLICK_INTERVAL);
      isBusy = false;
    }

  }
}
