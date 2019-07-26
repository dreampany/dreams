package com.dreampany.frame.util

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.View


/**
 * Created by Roman-372 on 7/26/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class ViewUtilKt {
    companion object {
        fun createDatePicker(
            context: Context,
            listener: DatePickerDialog.OnDateSetListener?,
            day: Int,
            month: Int,
            year: Int
        ) : DatePickerDialog {
            val dialog = object : DatePickerDialog(context, listener, year, month, day) {
                override fun onCreate(savedInstanceState: Bundle) {
                    super.onCreate(savedInstanceState)
                    val year = context.resources.getIdentifier("android:id/year", null, null)
                    if (year != 0) {
                        val yearPicker = findViewById<View>(year)
                        yearPicker?.setVisibility(View.GONE)
                    }
                }
            }
            return dialog
        }
    }
}