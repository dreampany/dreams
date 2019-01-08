package com.dreampany.frame.ui.listener;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Hawladar Roman on 7/13/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class OnVerticalScrollListener extends RecyclerView.OnScrollListener {

    private boolean scrolling;

    @Override
    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            scrolling = false;
            onIdle();
        } else if (newState == RecyclerView.SCROLL_STATE_SETTLING) {
            scrolling = true;
            onScrolling();
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

    public void onScrolledUp() {
    }

    public void onScrolledDown() {
    }

    public void onScrolledToTop() {
    }

    public void onScrolledToBottom() {
    }
}
