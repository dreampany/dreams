package com.dreampany.tools.ui.fragment

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dreampany.tools.R
import com.dreampany.framework.misc.ActivityScope
import com.dreampany.framework.ui.fragment.BaseFragment
import com.dreampany.framework.util.AndroidUtil
import com.dreampany.framework.util.TextUtil
import mehdi.sakout.aboutpage.AboutPage
import mehdi.sakout.aboutpage.Element
import javax.inject.Inject

/**
 * Created by roman on 2019-08-03
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@ActivityScope
class AboutFragment @Inject constructor() : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setTitle(R.string.title_about)
        val context = inflater.context
        val page = AboutPage(context)
            .isRTL(false)
            .setImage(R.mipmap.ic_launcher)
            .setDescription(TextUtil.getString(context, R.string.app_name))
            .addItem(getVersion(context))
            .addItem(getPrivacyPolicy(context))
            .addGroup("Connect with us")
            .addEmail(
                TextUtil.getString(
                    context,
                    R.string.email
                )/*, TextUtil.getString(context, R.string.title_email)*/
            )
            .addWebsite(
                TextUtil.getString(
                    context,
                    R.string.website
                )/*, TextUtil.getString(context, R.string.title_website)*/
            )
            .addPlayStore(AndroidUtil.getPackageName(context)/*, TextUtil.getString(context, R.string.title_play_store)*/)
            .addGitHub(
                TextUtil.getString(
                    context,
                    R.string.id_github
                )/*, TextUtil.getString(context, R.string.title_github)*/
            )/*.addItem(getYandexTranslation(context))*/

        return page.create()
    }

    override fun onStartUi(state: Bundle?) {

    }

    override fun onStopUi() {

    }

    private fun getVersion(context: Context): Element {
        val version = AndroidUtil.getVersionName(context)
        return Element()
            .setTitle(TextUtil.getString(context, R.string.summary_app_version, version))
    }

    private fun getPrivacyPolicy(context: Context): Element {
        return Element()
            .setTitle(TextUtil.getString(context, R.string.title_privacy_policy))
            .setOnClickListener { view ->
                val url = TextUtil.getString(context, R.string.url_privacy)
                val urlIntent = Intent(Intent.ACTION_VIEW)
                urlIntent.data = Uri.parse(url)
                startActivity(urlIntent)
            }
    }
}