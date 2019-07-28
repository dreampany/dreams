package com.dreampany.frame.ui.activity

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import com.dreampany.frame.R
import com.dreampany.frame.data.model.Task
import com.dreampany.frame.databinding.ActivityWebBinding
import im.delight.android.webview.AdvancedWebView

/**
 * Created by roman on 2019-07-27
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class WebActivity : BaseActivity(), AdvancedWebView.Listener {

    internal lateinit var bindWeb: ActivityWebBinding

    override fun getLayoutId(): Int {
        return R.layout.activity_web
    }

    override fun isFullScreen(): Boolean {
        return true
    }

    override fun onStartUi(state: Bundle?) {
        bindWeb = super.binding as ActivityWebBinding

        val task = getCurrentTask<Task<*>>(false)
        if (task == null) {
            finish()
            return
        }
        bindWeb.webview.setListener(this, this)
        val url = task!!.comment
        bindWeb.webview.loadUrl(url)
    }

    override fun onStopUi() {
        bindWeb.webview.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        bindWeb.webview.onResume()
    }

    override fun onPause() {
        bindWeb.webview.onPause()
        super.onPause()
    }

    override fun onBackPressed() {
        if (!bindWeb.webview.onBackPressed()) {
            return
        }
        super.onBackPressed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        bindWeb.webview.onActivityResult(requestCode, resultCode, intent)
    }

    override fun onPageStarted(url: String, favicon: Bitmap?) {

    }

    override fun onPageFinished(url: String) {

    }

    override fun onPageError(errorCode: Int, description: String?, failingUrl: String?) {

    }

    override fun onDownloadRequested(
        url: String,
        suggestedFilename: String?,
        mimeType: String?,
        contentLength: Long,
        contentDisposition: String?,
        userAgent: String?
    ) {

    }

    override fun onExternalPageRequest(url: String) {

    }
}