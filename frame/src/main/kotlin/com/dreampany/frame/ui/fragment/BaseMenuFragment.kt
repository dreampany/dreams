package com.dreampany.frame.ui.fragment

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import androidx.appcompat.widget.SearchView
import android.text.InputType
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import com.dreampany.frame.R


/**
 * Created by Hawladar Roman on 5/22/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
abstract class BaseMenuFragment : BaseFragment() {

    protected var menu: Menu? = null
    protected var inflater: MenuInflater? = null

    open fun getMenuId(): Int {
        return 0
    }

    open fun getSearchMenuItemId(): Int {
        return 0
    }

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