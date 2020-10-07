package com.dreampany.hello.ui.auth.activity

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.lifecycle.Observer
import com.dreampany.framework.data.model.Response
import com.dreampany.framework.misc.exts.*
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.framework.ui.activity.InjectActivity
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.hello.R
import com.dreampany.hello.data.enums.*
import com.dreampany.hello.data.model.Auth
import com.dreampany.hello.data.model.User
import com.dreampany.hello.databinding.AuthInfoActivityBinding
import com.dreampany.hello.misc.Constants
import com.dreampany.hello.ui.auth.fragment.BirthdayFragment
import com.dreampany.hello.ui.vm.AuthViewModel
import com.dreampany.hello.ui.vm.UserViewModel
import timber.log.Timber
import java.util.*

/**
 * Created by roman on 27/9/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class AuthInfoActivity : InjectActivity(), DatePickerDialog.OnDateSetListener {

    private lateinit var bind: AuthInfoActivityBinding
    private lateinit var authVm: AuthViewModel
    private lateinit var userVm: UserViewModel
    private lateinit var input: User
    private lateinit var birthdayCalendar: Calendar
    private lateinit var gender: Gender

    override val homeUp: Boolean = true
    override val layoutRes: Int = R.layout.auth_info_activity
    override val toolbarId: Int = R.id.toolbar

    override fun onStartUi(state: Bundle?) {
        val task = (task ?: return) as UiTask<Type, Subtype, State, Action, User>
        input = task.input ?: return
        initUi()
    }

    override fun onStopUi() {
    }

    override fun onDateSet(picker: DatePicker, year: Int, month: Int, dayOfMonth: Int) {
        updateUi(year, month, dayOfMonth)
    }

    private fun initUi() {
        if (::bind.isInitialized) return
        bind = getBinding()
        authVm = createVm(AuthViewModel::class)
        userVm = createVm(UserViewModel::class)
        authVm.subscribe(this, { this.processAuthResponse(it) })
        userVm.subscribe(this, { this.processUserResponse(it) })

        bind.inputEmail.setText(input.email)

        bind.layoutBirthday.setOnSafeClickListener {
            openBirthdayPicker()
        }

        bind.male.setOnSafeClickListener {
            updateUi(Gender.MALE)
        }

        bind.female.setOnSafeClickListener {
            updateUi(Gender.FEMALE)
        }

        bind.other.setOnSafeClickListener {
            updateUi(Gender.OTHER)
        }

        bind.register.setOnSafeClickListener {
            register()
        }
    }

    private fun openBirthdayPicker() {
        val picker = BirthdayFragment()
        picker.show(this)
    }

    private fun processAuthResponse(response: Response<Type, Subtype, State, Action, Auth>) {
        if (response is Response.Progress) {
            //bind.swipe.refresh(response.progress)
        } else if (response is Response.Error) {
            processError(response.error)
        } else if (response is Response.Result<Type, Subtype, State, Action, Auth>) {
            Timber.v("Result [%s]", response.result)
            processResult(response.result, response.state)
        }
    }

    private fun processUserResponse(response: Response<Type, Subtype, State, Action, User>) {
        if (response is Response.Progress) {
            //bind.swipe.refresh(response.progress)
        } else if (response is Response.Error) {
            processError(response.error)
        } else if (response is Response.Result<Type, Subtype, State, Action, User>) {
            Timber.v("Result [%s]", response.result)
            processResult(response.result)
        }
    }

    private fun processError(error: SmartError) {
        val titleRes = if (error.hostError) R.string.title_no_internet else R.string.title_error
        val message =
            if (error.hostError) getString(R.string.message_no_internet) else error.message
        showDialogue(
            titleRes,
            messageRes = R.string.message_unknown,
            message = message,
            onPositiveClick = {

            },
            onNegativeClick = {

            }
        )
    }

    private fun processResult(result: Auth?, state: State) {

    }

    private fun processResult(result: User?) {

    }

    private fun updateUi(year: Int, month: Int, dayOfMonth: Int) {
        if (::birthdayCalendar.isInitialized.not()) {
            birthdayCalendar = Calendar.getInstance()
        }
        birthdayCalendar.update(year, month, dayOfMonth)
        val date = birthdayCalendar.format(Constants.Pattern.YY_MM_DD)
        bind.birthday.text = date
    }

    private fun updateUi(gender: Gender) {
        this.gender = gender

        bind.male.setBackgroundColor(color(R.color.colorTransparent))
        bind.male.setTextColor(color(R.color.textColorPrimary))
        bind.female.setBackgroundColor(color(R.color.colorTransparent))
        bind.female.setTextColor(color(R.color.textColorPrimary))
        bind.other.setBackgroundColor(color(R.color.colorTransparent))
        bind.other.setTextColor(color(R.color.textColorPrimary))

        when (gender) {
            Gender.MALE -> {
                bind.male.setBackgroundColor(color(R.color.colorAccent))
                bind.male.setTextColor(color(R.color.white))
            }
            Gender.FEMALE -> {
                bind.female.setBackgroundColor(color(R.color.colorAccent))
                bind.female.setTextColor(color(R.color.white))
            }
            Gender.OTHER -> {
                bind.other.setBackgroundColor(color(R.color.colorAccent))
                bind.other.setTextColor(color(R.color.white))
            }
        }
    }

    private fun register() {
        val email = bind.inputEmail.trimValue
        var valid = true
        if (!email.isEmail) {
            valid = false
            bind.layoutEmail.error = getString(R.string.error_email)
        }
        if (::birthdayCalendar.isInitialized.not()) {
            valid = false
            //todo birthday error
        }
        val birth = Date().compareTo(birthdayCalendar.time)
        if (birth < 0) {
            valid = false
            //todo birthday error
        }
        if (::gender.isInitialized.not()) {
            valid = false
            //todo gender error
        }
        if (valid.not()) return
        //vm.read(email, password)
    }
}