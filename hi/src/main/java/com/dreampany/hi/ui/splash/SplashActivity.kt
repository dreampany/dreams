package com.dreampany.hi.ui.splash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dreampany.hi.misc.Executors
import com.dreampany.hi.open
import com.dreampany.hi.ui.auth.AuthActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Created by roman on 5/7/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    @Inject
    internal lateinit var ex: Executors

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        open(AuthActivity::class, true)
    }
}