package com.dreampany.tools.ui.news.activity

import android.os.Bundle
import com.dreampany.framework.ui.activity.InjectActivity
import com.dreampany.tools.data.source.news.pref.NewsPref
import com.dreampany.tools.databinding.RecyclerActivityBinding
import javax.inject.Inject
import com.dreampany.tools.R
/**
 * Created by roman on 22/10/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class PagesActivity : InjectActivity() {

    @Inject
    internal lateinit var pref: NewsPref

    private lateinit var bind: RecyclerActivityBinding
    private lateinit var vm: PageViewModel
    private lateinit var adapter: FastPageAdapter

    override val layoutRes: Int = R.layout.recycler_activity
    override val menuRes: Int = R.menu.pages_menu
    override val toolbarId: Int = R.id.toolbar

    override fun onStartUi(state: Bundle?) {

    }

    override fun onStopUi() {
     }
}