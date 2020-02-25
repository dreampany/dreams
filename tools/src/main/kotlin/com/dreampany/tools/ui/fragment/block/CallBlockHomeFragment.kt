package com.dreampany.tools.ui.fragment.block

import android.Manifest
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.dreampany.framework.data.enums.Action
import com.dreampany.framework.data.enums.State
import com.dreampany.framework.data.enums.Subtype
import com.dreampany.framework.data.enums.Type
import com.dreampany.framework.data.model.Response
import com.dreampany.framework.misc.ActivityScope
import com.dreampany.framework.misc.extension.inflate
import com.dreampany.framework.misc.extension.setOnSafeClickListener
import com.dreampany.framework.misc.extension.toTint
import com.dreampany.framework.ui.adapter.SmartAdapter
import com.dreampany.framework.ui.callback.SearchViewCallback
import com.dreampany.framework.ui.enums.UiState
import com.dreampany.framework.ui.fragment.BaseMenuFragment
import com.dreampany.framework.ui.listener.OnVerticalScrollListener
import com.dreampany.framework.util.AndroidUtil
import com.dreampany.framework.util.NumberUtil
import com.dreampany.framework.util.ViewUtil
import com.dreampany.tools.R
import com.dreampany.tools.data.enums.CallBlockType
import com.dreampany.tools.data.source.pref.BlockPref
import com.dreampany.tools.databinding.ContentRecyclerBinding
import com.dreampany.tools.databinding.ContentTopStatusBinding
import com.dreampany.tools.databinding.FragmentCallBlockHomeBinding
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.ui.adapter.ContactAdapter
import com.dreampany.tools.ui.misc.ContactRequest
import com.dreampany.tools.ui.model.ContactItem
import com.dreampany.tools.ui.vm.ContactViewModel
import com.ferfalk.simplesearchview.SimpleSearchView
import com.karumi.dexter.Dexter
import cz.kinst.jakub.view.StatefulLayout
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager
import kotlinx.android.synthetic.main.content_input_number.*
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by roman on 2019-11-15
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@ActivityScope
class CallBlockHomeFragment
@Inject constructor() : BaseMenuFragment(),
    SmartAdapter.OnUiItemClickListener<ContactItem, Action> {

    @Inject
    internal lateinit var factory: ViewModelProvider.Factory
    @Inject
    internal lateinit var blockPref: BlockPref

    private lateinit var bind: FragmentCallBlockHomeBinding
    private lateinit var bindStatus: ContentTopStatusBinding
    private lateinit var bindRecycler: ContentRecyclerBinding

    private lateinit var scroller: OnVerticalScrollListener
    private lateinit var searchView: SimpleSearchView

    private lateinit var vm: ContactViewModel
    private lateinit var adapter: ContactAdapter

    private val REQUIRED_PERMISSIONS = listOf(
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.ANSWER_PHONE_CALLS,
        /*Manifest.permission.READ_CALL_LOG,*/
        Manifest.permission.CALL_PHONE
    )

    override fun getLayoutId(): Int {
        return R.layout.fragment_call_block_home
    }

    override fun getMenuId(): Int {
        return R.menu.menu_block_home
    }

    override fun getSearchMenuItemId(): Int {
        return R.id.item_search
    }

    override fun getTitleResId(): Int {
        return R.string.title_feature_call_block
    }

    override fun getScreen(): String {
        return Constants.blockHome(context!!)
    }

    override fun onMenuCreated(menu: Menu, inflater: MenuInflater) {
        super.onMenuCreated(menu, inflater)

        val searchItem = getSearchMenuItem()
        searchItem.toTint(context, R.color.material_white)
        findMenuItemById(R.id.item_favorite).toTint(context, R.color.material_white)
        findMenuItemById(R.id.item_settings).toTint(context, R.color.material_white)

        val activity = getParent()
        if (activity is SearchViewCallback) {
            val searchCallback = activity as SearchViewCallback?
            searchView = searchCallback!!.searchView
            val searchItem = getSearchMenuItem()
            searchItem?.run {
                //initSearchView(searchView, this)
            }
        }
        //initLanguageUi()
    }

    override fun onStartUi(state: Bundle?) {
        initUi()
        initRecycler()
        onRefresh()
    }

    override fun onStopUi() {
        vm.updateUiState(uiState = UiState.HIDE_PROGRESS)
        if (searchView.isSearchOpen()) {
            searchView.closeSearch()
        }
        vm.clear()
    }

    override fun onStart() {
        super.onStart()
        requestPermissions()
    }

    override fun onRefresh() {
        if (adapter.isEmpty) {
            request()
        } else {
            //requestToUpdate()
        }
    }

    override fun onQueryTextChange(newText: String): Boolean {
        if (adapter.hasNewFilter(newText)) {
            adapter.setFilter(newText)
            adapter.filterItems()
        }
        return false
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.fab -> {
                inputDialog()
            }
        }
    }

    override fun onUiItemClick(view: View, item: ContactItem, action: Action) {

    }

    override fun onUiItemLongClick(view: View, item: ContactItem, action: Action) {
        adapter.toggleSelection(item)
    }

    private fun initUi() {
        bind = super.binding as FragmentCallBlockHomeBinding
        bindStatus = bind.layoutTopStatus
        bindRecycler = bind.layoutRecycler

        ViewUtil.setSwipe(bind.layoutRefresh, this)
        bind.fab.setOnClickListener(this)

        bind.stateful.setStateView(
            UiState.DEFAULT.name,
            context.inflate(R.layout.item_default)
        )
        bind.stateful.setStateView(
            UiState.SEARCH.name,
            context.inflate(R.layout.item_search)
        )
        bind.stateful.setStateView(
            UiState.EMPTY.name,
            context.inflate(R.layout.content_empty_contacts)?.apply {
                setOnSafeClickListener {

                }
            }
        )

        vm = ViewModelProvider(this, factory).get(ContactViewModel::class.java)
        vm.observeUiState(this, Observer { this.processUiState(it) })
        vm.observeOutputs(this, Observer { this.processMultipleResponse(it) })
        vm.observeOutput(this, Observer { this.processSingleResponse(it) })
    }

    private fun initRecycler() {
        bind.setItems(ObservableArrayList<Any>())
        adapter = ContactAdapter(this)
        adapter.setStickyHeaders(false)
        scroller = object : OnVerticalScrollListener() {}
        ViewUtil.setRecycler(
            adapter,
            bindRecycler.recycler,
            SmoothScrollLinearLayoutManager(context!!),
            FlexibleItemDecoration(context!!)
                .withOffset(adapter.getItemOffset())
                .withEdge(true),
            null,
            scroller, null
        )
    }

    private fun processUiState(response: Response.UiResponse) {
        when (response.uiState) {
            UiState.DEFAULT -> bind.stateful.setState(UiState.DEFAULT.name)
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
            UiState.SEARCH -> bind.stateful.setState(UiState.SEARCH.name)
            UiState.EMPTY -> bind.stateful.setState(UiState.EMPTY.name)
            UiState.ERROR -> {
            }
            UiState.CONTENT -> bind.stateful.setState(StatefulLayout.State.CONTENT)
        }
    }

    private fun processMultipleResponse(response: Response<List<ContactItem>>) {
        if (response is Response.Progress<*>) {
            val result = response as Response.Progress<*>
            vm.processProgress(
                state = result.state,
                action = result.action,
                loading = result.loading
            )
        } else if (response is Response.Failure<*>) {
            val result = response as Response.Failure<*>
            vm.processFailure(state = result.state, action = result.action, error = result.error)
        } else if (response is Response.Result<*>) {
            val result = response as Response.Result<List<ContactItem>>
            processSuccess(result.state, result.action, result.data)
        }
    }

    private fun processSingleResponse(response: Response<ContactItem>) {
        if (response is Response.Progress<*>) {
            val result = response as Response.Progress<*>
            vm.processProgress(
                state = result.state,
                action = result.action,
                loading = result.loading
            )
        } else if (response is Response.Failure<*>) {
            val result = response as Response.Failure<*>
            vm.processFailure(state = result.state, action = result.action, error = result.error)
        } else if (response is Response.Result<*>) {
            val result = response as Response.Result<ContactItem>
            processSingleSuccess(result.state, result.action, result.data)
        }
    }

    private fun processSuccess(state: State, action: Action, items: List<ContactItem>) {
        Timber.v("Result Action[%s] Size[%s]", action.name, items.size)
        adapter.addItems(items)
        ex.postToUi(Runnable {
            vm.updateUiState(state = state, action = action, uiState = UiState.EXTRA)
        }, 500L)
    }

    private fun processSingleSuccess(state: State, action: Action, item: ContactItem) {
        Timber.v("Result Single Coin[%s]", item.item.id)
        adapter.addItem(item)
        ex.postToUi(Runnable {
            vm.updateUiState(state = state, action = action, uiState = UiState.EXTRA)
        }, 500L)
    }

    private fun request() {
        request(
            state = State.BLOCKED,
            single = false,
            progress = true,
            start = adapter.itemCount.toLong(),
            limit = Constants.Limit.Crypto.LIST
        )
    }

    private fun inputDialog() {
        MaterialDialog(context!!).show {
            customView(R.layout.content_input_number)
            button_save.setOnClickListener {
                val nameCode = picker_country.selectedCountryNameCode
                val numberCode = picker_country.selectedCountryCode
                val number = edit_phone_number.text?.toString()
                if (number.isNullOrEmpty()) {
                    edit_phone_number.error = getString(R.string.error_empty_phone_number)
                    return@setOnClickListener
                }
                if (!NumberUtil.isValidPhoneNumber(nameCode, number)) {
                    edit_phone_number.error = getString(R.string.error_invalid_phone_number)
                    return@setOnClickListener
                }
                saveNumber(numberCode, number)
                ex.postToUi(kotlinx.coroutines.Runnable {
                    dismiss()
                })
            }
        }
    }

    private fun saveNumber(countryCode: String, number: String) {
        Timber.v("CountryCode - Number [%s - %s]", countryCode, number)
        request(
            action = Action.BLOCK,
            single = true,
            progress = true,
            callBlockType = CallBlockType.EXACT,
            countryCode = countryCode,
            number = number
        )
    }

    private fun requestToUpdate() {
        val visibles = adapter.getVisibleItems()
        if (visibles.isNullOrEmpty()) return
        val ids = arrayListOf<String>()
        visibles.forEach { ci ->
            ids.add(ci.item.id)
        }
        request(
            ids = ids,
            action = Action.UPDATE,
            single = false,
            progress = true
        )
    }

    private fun request(
        state: State = State.DEFAULT,
        action: Action = Action.DEFAULT,
        single: Boolean = Constants.Default.BOOLEAN,
        progress: Boolean = Constants.Default.BOOLEAN,
        start: Long = Constants.Default.LONG,
        limit: Long = Constants.Default.LONG,
        id: String? = Constants.Default.NULL,
        ids: List<String>? = Constants.Default.NULL,
        callBlockType: CallBlockType? = Constants.Default.NULL,
        countryCode: String = Constants.Default.STRING,
        number: String = Constants.Default.STRING
    ) {
        val request = ContactRequest(
            type = Type.CONTACT,
            subtype = Subtype.DEFAULT,
            state = state,
            action = action,
            single = single,
            progress = progress,
            start = start,
            limit = limit,
            id = id,
            ids = ids,
            blockType = callBlockType,
            countryCode = countryCode,
            phoneNumber = number
        )
        vm.request(request)
    }

    private fun requestPermissions() {
        if (AndroidUtil.hasMarshmallow()) {
            Dexter.withActivity(activity)
                .withPermissions(REQUIRED_PERMISSIONS)
                .withListener(this).check()
        }
    }
}