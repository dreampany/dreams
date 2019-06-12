package com.dreampany.lca.ui.fragment

import android.os.Bundle
import com.dreampany.frame.data.model.Task
import com.dreampany.frame.misc.ActivityScope
import com.dreampany.frame.ui.fragment.BaseFragment
import com.dreampany.frame.ui.fragment.BaseStateFragment
import com.dreampany.frame.util.TextUtil
import com.dreampany.lca.R
import com.dreampany.lca.misc.Constants
import com.dreampany.lca.ui.model.UiTask
import javax.inject.Inject

/**
 * Created by Roman-372 on 6/12/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@ActivityScope
class LibraryFragment @Inject constructor() : BaseStateFragment<BaseFragment>() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_tabpager_fixed
    }

/*    override fun getMenuId(): Int {
        return R.menu.menu_library
    }

    override fun getSearchMenuItemId(): Int {
        return R.id.item_search
    }*/

    override fun pageTitles(): Array<String> {
        return TextUtil.getStrings(context, R.string.favorites, R.string.alerts)
    }

    override fun pageClasses(): Array<Class<BaseFragment>> {
        val fav: Class<BaseFragment> = FavoritesFragment::class.java as Class<BaseFragment>
        val alert: Class<BaseFragment> = CoinAlertsFragment::class.java as Class<BaseFragment>
        return arrayOf(fav, alert)
    }

    override fun pageTasks(): Array<Task<*>>? {
        val task = getCurrentTask<UiTask<*>>(false)
        task?.let {
            return arrayOf(task, task, task)
        }
        return null
    }

    override fun hasAllPages(): Boolean {
        return true
    }

    override fun hasTabColor(): Boolean {
        return true
    }

    override fun getScreen(): String {
        return Constants.library(getAppContext()!!)
    }

    override fun onStartUi(state: Bundle?) {
        initView()
    }

    override fun onStopUi() {
    }

/*    override fun onQueryTextChange(newText: String): Boolean {
        val fragment = getCurrentFragment()
        return fragment != null && fragment.onQueryTextChange(newText)
    }*/

    private fun initView() {
        setTitle(R.string.library)
        setSubtitle(null)
    }
}