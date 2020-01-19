package com.dreampany.tools.ui.fragment.resume

import android.content.Context
import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintManager
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import com.dreampany.framework.api.session.SessionManager
import com.dreampany.framework.databinding.FragmentWebBinding
import com.dreampany.framework.misc.ActivityScope
import com.dreampany.framework.ui.fragment.BaseMenuFragment
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.tools.R
import com.dreampany.tools.data.mapper.ResumeMapper
import com.dreampany.tools.data.model.Resume
import com.dreampany.tools.misc.Constants
import javax.inject.Inject

/**
 * Created by roman on 2020-01-19
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@ActivityScope
class ResumePreviewFragment
@Inject constructor() :
    BaseMenuFragment() {

    @Inject
    internal lateinit var factory: ViewModelProvider.Factory
    @Inject
    internal lateinit var session: SessionManager
    @Inject
    internal lateinit var mapper: ResumeMapper
    private lateinit var bind: FragmentWebBinding

    override fun getLayoutId(): Int {
        return R.layout.fragment_web
    }

    override fun getMenuId(): Int {
        return R.menu.menu_resume_preview
    }

    override fun onStartUi(state: Bundle?) {
        initUi()
    }

    override fun onStopUi() {

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_print -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initUi() {
        bind = super.binding as FragmentWebBinding
        val uiTask = getCurrentTask<UiTask<Resume>>() ?: return
        uiTask.input?.run {
            val content = Constants.Extra.toPrintableContent(this)
            bind.web.loadDataWithBaseURL(null, content, "text/html", "utf-8", null)
        }
    }

    private fun printResume() {
         val printManager = activity?.getSystemService(Context.PRINT_SERVICE) as PrintManager? ?: return
        // Get a print adapter instance
        val printAdapter: PrintDocumentAdapter = bind.web.createPrintDocumentAdapter()
        // Create a print job with name and adapter instance
        val job = getString(R.string.app_name) + " Document"
        val printJob = printManager.print(
            job, printAdapter,
            PrintAttributes.Builder().build()
        )
    }
}