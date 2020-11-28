package com.dreampany.news.ui.web

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import com.dreampany.framework.misc.exts.task
import com.dreampany.framework.ui.activity.InjectActivity
import com.dreampany.news.R
import com.dreampany.news.databinding.WebActivityBinding
import com.dreampany.news.manager.AdsManager
import im.delight.android.webview.AdvancedWebView
import javax.inject.Inject

/**
 * Created by roman on 12/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class WebActivity : InjectActivity(), AdvancedWebView.Listener {

    @Inject
    internal lateinit var ads: AdsManager

    private lateinit var bind: WebActivityBinding
    private lateinit var url: String


    override val homeUp: Boolean = true

    override val layoutRes: Int = R.layout.web_activity
    //override fun toolbarId(): Int = R.id.toolbar

    override fun onStartUi(state: Bundle?) {
        initAd()
        initUi()
        url = task?.url ?: return
        bind.web.loadUrl(url)

        ads.loadBanner(this.javaClass.simpleName)
        ads.showInHouseAds(this)
        showProgress()
    }

    override fun onStopUi() {
        bind.web.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        bind.web.onResume()
        ads.resumeBanner(this.javaClass.simpleName)
    }

    override fun onPause() {
        bind.web.onPause()
        ads.pauseBanner(this.javaClass.simpleName)
        super.onPause()
    }

    override fun onBackPressed() {
        if (!bind.web.onBackPressed()) {
            return
        }
        super.onBackPressed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        bind.web.onActivityResult(requestCode, resultCode, intent)
    }

    override fun onPageFinished(url: String?) {
        hideProgress()
    }

    override fun onPageError(errorCode: Int, description: String?, failingUrl: String?) {
        hideProgress()
    }

    override fun onDownloadRequested(
        url: String?,
        suggestedFilename: String?,
        mimeType: String?,
        contentLength: Long,
        contentDisposition: String?,
        userAgent: String?
    ) {
    }

    override fun onExternalPageRequest(url: String?) {
    }

    override fun onPageStarted(url: String?, favicon: Bitmap?) {
    }

    private fun initAd() {
        ads.initAd(
            this,
            this.javaClass.simpleName,
            findViewById(R.id.adview),
            R.string.interstitial_ad_unit_id,
            R.string.rewarded_ad_unit_id
        )
    }

    private fun initUi() {
        if (::bind.isInitialized) return
        bind = binding()
        bind.web.setListener(this, this)

        bind.bottomBar.imageBack.setOnSafeClickListener {
            if (goBackIf()) {
                bind.bottomBar.imageForward.toTint(R.color.colorAccent)
            } else {
                bind.bottomBar.imageForward.toTint(R.color.material_grey400)
            }
        }
        bind.bottomBar.imageForward.setOnSafeClickListener {
            if (goForwardIf()) {
                bind.bottomBar.imageBack.toTint(R.color.colorAccent)
            } else {
                bind.bottomBar.imageBack.toTint(R.color.material_grey400)
            }
        }
        bind.bottomBar.imageReload.setOnSafeClickListener {
            bind.bottomBar.imageClose.toTint(R.color.colorAccent)
            bind.web.loadUrl(url)
        }
        bind.bottomBar.imageClose.setOnSafeClickListener { bind.web.stopLoading() }
    }

    private fun goBackIf(): Boolean {
        if (bind.web.canGoBack()) {
            bind.web.goBack()
            return true
        }
        return false
    }

    private fun goForwardIf(): Boolean {
        if (bind.web.canGoForward()) {
            bind.web.goForward()
            return true
        }
        return false
    }
}