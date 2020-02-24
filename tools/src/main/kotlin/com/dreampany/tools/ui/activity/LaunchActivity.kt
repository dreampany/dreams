package com.dreampany.tools.ui.activity

import android.os.Bundle
import com.dreampany.tools.R
import com.dreampany.framework.ui.activity.BaseActivity
import com.dreampany.tools.misc.Constants
import com.wang.avi.AVLoadingIndicatorView


/**
 * Created by roman on 2019-10-07
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class LaunchActivity : BaseActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_launch
    }

    override fun isPortrait(): Boolean {
        return true
    }

    override fun isFullScreen(): Boolean {
        return true
    }

    override fun getScreen(): String {
        return Constants.launch(this)
    }

    override fun onStartUi(state: Bundle?) {
        val loader = findViewById<AVLoadingIndicatorView>(R.id.view_loading)
        loader.smoothToShow()
        ex.postToUi(Runnable{
            loader.smoothToHide()
            openActivity(NavigationActivity::class.java, true)
            finish()
        }, 2000L)
    }

    override fun onStopUi() {
    }
}