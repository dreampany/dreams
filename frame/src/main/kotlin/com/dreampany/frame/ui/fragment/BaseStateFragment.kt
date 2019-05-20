package com.dreampany.frame.ui.fragment

import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.dreampany.frame.R
import com.dreampany.frame.data.model.Task
import com.dreampany.frame.misc.Constants
import com.dreampany.frame.ui.adapter.SmartPagerAdapter
import com.dreampany.frame.util.ColorUtil
import timber.log.Timber


/**
 * Created by Hawladar Roman on 5/25/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
abstract class BaseStateFragment<T : BaseFragment> : BaseMenuFragment() {

    private var adapter: SmartPagerAdapter<T>? = null

    protected abstract fun pageTitles(): Array<String>

    protected abstract fun pageClasses(): Array<Class<T>>

    protected abstract fun pageTasks() : Array<Task<*>>

    override fun getLayoutId(): Int {
        return R.layout.fragment_tabpager
    }

    open fun getViewPagerId(): Int {
        return R.id.view_pager
    }

    open fun getTabLayoutId(): Int {
        return R.id.tab_layout
    }

    open fun hasAllPages(): Boolean {
        return false
    }

    open fun hasTabColor(): Boolean {
        return false
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        fireOnStartUi = false
        super.onActivityCreated(savedInstanceState)
        initPager()
        onStartUi(savedInstanceState)
        getApp()?.throwAnalytics(Constants.Event.FRAGMENT, getScreen())
    }

    override fun getCurrentFragment(): BaseFragment? {
        val viewPager = findViewById<ViewPager>(getViewPagerId())
        if (viewPager != null && adapter != null) {
            val fragment = adapter?.getFragment(viewPager.getCurrentItem())
            if (fragment != null) {
                return fragment.getCurrentFragment()
            }
        }
        return null
    }

    fun getFragments(): List<BaseFragment>? {
        return adapter?.currentFragments
    }

    internal fun initPager() {

        val pageTitles = pageTitles()
        val pageClasses = pageClasses()
        val pageTasks = pageTasks()

        val viewPager = findViewById<ViewPager>(getViewPagerId())
        val tabLayout = findViewById<TabLayout>(getTabLayoutId())

        if (pageTitles.isEmpty() || pageClasses.isEmpty() || viewPager == null || tabLayout == null) {
            return
        }

        if (hasTabColor()) {
            if (hasColor() && applyColor()) {
                tabLayout.setBackgroundColor(ColorUtil.getColor(getContext(), color!!.getPrimaryId()))
                tabLayout.setSelectedTabIndicatorColor(
                        ColorUtil.getColor(getContext(), R.color.material_white))

                tabLayout.setTabTextColors(
                        ColorUtil.getColor(context, R.color.material_grey200),
                        ColorUtil.getColor(context, R.color.material_white))
            }
        }

        //if (adapter == null) {
            adapter = SmartPagerAdapter<T>(childFragmentManager)
        //}

        viewPager.setAdapter(adapter)
        tabLayout.setupWithViewPager(viewPager)

        //fragmentAdapter.removeAll();

        if (hasAllPages()) {
            Timber.v("Pages %d", pageClasses.size)
            viewPager.setOffscreenPageLimit(pageClasses.size)
        } else {
            //viewPager.setOffscreenPageLimit(pageClasses.length);
        }

        val pagerRunnable = {
            for (index in pageClasses.indices) {
                adapter?.addPage(pageTitles[index], pageClasses[index], pageTasks[index])
            }
        }
        viewPager.postDelayed(pagerRunnable, 250L)
    }
}