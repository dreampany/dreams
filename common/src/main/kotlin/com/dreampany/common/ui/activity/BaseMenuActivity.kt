package com.dreampany.common.ui.activity

import android.view.Menu

/**
 * Created by roman on 2019-05-20
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class BaseMenuActivity : BaseActivity() {

    open fun getMenuId(): Int {
        return 0
    }

    open fun onMenuCreated(menu: Menu) {

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuId = getMenuId()
        if (menuId != 0) {
            menu.clear()
            menuInflater.inflate(menuId, menu)
            binding.root.post { onMenuCreated(menu) }
            return true
        }
        return super.onCreateOptionsMenu(menu)
    }
}