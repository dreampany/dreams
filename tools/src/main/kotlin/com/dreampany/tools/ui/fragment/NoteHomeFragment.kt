package com.dreampany.tools.ui.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter
import com.dreampany.frame.api.session.SessionManager
import com.dreampany.frame.data.enums.Action
import com.dreampany.frame.data.enums.Type
import com.dreampany.frame.data.model.Response
import com.dreampany.frame.misc.ActivityScope
import com.dreampany.frame.ui.enums.UiState
import com.dreampany.frame.ui.fragment.BaseMenuFragment
import com.dreampany.frame.ui.listener.OnUiItemClickListener
import com.dreampany.frame.ui.listener.OnVerticalScrollListener
import com.dreampany.frame.util.ColorUtil
import com.dreampany.frame.util.ViewUtil
import com.dreampany.tools.R
import com.dreampany.tools.data.misc.NoteRequest
import com.dreampany.tools.data.model.Note
import com.dreampany.tools.databinding.ContentRecyclerBinding
import com.dreampany.tools.databinding.ContentTopStatusBinding
import com.dreampany.tools.databinding.FragmentNoteHomeBinding
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.ui.activity.ToolsActivity
import com.dreampany.tools.ui.adapter.NoteAdapter
import com.dreampany.tools.ui.enums.NoteOption
import com.dreampany.tools.ui.model.NoteItem
import com.dreampany.frame.ui.model.UiTask
import com.dreampany.tools.vm.NoteViewModel
import com.skydoves.powermenu.MenuAnimation
import com.skydoves.powermenu.OnMenuItemClickListener
import com.skydoves.powermenu.PowerMenu
import com.skydoves.powermenu.PowerMenuItem
import cz.kinst.jakub.view.StatefulLayout
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration
import eu.davidea.flexibleadapter.common.SmoothScrollStaggeredLayoutManager
import timber.log.Timber
import javax.inject.Inject


/**
 * Created by roman on 2019-08-04
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@ActivityScope
class NoteHomeFragment @Inject constructor() :
    BaseMenuFragment(),
    OnUiItemClickListener<NoteItem?, Action?>,
    OnMenuItemClickListener<PowerMenuItem> {

    @Inject
    internal lateinit var factory: ViewModelProvider.Factory
    @Inject
    internal lateinit var session: SessionManager
    private lateinit var bind: FragmentNoteHomeBinding
    private lateinit var bindStatus: ContentTopStatusBinding
    private lateinit var bindRecycler: ContentRecyclerBinding

    private lateinit var adapter: NoteAdapter
    private lateinit var vm: NoteViewModel
    private lateinit var scroller: OnVerticalScrollListener

    private val powerItems = mutableListOf<PowerMenuItem>()
    private var powerMenu: PowerMenu? = null
    private var currentItem: NoteItem? = null

    override fun getLayoutId(): Int {
        return R.layout.fragment_note_home
    }

    override fun onStartUi(state: Bundle?) {
        initUi()
        initRecycler()
        createMenuItems()
        session.track()
        request(progress = true)
        initTitleSubtitle()
    }

    override fun onStopUi() {
        processUiState(UiState.HIDE_PROGRESS)
        powerMenu?.run {
            if (isShowing) {
                dismiss()
            }
        }
    }

    override fun onRefresh() {
        super.onRefresh()
        request(progress = true)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            Constants.RequestCode.ADD_NOTE,
            Constants.RequestCode.EDIT_NOTE -> {
                if (isOkay(resultCode)) {
                    ex.postToUi(Runnable{ request(true) }, 1000L)
                }
            }
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.fab -> {
                openAddNoteUi()
            }
            R.id.layout_empty -> {
                openAddNoteUi()
            }
        }
    }

    override fun onClick(view: View, item: NoteItem?, action: Action?) {

    }

    override fun onLongClick(view: View, item: NoteItem?, action: Action?) {
        openOptionsMenu(view, item)
    }

    override fun onItemClick(position: Int, item: PowerMenuItem) {
        powerMenu?.dismiss()
        val option: NoteOption = item.tag as NoteOption
        Timber.v("Option fired %s", option.toTitle())
        processOption(option, currentItem!!)
    }

    private fun initTitleSubtitle() {
        setTitle(R.string.title_note)
    }

    private fun initUi() {
        bind = super.binding as FragmentNoteHomeBinding
        bindStatus = bind.layoutTopStatus
        bindRecycler = bind.layoutRecycler

        bind.stateful.setStateView(
            UiState.DEFAULT.name,
            LayoutInflater.from(context).inflate(R.layout.item_default, null)
        )

        bind.stateful.setStateView(
            UiState.EMPTY.name,
            LayoutInflater.from(context).inflate(R.layout.item_empty_note, null).apply {
                setOnClickListener(this@NoteHomeFragment)
            }
        )

        processUiState(UiState.DEFAULT)

        ViewUtil.setSwipe(bind.layoutRefresh, this)
        bind.fab.setOnClickListener(this)

        val adapter = AHBottomNavigationAdapter(getParent(), R.menu.menu_bottom_note_home)
        adapter.setupWithBottomNavigation(bind.bottomNav)
        bind.bottomNav.apply {
            isTranslucentNavigationEnabled = true
            defaultBackgroundColor = ColorUtil.getColor(context, R.color.colorPrimary)
            accentColor = ColorUtil.getColor(context, R.color.colorAccent)
            inactiveColor = R.color.colorPrimaryDark
            isForceTint = true
            titleState = AHBottomNavigation.TitleState.SHOW_WHEN_ACTIVE
        }

        vm = ViewModelProviders.of(this, factory).get(NoteViewModel::class.java)
        vm.observeUiState(this, Observer { this.processUiState(it) })
        vm.observeOutputs(this, Observer { this.processMultipleResponse(it) })
        vm.observeOutput(this, Observer { this.processSingleResponse(it) })
    }

    private fun initRecycler() {
        bind.setItems(ObservableArrayList<Any>())
        adapter = NoteAdapter(this)
        adapter.setStickyHeaders(false)
        scroller = object : OnVerticalScrollListener() {}
        ViewUtil.setRecycler(
            adapter,
            bindRecycler.recycler,
            SmoothScrollStaggeredLayoutManager(context!!, adapter.getSpanCount()),
            FlexibleItemDecoration(context!!)
                .addItemViewType(R.layout.item_note, adapter.getItemOffset())
                .withEdge(true),
            null,
            scroller,
            null
        )
    }

    private fun createMenuItems() {
        if (powerItems.isEmpty()) {

            powerItems.add(
                PowerMenuItem(
                    NoteOption.EDIT.toTitle(),
                    // R.drawable.ic_edit_black_24dp,
                    NoteOption.EDIT
                )
            )
            powerItems.add(
                PowerMenuItem(
                    NoteOption.FAVORITE.toTitle(),
                    // R.drawable.ic_favorite_black_24dp,
                    NoteOption.FAVORITE
                )
            )
            powerItems.add(
                PowerMenuItem(
                    NoteOption.ARCHIVE.toTitle(),
                    // R.drawable.ic_archive_black_24dp,
                    NoteOption.ARCHIVE
                )
            )
            powerItems.add(
                PowerMenuItem(
                    NoteOption.TRASH.toTitle(),
                    //R.drawable.ic_delete_black_24dp,
                    NoteOption.TRASH
                )
            )
            powerItems.add(
                PowerMenuItem(
                    NoteOption.DELETE.toTitle(),
                    //R.drawable.ic_delete_forever_black_24dp,
                    NoteOption.DELETE
                )
            )
        }
    }

    private fun openOptionsMenu(view: View, item: NoteItem?) {
        if (item == null) {
            return
        }
        currentItem = item
        powerMenu = PowerMenu.Builder(context)
            .setAnimation(MenuAnimation.SHOWUP_TOP_RIGHT)
            .addItemList(powerItems)
            .setSelectedMenuColor(ColorUtil.getColor(context!!, R.color.colorPrimary))
            .setSelectedTextColor(Color.WHITE)
            .setOnMenuItemClickListener(this)
            .setLifecycleOwner(this)
            .setDividerHeight(1)
            .setTextSize(14)
            .build()
        powerMenu?.showAsAnchorRightBottom(view)
    }

    private fun processOption(option: NoteOption, item: NoteItem) {
        when (option) {
            NoteOption.EDIT -> {
                openEditNoteUi(item.item)
            }
        }
    }

    private fun request(progress: Boolean = Constants.Default.BOOLEAN) {
        val request = NoteRequest(
            action = Action.GET,
            single = false,
            progress = progress
        )
        vm.request(request)
    }

    private fun processUiState(state: UiState) {
        Timber.v("UiState %s", state.name)
        when (state) {
            UiState.DEFAULT -> bind.stateful.setState(UiState.DEFAULT.name)
            UiState.EMPTY -> bind.stateful.setState(UiState.EMPTY.name)
            UiState.SHOW_PROGRESS -> if (!bind.layoutRefresh.isRefreshing()) {
                bind.layoutRefresh.setRefreshing(true)
            }
            UiState.HIDE_PROGRESS -> if (bind.layoutRefresh.isRefreshing()) {
                bind.layoutRefresh.setRefreshing(false)
            }
            UiState.OFFLINE -> bindStatus.layoutExpandable.expand()
            UiState.ONLINE -> bindStatus.layoutExpandable.collapse()
            UiState.EXTRA -> processUiState(if (adapter.isEmpty()) UiState.EMPTY else UiState.CONTENT)
            UiState.CONTENT -> {
                bind.stateful.setState(StatefulLayout.State.CONTENT)
                initTitleSubtitle()
            }
        }
    }

    fun processMultipleResponse(response: Response<List<NoteItem>>) {
        if (response is Response.Progress<*>) {
            val result = response as Response.Progress<*>
            Timber.v("processMultipleResponse %s", result.loading)
            vm.processProgress(result.loading)
        } else if (response is Response.Failure<*>) {
            val result = response as Response.Failure<*>
            vm.processFailure(result.error)
        } else if (response is Response.Result<*>) {
            val result = response as Response.Result<List<NoteItem>>
            processSuccess(result.action, result.data)
        }
    }

    private fun processSuccess(action: Action, items: List<NoteItem>) {
        Timber.v("Result Action[%s] Size[%s]", action.name, items.size)
        adapter.addItems(items)
        ex.postToUi(Runnable{ processUiState(UiState.EXTRA) }, 500L)
    }

    fun processSingleResponse(response: Response<NoteItem>) {
        if (response is Response.Progress<*>) {
            val result = response as Response.Progress<*>
            Timber.v("processSingleResponse %s", result.loading)
            vm.processProgress(result.loading)
        } else if (response is Response.Failure<*>) {
            val result = response as Response.Failure<*>
            vm.processFailure(result.error)
        } else if (response is Response.Result<*>) {
            val result = response as Response.Result<NoteItem>
            processSuccess(result.action, result.data)
        }
    }

    private fun processSuccess(action: Action, item: NoteItem) {
        adapter.addItem(item)
        ex.postToUi(Runnable{ processUiState(UiState.EXTRA) }, 500L)
    }

    private fun openAddNoteUi() {
        val task = UiTask<Note>(type = Type.NOTE, action = Action.ADD)
        openActivity(ToolsActivity::class.java, task, Constants.RequestCode.ADD_NOTE)
    }

    private fun openEditNoteUi(note: Note) {
        val task = UiTask<Note>(type = Type.NOTE, action = Action.EDIT, input = note)
        openActivity(ToolsActivity::class.java, task, Constants.RequestCode.EDIT_NOTE)
    }
}