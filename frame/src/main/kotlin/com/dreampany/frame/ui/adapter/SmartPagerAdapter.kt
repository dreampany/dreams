package com.dreampany.frame.ui.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.dreampany.frame.ui.fragment.BaseFragment


/**
 * Created by Hawladar Roman on 5/25/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
class SmartPagerAdapter<T : BaseFragment>(fragmentManager: FragmentManager) : BaseStateAdapter<T>(fragmentManager) {

    override fun getItem(position: Int): Fragment? {
        val fragment = getFragment(position)
        return if (fragment != null) fragment else newFragment(position)
    }
}