package com.dreampany.hi.ui.auth

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.dreampany.hi.R
import com.dreampany.hi.databinding.AuthActivityBinding
import com.dreampany.hi.open
import com.dreampany.hi.ui.vm.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by roman on 5/7/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {

    private val vm: UserViewModel by viewModels()
    private lateinit var binding: AuthActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.auth_activity)

        binding.login.setOnClickListener {
            open(LoginActivity::class)
        }

        binding.register.setOnClickListener {
            open(RegisterActivity::class)
        }

    }
}