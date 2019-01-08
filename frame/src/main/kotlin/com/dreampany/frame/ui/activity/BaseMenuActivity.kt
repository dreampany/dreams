package com.dreampany.frame.ui.activity

import android.view.Menu


/**
 * Created by Hawladar Roman on 5/22/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */

abstract class BaseMenuActivity : BaseActivity() {

    open fun getMenuId(): Int {
        return 0
    }

    open fun onMenuCreated(menu: Menu) {

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
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