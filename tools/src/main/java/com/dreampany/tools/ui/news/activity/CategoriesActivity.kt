package com.dreampany.tools.ui.news.activity

import android.os.Bundle
import com.dreampany.framework.ui.activity.InjectActivity
import com.dreampany.tools.data.source.news.pref.NewsPref
import javax.inject.Inject
import com.dreampany.tools.R
import com.dreampany.tools.databinding.RecyclerActivityBinding

/**
 * Created by roman on 13/10/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class CategoriesActivity : InjectActivity()  {

    @Inject
    internal lateinit var pref: NewsPref

    private lateinit var bind: RecyclerActivityBinding

    override val layoutRes: Int = R.layout.recycler_activity
    override val menuRes: Int = R.menu.categories_menu
    override val toolbarId: Int = R.id.toolbar

    override fun onStartUi(state: Bundle?) {


    }

    override fun onStopUi() {
     }
}