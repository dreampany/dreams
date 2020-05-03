package com.dreampany.framework.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.dreampany.framework.ui.fragment.BaseFragment


/**
 * Created by Hawladar Roman on 5/25/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
class SmartPagerAdapter<T : BaseFragment>(fragmentManager: FragmentManager) : BaseStateAdapter<T>(fragmentManager) {

    override fun getItem(position: Int): Fragment {
        val fragment = getFragment(position)
        return if (fragment != null) fragment else newFragment(position)
    }
}