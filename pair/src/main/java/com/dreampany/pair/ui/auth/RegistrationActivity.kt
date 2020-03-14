package com.dreampany.pair.ui.auth

import android.os.Bundle
import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.lifecycle.ViewModelProvider
import com.dreampany.common.misc.extension.setOnSafeClickListener
import com.dreampany.common.ui.activity.BaseInjectorActivity
import com.dreampany.common.ui.vm.factory.ViewModelFactory
import com.dreampany.pair.R
import com.dreampany.pair.databinding.RegistrationActivityBinding
import com.dreampany.pair.ui.auth.vm.RegistrationViewModel
import javax.inject.Inject

/**
 * Created by roman on 3/12/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class RegistrationActivity : BaseInjectorActivity() {

    @Inject
    internal lateinit var factory: ViewModelFactory

    private lateinit var bind: RegistrationActivityBinding
    private lateinit var vm: RegistrationViewModel

    override fun hasBinding(): Boolean = true

    @LayoutRes
    override fun getLayoutId(): Int = R.layout.registration_activity

    @IdRes
    override fun getToolbarId(): Int = R.id.toolbar

    override fun onStartUi(state: Bundle?) {
        initUi()
    }

    override fun onStopUi() {
    }

    private fun onSafeClick(view: View) {
        when (view) {
            bind.buttonRegister -> {
                register()
            }
        }
    }

    private fun initUi() {
        bind = getBinding()
        vm = ViewModelProvider(this, factory).get(RegistrationViewModel::class.java)

        bind.buttonRegister.setOnSafeClickListener(this::onSafeClick)
    }


    private fun register() {
        val name = bind.inputName.toString()
        val email = bind.inputEmail.toString()
        val password = bind.inputPassword.toString()


    }
}