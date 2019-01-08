package com.dreampany.word.ui.activity

import android.os.Bundle
import com.dreampany.frame.ui.activity.BaseActivity
import com.dreampany.frame.util.AndroidUtil
import com.dreampany.word.R
import com.dreampany.word.data.source.pref.LoadPref
import com.wang.avi.AVLoadingIndicatorView
import javax.inject.Inject


/**
 * Created by Hawladar Roman on 5/22/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
class LaunchActivity : BaseActivity() {

    @Inject
    lateinit var pref : LoadPref

    override fun getLayoutId(): Int {
        return R.layout.activity_launch
    }

    override fun isFullScreen(): Boolean {
        return true
    }

    override fun onStartUi(state: Bundle?) {
        val loader = findViewById<AVLoadingIndicatorView>(R.id.view_loading)
        loader.smoothToShow()
        AndroidUtil.getUiHandler().postDelayed({
            loader.smoothToHide()
            openActivity(NavigationActivity::class.java)
            finish()
        }, 2000L)
    }

    override fun onStopUi() {
    }

/*    private fun getTargetClazz(): Class<*> {
        if (pref.isCommonLoaded) {
            return NavigationActivity::class.java
        }
        return LoaderActivity::class.java
    }*/

}