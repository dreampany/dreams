package com.dreampany.ads

/**
 * Created by roman on 16/6/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface AdListener {
    fun onAdLoadFailed(exception: Exception)
    fun onAdLoaded()
    fun onAdClosed()
    fun onAdShown()
    fun onApplicationLeft()
}