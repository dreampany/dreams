package com.dreampany.tools.data.enums.wifi

import androidx.annotation.DrawableRes
import com.dreampany.tools.R
import com.dreampany.tools.misc.constants.WifiConstants
import java.util.*

/**
 * Created by roman on 1/8/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
enum class Security(@DrawableRes val res: Int, val extra: String? = null) {
    NONE(R.drawable.ic_baseline_lock_open_24),
    WPS(R.drawable.ic_lock_outline),
    WEP(R.drawable.ic_lock_outline),
    WPA(R.drawable.ic_baseline_lock_24),
    WPA2(R.drawable.ic_baseline_lock_24),
    WPA3(R.drawable.ic_baseline_lock_24, WifiConstants.Keys.RSN);

    companion object {

        fun findAll(capabilities: String?): Set<Security>? {
            val result: List<Security>? = parse(capabilities)?.mapNotNull { capability ->
                try {
                    Security.valueOf(capability)
                } catch (error: Throwable) {
                    Security.values().find { it.extra == capability }
                }
            }
            return if (result.isNullOrEmpty()) null else TreeSet(result)
        }

        fun findOne(capabilities: String?) : Security = findAll(capabilities)?.firstOrNull() ?: NONE

        private fun parse(capabilities: String?): List<String>? {
            if (capabilities.isNullOrEmpty()) return null
            return capabilities
                .toUpperCase(Locale.getDefault())
                .replace("][", "-")
                .replace("]", "")
                .replace("[", "")
                .split("-".toRegex()).toList()
        }
    }
}