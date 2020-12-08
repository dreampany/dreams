package com.dreampany.hello.ui.auth.activity

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.DatePicker
import com.dreampany.framework.data.model.Response
import com.dreampany.framework.misc.exts.*
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.framework.ui.activity.InjectActivity
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.hello.R
import com.dreampany.hello.data.enums.*
import com.dreampany.hello.data.model.Auth
import com.dreampany.hello.data.source.pref.Pref
import com.dreampany.hello.databinding.AuthInfoActivityBinding
import com.dreampany.hello.misc.Constants
import com.dreampany.hello.misc.active
import com.dreampany.hello.misc.inactive
import com.dreampany.hello.misc.isValidAge
import com.dreampany.hello.ui.auth.fragment.BirthdayFragment
import com.dreampany.hello.ui.vm.AuthViewModel
import timber.log.Timber
import java.util.*
import javax.inject.Inject

/**
 * Created by roman on 27/9/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class AuthInfoActivity : InjectActivity(), DatePickerDialog.OnDateSetListener {

    @Inject
    internal lateinit var pref: Pref

    private lateinit var bind: AuthInfoActivityBinding
    private lateinit var vm: AuthViewModel
    private lateinit var input: Auth

    private lateinit var birthday: Calendar

    override val homeUp: Boolean = true
    override val layoutRes: Int = R.layout.auth_info_activity
    override val toolbarId: Int = R.id.toolbar

    override fun onStartUi(state: Bundle?) {
        val task = (task ?: return) as UiTask<Type, Subtype, State, Action, Auth>
        input = task.input ?: return
        initUi()
    }

    override fun onStopUi() {
    }

    override fun onDateSet(picker: DatePicker, year: Int, month: Int, dayOfMonth: Int) {
        updateUi(year, month, dayOfMonth)
        updateUi()
    }

    private fun initUi() {
        if (::bind.isInitialized) return
        bind = binding()
        vm = createVm(AuthViewModel::class)
        vm.subscribe(this, { this.process(it) })

        bind.inputUsername.setText(input.username)
        bind.inputEmail.setText(input.email)

        birthday = Calendar.getInstance()
        if (input.birthday.isEmpty.not()) {
            birthday.timeInMillis = input.birthday
        }
        input.gender?.let {
            updateUi(it)
        }

        bind.layoutBirthday.setOnSafeClickListener {
            openBirthdayPicker()
        }

        bind.male.setOnSafeClickListener {
            updateUi(Gender.MALE)
            updateUi()
        }

        bind.female.setOnSafeClickListener {
            updateUi(Gender.FEMALE)
            updateUi()
        }

        bind.other.setOnSafeClickListener {
            updateUi(Gender.OTHER)
            updateUi()
        }

        bind.register.setOnSafeClickListener {
            register()
        }
    }

    private fun openBirthdayPicker() {
        val picker = BirthdayFragment(birthday)
        picker.show(this)
    }

    private fun updateUi(year: Int, month: Int, dayOfMonth: Int) {
        birthday.update(year, month, dayOfMonth)
        val date = birthday.format(Constants.Pattern.YY_MM_DD)
        bind.birthday.text = date
    }

    private fun updateUi(gender: Gender) {
        if (input.gender == gender) return
        input.gender = gender

        bind.male.setBackgroundColor(color(R.color.colorTransparent))
        bind.male.setTextColor(color(R.color.textColorPrimary))
        bind.female.setBackgroundColor(color(R.color.colorTransparent))
        bind.female.setTextColor(color(R.color.textColorPrimary))
        bind.other.setBackgroundColor(color(R.color.colorTransparent))
        bind.other.setTextColor(color(R.color.textColorPrimary))

        when (gender) {
            Gender.MALE -> {
                bind.male.setBackgroundColor(color(R.color.colorAccent))
                bind.male.setTextColor(color(R.color.material_white))
            }
            Gender.FEMALE -> {
                bind.female.setBackgroundColor(color(R.color.colorAccent))
                bind.female.setTextColor(color(R.color.material_white))
            }
            Gender.OTHER -> {
                bind.other.setBackgroundColor(color(R.color.colorAccent))
                bind.other.setTextColor(color(R.color.material_white))
            }
        }
    }

    private fun updateUi() {
        if (bind.inputUsername.isEmpty && bind.inputEmail.isEmpty) {
            bind.register.inactive()
            return
        }
        if (birthday.isValidAge.not()) {
            bind.register.inactive()
            return
        }
        if (input.gender == null) {
            bind.register.inactive()
            return
        }
        bind.register.active()
    }

    private fun register() {
        val email = bind.inputEmail.trimValue
        var valid = true
        if (email.isEmail.not()) {
            valid = false
            bind.layoutEmail.error = getString(R.string.error_email)
        }
        if (birthday.isValidAge.not()) {
            valid = false
            //todo birthday error
        }
        if (input.gender == null) {
            valid = false
            //todo gender error
        }
        val country = bind.countryPicker.selectedCountryCode
        if (country.isNullOrEmpty()) {
            valid = false
            //todo country error
        }
        if (valid.not()) return
        input.email = email
        input.country = country
        vm.write(input)
    }

    private fun process(response: Response<Type, Subtype, State, Action, Auth>) {
        if (response is Response.Progress) {
            progress(response.progress)
        } else if (response is Response.Error) {
            process(response.error)
        } else if (response is Response.Result<Type, Subtype, State, Action, Auth>) {
            Timber.v("Result [%s]", response.result)
            process(response.result, response.state)
        }
    }

    private fun process(error: SmartError) {
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

    private fun process(result: Auth?, state: State) {

    }
}