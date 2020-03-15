package com.dreampany.pair.ui.auth

import android.os.Bundle
import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dreampany.common.data.model.Response
import com.dreampany.common.misc.extension.isEmail
import com.dreampany.common.misc.extension.setOnSafeClickListener
import com.dreampany.common.misc.extension.string
import com.dreampany.common.ui.activity.BaseInjectorActivity
import com.dreampany.common.ui.vm.factory.ViewModelFactory
import com.dreampany.pair.R
import com.dreampany.pair.data.enums.Subtype
import com.dreampany.pair.data.enums.Type
import com.dreampany.pair.data.model.User
import com.dreampany.pair.databinding.RegistrationActivityBinding
import com.dreampany.pair.ui.auth.vm.RegistrationViewModel
import com.kaopiz.kprogresshud.KProgressHUD
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

    private lateinit var progress: KProgressHUD

    override fun hasBinding(): Boolean = true

    @LayoutRes
    override fun getLayoutId(): Int = R.layout.registration_activity

    @IdRes
    override fun getToolbarId(): Int = R.id.toolbar

    override fun onStartUi(state: Bundle?) {
        initUi()
    }

    override fun onStopUi() {
        hideProgress()
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
        vm.subscribe(this, Observer { this.processResponse(it) })

    }


    private fun register() {
        val name = bind.inputName.string()
        val email = bind.inputEmail.string()
        val password = bind.inputPassword.string()

        if (!email.isEmail()) {
            bind.inputName.error = getString(R.string.error_email)
            return
        }
        if (password.isEmpty()) {
            bind.inputPassword.error = getString(R.string.error_password)
            return
        }
        showProgress()
        vm.register(email, password, name)
    }

    private fun processResponse(response: Response<User, Type, Subtype>) {
        hideProgress()
    }

    private fun showProgress() {
        progress = KProgressHUD.create(this)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setCancellable(true)
            .setAnimationSpeed(2)
            .setDimAmount(0.5f)
            .show();
    }

    private fun hideProgress() {
        if (::progress.isInitialized && progress.isShowing) {
            progress.dismiss()
        }
    }
}