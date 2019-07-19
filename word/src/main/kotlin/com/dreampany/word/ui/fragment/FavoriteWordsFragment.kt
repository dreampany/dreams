package com.dreampany.word.ui.fragment

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.dreampany.frame.misc.ActivityScope
import com.dreampany.frame.ui.adapter.SmartAdapter
import com.dreampany.frame.ui.fragment.BaseMenuFragment
import com.dreampany.frame.ui.listener.OnVerticalScrollListener
import com.dreampany.word.R
import com.dreampany.word.databinding.ContentRecyclerBinding
import com.dreampany.word.databinding.ContentTopStatusBinding
import com.dreampany.word.databinding.FragmentFavoritesBinding
import com.dreampany.word.ui.adapter.WordAdapter
import com.dreampany.word.ui.model.WordItem
import com.dreampany.word.vm.WordViewModel
import javax.inject.Inject

/**
 * Created by Roman-372 on 7/19/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@ActivityScope
class FavoriteWordsFragment @Inject constructor() : BaseMenuFragment(),
    SmartAdapter.Callback<WordItem> {

    private val NONE = "none"
    private val EMPTY = "empty"

    @Inject
    internal lateinit var factory: ViewModelProvider.Factory
    private lateinit var bind: FragmentFavoritesBinding
    private lateinit var bindStatus: ContentTopStatusBinding
    private lateinit var bindRecycler: ContentRecyclerBinding

    private lateinit var scroller: OnVerticalScrollListener
    private lateinit var vm: WordViewModel
    private lateinit var adapter: WordAdapter

    override fun getLayoutId(): Int {
        return R.layout.fragment_favorites
    }

    override fun getMenuId(): Int {
        return R.menu.menu_search
    }

    override fun getSearchMenuItemId(): Int {
        return R.id.item_search
    }

    override fun onStartUi(state: Bundle?) {

    }

    override fun onStopUi() {

    }


    override val empty: Boolean
        get() = false
    override val items: List<WordItem>?
        get() = adapter.currentItems
    override val visibleItems: List<WordItem>?
        get() = adapter.getVisibleItems()
    override val visibleItem: WordItem?
        get() = adapter.getVisibleItem()
}