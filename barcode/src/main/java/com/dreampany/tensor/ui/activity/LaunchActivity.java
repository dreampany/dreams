package com.dreampany.tensor.ui.activity;

import android.os.Bundle;
import android.widget.RelativeLayout;

import com.dreampany.frame.ui.activity.BaseActivity;
import com.dreampany.frame.util.AndroidUtil;
import com.dreampany.tensor.R;
import com.dynamitechetan.flowinggradient.FlowingGradientClass;

import io.supercharge.shimmerlayout.ShimmerLayout;

public class LaunchActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_launch;
    }

    @Override
    protected boolean isFullScreen() {
        return true;
    }

    @Override
    protected void onStartUi(Bundle state) {
        RelativeLayout layout = findViewById(R.id.layout);
        final ShimmerLayout shimmer = findViewById(R.id.shimmer);
        FlowingGradientClass grad = new FlowingGradientClass();
        grad.setBackgroundResource(R.drawable.translate)
                .onRelativeLayout(layout)
                .setTransitionDuration(2000)
                .start();
        shimmer.startShimmerAnimation();
        AndroidUtil.getUiHandler().postDelayed(() -> {
            shimmer.stopShimmerAnimation();
            openActivity(NavigationActivity.class);
            finish();
        }, 2000L);
    }

    @Override
    protected void onStopUi() {

    }
}
