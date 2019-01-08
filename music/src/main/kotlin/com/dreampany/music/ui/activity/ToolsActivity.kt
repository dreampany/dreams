package com.dreampany.music.ui.activity

import android.os.Bundle
import com.dreampany.frame.ui.activity.BaseActivity
import com.dreampany.music.R
import com.dreampany.music.ui.model.UiTask

/**
 * Created by Hawladar Roman on 5/24/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
class ToolsActivity : BaseActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_tools
    }

    override fun isFullScreen(): Boolean {
        val uiTask = getCurrentTask<UiTask<*>>(true)
        return uiTask?.isFullscreen ?: super.isFullScreen()
    }

    override fun onStartUi(state: Bundle?) {
        val uiTask = getCurrentTask<UiTask<*>>(false) ?: return
        val type = uiTask.type
        val subtype = uiTask.subtype
        if (type == null || subtype == null) {
            return
        }

/*        when (type) {
            UiType.COIN -> {
                when (subtype) {
                    UiSubtype.VIEW -> {
                        commitFragment(CoinFragment::class.java, coinFragmentProvider, R.id.layout, uiTask)
                    }
                }
            }
            UiType.MORE -> {
                when (subtype) {
                    UiSubtype.ABOUT_US -> {
                        commitFragment(AboutFragment::class.java, aboutFragmentProvider, R.id.layout, uiTask)
                    }
                    UiSubtype.SETTINGS -> {
                        commitFragment(SettingsFragment::class.java, settingsFragmentProvider, R.id.layout, uiTask)
                    }
                }
            }
            else -> {
            }
        }*/
    }

    override fun onBackPressed() {
        val fragment = currentFragment
        if (fragment != null && fragment.hasBackPressed()) {
            return
        }
        finish()
    }

    override fun onStopUi() {
    }
}
