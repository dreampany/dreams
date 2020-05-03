package com.dreampany.framework.ui.activity

import android.view.Menu
import android.view.MenuInflater


/**
 * Created by roman on 2020-02-20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class BaseMenuActivity : BaseActivity() {

    protected var menu: Menu? = null

    open fun getMenuId(): Int {
        return 0
    }

    open fun onMenuCreated(menu: Menu) {

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        this.menu = menu
        val menuId = getMenuId()
        if (menuId != 0) { //this need clear
            menu.clear()
            menuInflater.inflate(menuId, menu)
            binding.root.post { onMenuCreated(menu) }
            return true
        }
        return super.onCreateOptionsMenu(menu)
    }
}