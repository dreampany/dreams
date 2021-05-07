package com.dreampany.hi.ui.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.dreampany.hi.R
import com.dreampany.hi.databinding.AuthActivityBinding
import com.dreampany.hi.databinding.LoginActivityBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by roman on 5/7/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: LoginActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView<LoginActivityBinding>(
            this,
            R.layout.login_activity
        )

    }

}
