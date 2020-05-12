package com.kddi.htb

import android.app.Application
import android.util.Log
import com.twitter.sdk.android.core.DefaultLogger
import com.twitter.sdk.android.core.Twitter
import com.twitter.sdk.android.core.TwitterAuthConfig
import com.twitter.sdk.android.core.TwitterConfig

/**
 * Created by roman on 11/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        val config = TwitterConfig.Builder(this)
            .logger(DefaultLogger(Log.DEBUG))
            .twitterAuthConfig(
                TwitterAuthConfig(
                    resources.getString(R.string.com_twitter_sdk_android_CONSUMER_KEY),
                    resources.getString(R.string.com_twitter_sdk_android_CONSUMER_SECRET)
                )
            )
            //.debug(BuildConfig.DEBUG)
            .build()

        Twitter.initialize(config)
    }
}