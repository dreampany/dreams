package com.dreampany.lca.misc

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Roman-372 on 7/30/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class MonthSlashDayFormatter : ValueFormatter() {

    override fun getFormattedValue(value: Float, axis: AxisBase): String {
        val date = Date(value.toLong())
        val sdf = SimpleDateFormat("MM/dd", Locale.ENGLISH)
        return sdf.format(date)
    }
}