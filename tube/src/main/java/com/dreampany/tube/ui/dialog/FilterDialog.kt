package com.dreampany.tube.ui.dialog

import android.os.Bundle
import android.widget.ArrayAdapter
import com.dreampany.framework.inject.annote.FragmentScope
import com.dreampany.framework.misc.exts.arraysOf
import com.dreampany.framework.ui.fragment.InjectDialogFragment
import com.dreampany.tube.R
import com.dreampany.tube.data.source.pref.Prefs
import com.dreampany.tube.databinding.FilterDialogBinding
import javax.inject.Inject

/**
 * Created by roman on 11/28/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@FragmentScope
class FilterDialog
@Inject constructor() : InjectDialogFragment() {

    @Inject
    internal lateinit var pref: Prefs

    private lateinit var bind: FilterDialogBinding

    override val layoutRes: Int = R.layout.filter_dialog

    override fun onStartUi(state: Bundle?) {
        initUi()
    }

    override fun onStopUi() {
    }

    private fun initUi() {
        if (::bind.isInitialized) return
        bind = binding()

        val ordersLabels = context.arraysOf(R.array.array_order_labels)
        val ordersValues = context.arraysOf(R.array.array_order_values)
        val adpt = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            ordersLabels
        )
        adpt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        with(bind.orders)
        {
            adapter = adpt
        }

        /*with(mySpinner)
        {
            adapter = aa
            setSelection(0, false)
            onItemSelectedListener = this@MainActivity
            prompt = "Select your favourite language"
            gravity = Gravity.CENTER

        }*/
    }
}