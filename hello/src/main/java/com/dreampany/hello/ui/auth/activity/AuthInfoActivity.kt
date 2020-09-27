package com.dreampany.hello.ui.auth.activity

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.DatePicker
import com.dreampany.framework.misc.exts.setOnSafeClickListener
import com.dreampany.framework.misc.exts.task
import com.dreampany.framework.ui.activity.InjectActivity
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.hello.R
import com.dreampany.hello.data.enums.Action
import com.dreampany.hello.data.enums.State
import com.dreampany.hello.data.enums.Subtype
import com.dreampany.hello.data.enums.Type
import com.dreampany.hello.data.model.User
import com.dreampany.hello.databinding.AuthInfoActivityBinding
import com.dreampany.hello.ui.auth.fragment.BirthdayFragment
import com.dreampany.hello.ui.vm.UserViewModel

/**
 * Created by roman on 27/9/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class AuthInfoActivity : InjectActivity(), DatePickerDialog.OnDateSetListener {

    override val homeUp: Boolean = true
    override val layoutRes: Int = R.layout.auth_info_activity
    override val toolbarId: Int = R.id.toolbar

    private lateinit var bind: AuthInfoActivityBinding
    private lateinit var vm: UserViewModel
    private lateinit var input: User

    override fun onStartUi(state: Bundle?) {
        val task = (task ?: return) as UiTask<Type, Subtype, State, Action, User>
        input = task.input ?: return
        initUi()
    }

    override fun onStopUi() {
    }

    override fun onDateSet(picker: DatePicker, year: Int, month: Int, dayOfMonth: Int) {

    }

    private fun initUi() {
        if (::bind.isInitialized) return
        bind = getBinding()
        vm = createVm(UserViewModel::class)

        bind.layoutBirthday.setOnSafeClickListener {
            openBirthdayPicker()
        }
    }

    private fun openBirthdayPicker() {
        val picker = BirthdayFragment()
        picker.show(this)
    }


}