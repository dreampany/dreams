package com.dreampany.translate.ui.activity

import android.os.Bundle
import com.dreampany.translate.R
import com.dreampany.frame.ui.activity.BaseActivity
import com.wang.avi.AVLoadingIndicatorView


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
        val loader = findViewById<AVLoadingIndicatorView>(R.id.view_loading)
        loader.smoothToShow()
        ex.postToUi({
            loader.smoothToHide()
            openActivity(NavigationActivity::class.java, true)
            finish()
        }, 2000L)
    }

    override fun onStopUi() {
    }
}