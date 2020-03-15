package com.dreampany.pair.ui.auth.activity

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
import com.dreampany.pair.databinding.LoginActivityBinding
import com.dreampany.pair.ui.auth.vm.AuthViewModel
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by roman on 3/12/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class LoginActivity : BaseInjectorActivity() {

    @Inject
    internal lateinit var factory: ViewModelFactory

    private lateinit var bind: LoginActivityBinding
    private lateinit var vm: AuthViewModel

    override fun hasBinding(): Boolean = true

    @LayoutRes
    override fun layoutId(): Int = R.layout.login_activity

    @IdRes
    override fun toolbarId(): Int = R.id.toolbar

    override fun homeUp(): Boolean = true

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
        vm = ViewModelProvider(this, factory).get(AuthViewModel::class.java)

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
        vm.register(email, password, name)
    }

    private fun processResponse(response: Response<User, Type, Subtype>) {
        if (response is Response.Progress) {
            if (response.progress) showProgress() else hideProgress()
        } else if (response is Response.Error) {
            processError(response.error)
            //vm.processFailure(state = result.state, action = result.action, error = result.error)
        } else if (response is Response.Result<User, Type, Subtype>) {
            Timber.v("Result [%s]", response.result.email)
            processResult(response.result)
        }
    }

    private fun processError(error: Throwable) {
        if (error.cause is FirebaseAuthUserCollisionException) {
            showDialogue(
                R.string.title_dialog_registration,
                R.string.message_dialog_account_already_used,
                onPositiveClick = {

                },
                onNegativeClick = {

                }
            )
        }
    }

    private fun processResult(user: User) {

    }

}