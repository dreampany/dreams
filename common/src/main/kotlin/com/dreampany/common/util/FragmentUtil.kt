package com.dreampany.common.util

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.dreampany.common.misc.AppExecutors
import com.dreampany.common.ui.activity.BaseActivity

/**
 * Created by roman on 2019-05-20
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class FragmentUtil private constructor() {
    companion object {
        fun <T : Fragment> getFragmentByTag(activity: AppCompatActivity, fragmentTag: String): T? {
            return activity.supportFragmentManager.findFragmentByTag(fragmentTag) as T?
        }

        fun <T : Fragment> commitFragment(
            activity: BaseActivity,
            fragment: T,
            parentId: Int,
            ex : AppExecutors
        ): T {

            val commitRunnable = Runnable {
                if (activity.isDestroyed || activity.isFinishing) {

                }
                activity.supportFragmentManager.beginTransaction()
                    //.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                    .replace(parentId, fragment, fragment.javaClass.simpleName).addToBackStack(null)
                    .commitAllowingStateLoss()
            }

            ex.uiExecutor.execute(commitRunnable, 250L)
            return fragment
        }
    }
}