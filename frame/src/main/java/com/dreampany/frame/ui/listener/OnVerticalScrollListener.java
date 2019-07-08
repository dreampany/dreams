package com.dreampany.frame.ui.listener;

import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Hawladar Roman on 7/13/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class OnVerticalScrollListener extends RecyclerView.OnScrollListener {

    private int delay = 3000;
    private boolean scrolling;
    private boolean scrollingCallAtEnd;
    private Handler handler;
    private Runnable runner = this::onScrollingAtEnd;

    public OnVerticalScrollListener() {

    }

    public OnVerticalScrollListener(boolean scrollingCallAtEnd) {
        this.scrollingCallAtEnd = scrollingCallAtEnd;
        handler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            scrolling = false;
            onIdle();
        } else if (newState == RecyclerView.SCROLL_STATE_SETTLING) {
            scrolling = true;
            onScrolling();
            if (scrollingCallAtEnd) {
                handler.removeCallbacks(runner);
                handler.postDelayed(runner, delay);
            }
        }
    }

    @Override
    public final void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (!recyclerView.canScrollVertically(-1)) {
            onScrolledToTop();
        } else if (!recyclerView.canScrollVertically(1)) {
            onScrolledToBottom();
        }
        if (dy < 0) {
            onScrolledUp(dy);
        } else if (dy > 0) {
            onScrolledDown(dy);
        }
    }

    public void onScrolledUp(int dy) {
        onScrolledUp();
    }

    public void onScrolledDown(int dy) {
        onScrolledDown();
    }

    public boolean isScrolling() {
        return scrolling;
    }

    public boolean isIdle() {
        return !scrolling;
    }

    public void onIdle() {
    }

    public void onScrolling() {
    }

    public void onScrollingAtEnd() {
    }

    public void onScrolledUp() {
    }

    public void onScrolledDown() {
    }

    public void onScrolledToTop() {
    }

    public void onScrolledToBottom() {
    }
}
