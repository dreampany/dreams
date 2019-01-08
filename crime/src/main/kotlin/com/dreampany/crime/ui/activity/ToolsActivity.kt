package com.dreampany.crime.ui.activity

import android.os.Bundle
import com.dreampany.frame.ui.activity.BaseActivity
import com.dreampany.crime.R
import com.dreampany.crime.ui.enums.UiSubtype
import com.dreampany.crime.ui.enums.UiType
import com.dreampany.crime.ui.fragment.AboutFragment
import com.dreampany.crime.ui.fragment.SettingsFragment
import com.dreampany.crime.ui.model.UiTask
import dagger.Lazy
import javax.inject.Inject

/**
 * Created by Hawladar Roman on 5/24/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
class ToolsActivity : BaseActivity() {

    @Inject
    lateinit var aboutFragmentProvider: Lazy<AboutFragment>
    @Inject
    lateinit var settingsFragmentProvider: Lazy<SettingsFragment>


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

        when (type) {
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
        }
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
