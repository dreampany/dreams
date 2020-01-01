package com.dreampany.framework.ui.activity

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import com.dreampany.framework.R
import com.dreampany.framework.data.model.Task
import com.dreampany.framework.databinding.ActivityWebBinding
import im.delight.android.webview.AdvancedWebView

/**
 * Created by roman on 2019-07-27
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class WebActivity : BaseActivity(), AdvancedWebView.Listener {

    private lateinit var bind: ActivityWebBinding

    override fun getLayoutId(): Int {
        return R.layout.activity_web
    }

    override fun isFullScreen(): Boolean {
        return true
    }

    override fun onStartUi(state: Bundle?) {
        bind = super.binding as ActivityWebBinding

        val task = getCurrentTask<Task<*>>(false)
        if (task == null) {
            finish()
            return
        }
        bind.webView.setListener(this, this)
        val url = task.extra
        bind.webView.loadUrl(url)
    }

    override fun onStopUi() {
        bind.webView.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        bind.webView.onResume()
    }

    override fun onPause() {
        bind.webView.onPause()
        super.onPause()
    }

    override fun onBackPressed() {
        if (!bind.webView.onBackPressed()) {
            return
        }
        super.onBackPressed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        bind.webView.onActivityResult(requestCode, resultCode, intent)
    }

    override fun onPageStarted(url: String, favicon: Bitmap?) {
        bind.viewSpin.visibility = View.VISIBLE
    }

    override fun onPageFinished(url: String) {
        bind.viewSpin.visibility = View.GONE
    }

    override fun onPageError(errorCode: Int, description: String?, failingUrl: String?) {
        bind.viewSpin.visibility = View.GONE
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