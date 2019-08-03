package com.dreampany.tools.ui.fragment

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.dreampany.frame.api.session.SessionManager
import com.dreampany.frame.misc.ActivityScope
import com.dreampany.frame.ui.fragment.BaseMenuFragment
import com.dreampany.frame.ui.listener.OnVerticalScrollListener
import com.dreampany.tools.R
import com.dreampany.tools.databinding.ContentRecyclerBinding
import com.dreampany.tools.databinding.ContentTopStatusBinding
import com.dreampany.tools.databinding.FragmentHomeBinding
import com.dreampany.tools.ui.adapter.FeatureAdapter
import javax.inject.Inject

/**
 * Created by Roman-372 on 7/26/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@ActivityScope
class HomeFragment
@Inject constructor() :
    BaseMenuFragment() {

    @Inject
    internal lateinit var factory: ViewModelProvider.Factory
    @Inject
    internal lateinit var session: SessionManager
    private lateinit var bind: FragmentHomeBinding
    private lateinit var bindStatus: ContentTopStatusBinding
    private lateinit var bindRecycler: ContentRecyclerBinding

    private lateinit var scroller: OnVerticalScrollListener
    //private lateinit var vm:
    private lateinit var adapter: FeatureAdapter

    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }
/*
    override fun getMenuId(): Int {
        return R.menu.menu_home
    }

    override fun getSearchMenuItemId(): Int {
        return R.id.item_search
    }*/

    override fun onStartUi(state: Bundle?) {
        /*initView()
        initRecycler()

        session.track()
        initTitleSubtitle()
        request(true, true, false)*/
    }

    override fun onStopUi() {

    }


}