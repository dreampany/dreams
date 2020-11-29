package com.dreampany.tube.ui.dialog

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.dreampany.framework.inject.annote.FragmentScope
import com.dreampany.framework.misc.exts.arraysOf
import com.dreampany.framework.misc.exts.setOnSafeClickListener
import com.dreampany.framework.ui.fragment.InjectDialogFragment
import com.dreampany.tube.R
import com.dreampany.tube.data.source.pref.Prefs
import com.dreampany.tube.databinding.FilterDialogBinding
import com.google.common.collect.Maps
import okhttp3.internal.indexOf
import timber.log.Timber
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
    private lateinit var orders: MutableMap<String, String>

    override val layoutRes: Int = R.layout.filter_dialog

    override fun onStartUi(state: Bundle?) {
        initUi()
        initOrderUi()
    }

    override fun onStopUi() {
    }

    private fun initUi() {
        if (::bind.isInitialized) return
        bind = binding()

        bind.cancel.setOnSafeClickListener {
            hide()
        }

        bind.apply.setOnSafeClickListener {
            apply()
            hide()
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

    private fun initOrderUi() {
        orders = Maps.newHashMap()
        val ordersLabels = context.arraysOf(R.array.array_order_labels)
        val ordersValues = context.arraysOf(R.array.array_order_values)
        val selectedIndex = ordersValues.indexOf(pref.order)
        for (index in ordersLabels.indices) {
            orders.put(ordersLabels[index], ordersValues[index])
        }

        val adpt = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            ordersLabels
        )
        adpt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        with(bind.orders) {
            adapter = adpt
            setSelection(selectedIndex, false)
        }
    }

    private fun apply() {
        val orderLabel = bind.orders.selectedItem.toString()
        val orderValue = orders.get(orderLabel) ?: return
        pref.commitOrder(orderValue)
    }
}