package com.dreampany.hello.ui.auth.fragment

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.dreampany.framework.R
import com.dreampany.framework.misc.exts.dayOfMonth
import com.dreampany.framework.misc.exts.month
import com.dreampany.framework.misc.exts.year
import java.util.*

/**
 * Created by roman on 28/9/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class BirthdayFragment : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.Theme_Dialog)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val year = calendar.year
        val month = calendar.month
        val dayOfMonth = calendar.dayOfMonth
        val activity = requireActivity()
        return DatePickerDialog(
            activity,
            R.style.Theme_DatePicker,
            activity as DatePickerDialog.OnDateSetListener,
            year,
            month,
            dayOfMonth
        )
    }

    fun show(activity: AppCompatActivity) {
        show(activity.supportFragmentManager, activity.toString())
    }

    fun show(fragment: Fragment) {
        show(fragment.childFragmentManager, fragment.tag)
    }

    fun hide() {
        dismissAllowingStateLoss()
    }
}