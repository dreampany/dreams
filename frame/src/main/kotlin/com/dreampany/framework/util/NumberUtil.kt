package com.dreampany.framework.util

import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil
import timber.log.Timber
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*


/**
 * Created by roman on 2019-10-14
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class NumberUtil {
    companion object {
        private val suffixes = TreeMap<Long, String>()
        private val random = Random()

        init {
            suffixes[1_000L] = "K"
            suffixes[1_000_000L] = "M"
            suffixes[1_000_000_000L] = "G"
            suffixes[1_000_000_000_000L] = "T"
            suffixes[1_000_000_000_000_000L] = "P"
            suffixes[1_000_000_000_000_000_000L] = "E"
        }

        fun toNumInUnits(bytes: Long): String {
            var bytes = bytes
            var u = 0
            while (bytes > 1024 * 1024) {
                u++
                bytes = bytes shr 10
            }
            if (bytes > 1024)
                u++
            return String.format(Locale.ENGLISH, "%.1f %cB", bytes / 1024f, " kMGTPE"[u])
        }

        fun readableFileSize(size: Long): String {
            if (size <= 0) return "0"
            val units = arrayOf("B", "kB", "MB", "GB", "TB")
            val digitGroups = (Math.log10(size.toDouble()) / Math.log10(1024.0)).toInt()
            return DecimalFormat("#,##0.#").format(
                size / Math.pow(
                    1024.0,
                    digitGroups.toDouble()
                )
            ) + " " + units[digitGroups]
        }

        fun formatCount(count: Long): String {
            //Long.MIN_VALUE == -Long.MIN_VALUE so we need an adjustment here
            if (count == java.lang.Long.MIN_VALUE) return formatCount(java.lang.Long.MIN_VALUE + 1)
            if (count < 0) return "-" + formatCount(-count)
            if (count < 1000) return java.lang.Long.toString(count) //deal with easy case

            val e = suffixes.floorEntry(count)
            val divideBy = e!!.key
            val suffix = e.value

            val truncated = count / (divideBy!! / 10) //the number part of the output times 10
            val hasDecimal = truncated < 100 && truncated / 10.0 != (truncated / 10).toDouble()
            return if (hasDecimal) (truncated / 10.0).toString() + suffix else (truncated / 10).toString() + suffix
        }

        fun format(number: Long): String {
            //  Locale.setDefault(new Locale("pl", "PL"));
            return String.format(Locale.getDefault(), "%,d", number)
        }

        fun nextRand(upper: Int): Int {
            return if (upper <= 0) -1 else random.nextInt(upper)
        }

        fun nextRand(upper: Long): Long {
            return if (upper <= 0) -1 else random.nextInt(upper.toInt()).toLong()
        }

        fun nextRand(min: Int, max: Int): Int {
            return random.nextInt(max - min + 1) + min
        }

        fun getPercentage(current: Long, total: Long): Int {
            val ratio = current.toFloat() / total

            var percentage = (ratio * 100).toInt()

            if (percentage > 100) {
                percentage = 100
            }

            return percentage
        }

        fun round(value: Double, places: Int): Double {
            require(places >= 0)

            var bd = BigDecimal(value)
            bd = bd.setScale(places, RoundingMode.HALF_UP)
            return bd.toDouble()
        }

        fun percentage(current: Int, total: Int): Int {
            return current * 100 / total
        }

        fun parseInt(value: Any?, defaultValue: Int): Int {
            return parseInt(value?.toString(), defaultValue)
        }

        fun parseInt(value: String?, defaultValue: Int): Int {
            try {
                return value?.toInt() ?: defaultValue
            } catch (e: NumberFormatException) {
                return defaultValue
            }
        }

        fun randomBool(): Boolean {
            return random.nextBoolean()
        }

        fun isValidPhoneNumber(countryCode: String, number: String): Boolean {
            val phoneUtil = PhoneNumberUtil.getInstance()
            try {
                val phone = phoneUtil.parse(number, countryCode)
                return phoneUtil.isValidNumber(phone)
            } catch (error: NumberParseException) {
                Timber.e(error)
            }
            return false
        }
    }
}