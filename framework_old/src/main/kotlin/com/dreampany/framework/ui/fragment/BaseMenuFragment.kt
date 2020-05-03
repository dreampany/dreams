package com.dreampany.framework.ui.fragment

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import androidx.appcompat.widget.SearchView
import android.text.InputType
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import com.dreampany.framework.R


/**
 * Created by roman on 2020-02-20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class BaseMenuFragment : BaseFragment() {

    protected var menu: Menu? = null
    protected var inflater: MenuInflater? = null

    open fun getMenuId(): Int = 0

    open fun getSearchMenuItemId(): Int = 0

    open fun onMenuCreated(menu: Menu, inflater: MenuInflater) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        this.menu = menu
        this.inflater = inflater
        val menuId = getMenuId()

        if (menuId == 0) {
            onMenuCreated(menu, inflater)
            return
        }
        menu.clear()
        inflater.inflate(menuId, menu)
        //binding.root.post {
            onMenuCreated(menu, inflater)
            initSearch()
        //}
    }

    protected fun findMenuItemById(menuItemId: Int) : MenuItem? {
        if (menu == null || menuItemId == 0) {
            return null
        }
        val item = menu?.findItem(menuItemId)
        if (item == null) {
            return null
        }
        return item
    }

    protected fun getSearchMenuItem(): MenuItem? {
        val menuItemId = getSearchMenuItemId()
        val item = findMenuItemById(menuItemId)
        return item
    }

    protected fun getSearchView() : SearchView? {
        val item = getSearchMenuItem()
        if (item == null) {
            return null
        }
        val view = item.actionView
        if (view == null) {
            return null
        }
        val searchView = item.actionView as SearchView
        return searchView
    }

    fun initSearch() {
        val searchView = getSearchView()
        searchView?.let {
            searchView.inputType = InputType.TYPE_TEXT_VARIATION_FILTER
            searchView.imeOptions = EditorInfo.IME_ACTION_DONE or EditorInfo.IME_FLAG_NO_FULLSCREEN
            searchView.queryHint = getString(R.string.search)
            val searchManager = searchView.context.getSystemService(Context.SEARCH_SERVICE) as SearchManager
            val activity = activity
            searchView.setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
            searchView.setOnQueryTextListener(this)
        }


/*        MenuItemCompat.setOnActionExpandListener(item, object : MenuItemCompat.OnActionExpandListener {
            override fun onMenuItemActionExpand(menuItem: MenuItem): Boolean {
                //vm.updateUiMode(UiMode.SEARCH);
                return true
            }

            override fun onMenuItemActionCollapse(menuItem: MenuItem): Boolean {
                //vm.updateUiMode(UiMode.MAIN);
                return true
            }
        })*/
    }



    fun getQuery(): String? {
        val searchView = getSearchView()
        if (searchView != null) {
            return searchView.query.toString()
        }
        return null
    }
}