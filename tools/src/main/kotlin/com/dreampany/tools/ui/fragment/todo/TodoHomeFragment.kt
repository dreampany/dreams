package com.dreampany.tools.ui.fragment.todo

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import androidx.lifecycle.ViewModelProvider
import com.dreampany.framework.api.session.SessionManager
import com.dreampany.framework.misc.ActivityScope
import com.dreampany.framework.ui.enums.UiState
import com.dreampany.framework.ui.fragment.BaseMenuFragment
import com.dreampany.framework.util.ColorUtil
import com.dreampany.framework.util.MenuTint
import com.dreampany.tools.R
import com.dreampany.tools.databinding.ContentRecyclerBinding
import com.dreampany.tools.databinding.ContentTopStatusBinding
import com.dreampany.tools.databinding.FragmentTodoHomeBinding
import com.dreampany.tools.misc.Constants
import javax.inject.Inject

/**
 * Created by roman on 2019-11-24
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@ActivityScope
class TodoHomeFragment
@Inject constructor() :
    BaseMenuFragment() {

    @Inject
    internal lateinit var factory: ViewModelProvider.Factory
    @Inject
    internal lateinit var session: SessionManager
    private lateinit var bind: FragmentTodoHomeBinding
    private lateinit var bindStatus: ContentTopStatusBinding
    private lateinit var bindRecycler: ContentRecyclerBinding

    override fun getLayoutId(): Int {
        return R.layout.fragment_todo_home
    }

    override fun getMenuId(): Int {
        return R.menu.menu_todo_home
    }

    override fun getSearchMenuItemId(): Int {
        return R.id.item_search
    }

    override fun getTitleResId(): Int {
        return R.string.title_feature_todo
    }

    override fun getScreen(): String {
        return Constants.noteHome(context!!)
    }

    override fun onMenuCreated(menu: Menu, inflater: MenuInflater) {
        super.onMenuCreated(menu, inflater)
        val searchItem = getSearchMenuItem()
        val favoriteItem = menu.findItem(R.id.item_favorite)
        val settingsItem = menu.findItem(R.id.item_settings)
        MenuTint.colorMenuItem(
            ColorUtil.getColor(context!!, R.color.material_white),
            null, searchItem, favoriteItem, settingsItem
        )
    }

    override fun onStartUi(state: Bundle?) {
        //initUi()
        //initRecycler()
        //createMenuItems()
        //request(progress = true)
        session.track()
    }

    override fun onStopUi() {
        //vm.updateUiState(uiState = UiState.HIDE_PROGRESS)
    }
}