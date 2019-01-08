package com.dreampany.share.ui.activity

import android.os.Bundle
import android.widget.RelativeLayout
import com.dreampany.frame.ui.activity.BaseActivity
import com.dreampany.frame.util.AndroidUtil
import com.dreampany.share.R
import com.dynamitechetan.flowinggradient.FlowingGradientClass
import io.supercharge.shimmerlayout.ShimmerLayout


/**
 * Created by Hawladar Roman on 5/22/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
class LaunchActivity : BaseActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_launch
    }

    override fun isFullScreen(): Boolean {
        return true
    }

    override fun onStartUi(state: Bundle?) {
        val layout = findViewById<RelativeLayout>(R.id.layout)
        val shimmer = findViewById<ShimmerLayout>(R.id.shimmer)
        val grad = FlowingGradientClass()
        grad.setBackgroundResource(R.drawable.translate)
                .onRelativeLayout(layout)
                .setTransitionDuration(2000)
                .start()
        shimmer.startShimmerAnimation()
        AndroidUtil.getUiHandler().postDelayed({
            shimmer.stopShimmerAnimation()
            openActivity(NavigationActivity::class.java)
            finish()
        }, 2000L)
    }

    override fun onStopUi() {
    }
}