package com.dreampany.pair.ui.tutorial

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.dreampany.common.misc.extension.open
import com.dreampany.common.misc.extension.setOnSafeClickListener
import com.dreampany.common.ui.activity.InjectActivity
import com.dreampany.common.ui.adapter.BaseAdapter
import com.dreampany.common.ui.vm.factory.ViewModelFactory
import com.dreampany.pair.R
import com.dreampany.pair.databinding.TutorialActivityBinding
import com.dreampany.pair.ui.auth.activity.AuthActivity
import com.dreampany.pair.ui.auth.vm.AuthViewModel
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by roman on 3/10/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class TutorialActivity : InjectActivity() {

    @Inject
    internal lateinit var factory: ViewModelFactory

    private lateinit var bind: TutorialActivityBinding
    private lateinit var vm: AuthViewModel

    private lateinit var tutorialAdapter: TutorialAdapter

    override fun fullScreen(): Boolean = true

    override fun hasBinding(): Boolean = true

    override fun layoutRes(): Int = R.layout.tutorial_activity

    override fun onStartUi(state: Bundle?) {
        initUi()
        initRecycler()
    }

    override fun onStopUi() {
    }

    private fun onSafeClick(view: View) {
        Timber.v("Clicked %s", view.id)
        when (view) {
            bind.buttonJoin -> {
                joinPressed()
            }
        }
    }

    private fun initUi() {
        bind = getBinding()
        vm = ViewModelProvider(this, factory).get(AuthViewModel::class.java)

        bind.buttonJoin.setOnSafeClickListener(this::onSafeClick)
    }

    private fun initRecycler() {
        tutorialAdapter = TutorialAdapter(object : BaseAdapter.OnItemClickListener<TutorialItem> {
            override fun onItemClick(item: TutorialItem) {

            }

            override fun onChildItemClick(view: View, item: TutorialItem) {
            }
        })
        tutorialAdapter.add(TutorialItem(R.mipmap.ic_launcher, "Hello1", "This is Hello"))
        tutorialAdapter.add(TutorialItem(R.mipmap.ic_launcher, "Hello2", "This is Hello"))
        tutorialAdapter.add(TutorialItem(R.mipmap.ic_launcher, "Hello3", "This is Hello"))
        bind.pager.adapter = tutorialAdapter
        bind.indicator.setViewPager(bind.pager)
        tutorialAdapter.registerAdapterDataObserver(bind.indicator.adapterDataObserver)
    }

    private fun joinPressed() {
        vm.setJoinPressed(true)
        open(AuthActivity::class, true)
    }
}