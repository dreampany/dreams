package com.dreampany.tools.ui.fragment.note

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dreampany.framework.api.session.SessionManager
import com.dreampany.framework.data.enums.Action
import com.dreampany.framework.data.enums.State
import com.dreampany.framework.data.enums.Type
import com.dreampany.framework.data.model.Response
import com.dreampany.framework.injector.annote.ActivityScope
import com.dreampany.framework.misc.extension.toTint
import com.dreampany.framework.ui.adapter.SmartAdapter
import com.dreampany.framework.ui.enums.UiState
import com.dreampany.framework.ui.fragment.BaseMenuFragment
import com.dreampany.framework.ui.listener.OnVerticalScrollListener
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.framework.util.ColorUtil
import com.dreampany.framework.util.ViewUtil
import com.dreampany.tools.R
import com.dreampany.tools.data.model.Note
import com.dreampany.tools.databinding.ContentRecyclerBinding
import com.dreampany.tools.databinding.ContentTopStatusBinding
import com.dreampany.tools.databinding.FragmentFavoriteNotesBinding
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.ui.activity.ToolsActivity
import com.dreampany.tools.ui.adapter.NoteAdapter
import com.dreampany.tools.ui.enums.NoteOption
import com.dreampany.tools.ui.request.NoteRequest
import com.dreampany.tools.ui.model.NoteItem
import com.dreampany.tools.ui.vm.note.NoteViewModel
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
class FavoriteNotesFragment
@Inject constructor() :
    BaseMenuFragment(),
    SmartAdapter.OnUiItemClickListener<NoteItem?, Action?>,
    OnMenuItemClickListener<PowerMenuItem> {

    @Inject
    internal lateinit var factory: ViewModelProvider.Factory
    @Inject
    internal lateinit var session: SessionManager
    private lateinit var bind: FragmentFavoriteNotesBinding
    private lateinit var bindStatus: ContentTopStatusBinding
    private lateinit var bindRecycler: ContentRecyclerBinding

    private lateinit var adapter: NoteAdapter
    private lateinit var vm: NoteViewModel
    private lateinit var scroller: OnVerticalScrollListener

    private val optionItems = mutableListOf<PowerMenuItem>()
    private var powerMenu: PowerMenu? = null
    private var currentItem: NoteItem? = null
    private var updated: Boolean = false

    override fun getLayoutId(): Int {
        return R.layout.fragment_favorite_notes
    }

    override fun getMenuId(): Int {
        return R.menu.menu_favorite_notes
    }

    override fun getSearchMenuItemId(): Int {
        return R.id.item_search
    }

    override fun getTitleResId(): Int {
        return R.string.title_favorite_notes
    }

    override fun getScreen(): String {
        return Constants.favoriteNotes(context!!)
    }

    override fun onMenuCreated(menu: Menu, inflater: MenuInflater) {
        super.onMenuCreated(menu, inflater)
        getSearchMenuItem().toTint(context, R.color.material_white)
    }

    override fun onStartUi(state: Bundle?) {
        initUi()
        initRecycler()
        createMenuItems()
        session.track()
        request(action = Action.FAVORITE, progress = true)
        initTitleSubtitle()
    }

    override fun onStopUi() {
        vm.updateUiState(uiState = UiState.HIDE_PROGRESS)
        powerMenu?.run {
            if (isShowing) {
                dismiss()
            }
        }
    }

    override fun onRefresh() {
        super.onRefresh()
        request(action = Action.FAVORITE, progress = true)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            Constants.RequestCode.VIEW,
            Constants.RequestCode.ADD,
            Constants.RequestCode.EDIT -> {
                if (isOkay(resultCode)) {
                    ex.postToUi(
                        Runnable { request(action = Action.FAVORITE, progress = true) },
                        500L
                    )
                }
            }
        }
    }

    override fun onQueryTextChange(newText: String): Boolean {
        if (adapter.isEmpty) return false
        if (adapter.hasNewFilter(newText)) {
            adapter.setFilter(newText)
            adapter.filterItems()
        }
        return false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_favorite ->

                return true
            R.id.item_settings -> {

                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.button_favorite -> {
                val note = v.tag as Note?
                request(id = note?.id, action = Action.FAVORITE, input = note, single = true)
            }
            R.id.layout_empty -> {
                openAddNoteUi()
            }
        }
    }

    override fun onUiItemClick(view: View, item: NoteItem?, action: Action?) {

    }

    override fun onUiItemLongClick(view: View, item: NoteItem?, action: Action?) {
        openOptionsMenu(view, item)
    }

    override fun onItemClick(position: Int, item: PowerMenuItem) {
        powerMenu?.dismiss()
        val option: NoteOption = item.tag as NoteOption
        Timber.v("Option fired %s", option.toTitle())
        processOption(option, currentItem!!)
    }

    override fun hasBackPressed(): Boolean {
        forResult(updated)
        return true
    }


    private fun initTitleSubtitle() {
        /*setTitle(R.string.title_favorite_notes)*/
        val subtitle = getString(R.string.subtitle_favorite_notes, adapter.itemCount)
        setSubtitle(subtitle)
    }

    private fun initUi() {
        bind = super.binding as FragmentFavoriteNotesBinding
        bindStatus = bind.layoutTopStatus
        bindRecycler = bind.layoutRecycler

        bind.stateful.setStateView(
            UiState.DEFAULT.name,
            LayoutInflater.from(context).inflate(R.layout.item_default, null)
        )

        bind.stateful.setStateView(
            UiState.EMPTY.name,
            LayoutInflater.from(context).inflate(R.layout.content_empty_note, null).apply {
                setOnClickListener(this@FavoriteNotesFragment)
            }
        )

        ViewUtil.setSwipe(bind.layoutRefresh, this)

        vm = ViewModelProvider(this, factory).get(NoteViewModel::class.java)
        vm.observeUiState(this, Observer { this.processUiState(it) })
        vm.observeOutputs(this, Observer { this.processMultipleResponse(it) })
        vm.observeOutput(this, Observer { this.processSingleResponse(it) })
        vm.updateUiState(uiState = UiState.DEFAULT)
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
        if (optionItems.isNotEmpty()) {
            return
        }
        val options = NoteOption.getAll()
        for (option in options) {
            optionItems.add(PowerMenuItem(option.toString(), option))
        }
    }

    private fun openOptionsMenu(view: View, item: NoteItem?) {
        if (item == null) {
            return
        }
        currentItem = item
        powerMenu = PowerMenu.Builder(context)
            .setAnimation(MenuAnimation.SHOWUP_TOP_RIGHT)
            .addItemList(optionItems)
            .setSelectedMenuColor(ColorUtil.getColor(context!!, R.color.colorPrimary))
            .setSelectedTextColor(Color.WHITE)
            .setOnMenuItemClickListener(this)
            .setLifecycleOwner(this)
            .setDividerHeight(1)
            .setTextSize(12)
            .build()
        powerMenu?.showAsAnchorRightBottom(view)
    }

    private fun processOption(option: NoteOption, item: NoteItem) {
        when (option) {
            NoteOption.EDIT -> {
                openEditNoteUi(item.item)
            }
            NoteOption.FAVORITE -> {
                request(action = Action.FAVORITE, input = item.item, single = true)
            }
            NoteOption.ARCHIVE -> {
                request(action = Action.ARCHIVE, input = item.item, single = true)
            }
            NoteOption.TRASH -> {
                request(action = Action.TRASH, input = item.item, single = true)
            }
            NoteOption.DELETE -> {
                request(action = Action.DELETE, input = item.item, single = true)
            }
        }
    }

    private fun processUiState(response: Response.UiResponse) {
        Timber.v("UiState %s", response.uiState.name)
        when (response.uiState) {
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
            UiState.EXTRA -> {
                response.uiState = if (adapter.isEmpty()) UiState.EMPTY else UiState.CONTENT
                processUiState(response)
            }
            UiState.CONTENT -> {
                bind.stateful.setState(StatefulLayout.State.CONTENT)
                initTitleSubtitle()
            }
        }
    }

    fun processMultipleResponse(response: Response<List<NoteItem>>) {
        if (response is Response.Progress<*>) {
            val result = response as Response.Progress<*>
            vm.processProgress(state = result.state, action =  result.action, loading =  result.loading)
        } else if (response is Response.Failure<*>) {
            val result = response as Response.Failure<*>
            vm.processFailure(state =  result.state,  action = result.action, error = result.error)
        } else if (response is Response.Result<*>) {
            val result = response as Response.Result<List<NoteItem>>
            processSuccess(result.state, result.action, result.data)
        }
    }

    private fun processSuccess(state: State, action: Action, items: List<NoteItem>) {
        Timber.v("Result Action[%s] Size[%s]", action.name, items.size)
        adapter.addItems(items)
        ex.postToUi(Runnable {
            vm.updateUiState(state = state, action =  action, uiState =  UiState.EXTRA)
        }, 500L)
    }

    fun processSingleResponse(response: Response<NoteItem>) {
        if (response is Response.Progress<*>) {
            val result = response as Response.Progress<*>
            vm.processProgress(state = result.state, action =  result.action, loading =  result.loading)
        } else if (response is Response.Failure<*>) {
            val result = response as Response.Failure<*>
            vm.processFailure(state =  result.state,  action = result.action, error = result.error)
        } else if (response is Response.Result<*>) {
            val result = response as Response.Result<NoteItem>
            processSuccess(result.state, result.action, result.data)
        }
    }

    private fun processSuccess(state: State, action: Action, item: NoteItem) {
        updated = true
        if (action == Action.DELETE) {
            adapter.removeItem(item)
        } else {
            adapter.addItem(item)
        }
        ex.postToUi(Runnable { vm.updateUiState(state =  state,action =  action, uiState =  UiState.EXTRA) }, 500L)
    }

    private fun openAddNoteUi() {
        val task = UiTask<Note>(
            type = Type.NOTE,
            action = Action.ADD
        )
        openActivity(ToolsActivity::class.java, task, Constants.RequestCode.ADD)
    }

    private fun openEditNoteUi(note: Note) {
        val task = UiTask<Note>(
            type = Type.NOTE,
            action = Action.EDIT,
            input = note
        )
        openActivity(ToolsActivity::class.java, task, Constants.RequestCode.EDIT)
    }

    private fun openFavoriteUi() {
        val task = UiTask<Note>(
            type = Type.NOTE,
            state = State.FAVORITE,
            action = Action.OPEN
        )
        openActivity(ToolsActivity::class.java, task, Constants.RequestCode.SETTINGS)
    }

    private fun openSettingsUi() {
        val task = UiTask<Note>(
            type = Type.NOTE,
            state = State.SETTINGS,
            action = Action.OPEN
        )
        openActivity(ToolsActivity::class.java, task, Constants.RequestCode.SETTINGS)
    }

    private fun request(
        id: String? = Constants.Default.NULL,
        action: Action = Action.DEFAULT,
        single: Boolean = Constants.Default.BOOLEAN,
        progress: Boolean = Constants.Default.BOOLEAN,
        input: Note? = Constants.Default.NULL
    ) {
        val request = NoteRequest(
            id = id,
            action = action,
            input = input,
            single = single,
            progress = progress
        )
        vm.request(request)
    }
}