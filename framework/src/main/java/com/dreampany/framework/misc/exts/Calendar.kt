package com.dreampany.framework.misc.exts

import timber.log.Timber
import java.util.*

/**
 * Created by roman on 28/9/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
val Calendar.dayOfWeek: Int get() = get(Calendar.DAY_OF_WEEK)

val Calendar.date: Int get() = get(Calendar.DATE)

val Calendar.weekOfYear: Int get() = get(Calendar.WEEK_OF_YEAR)

val Calendar.dateOfYear: Int get() = get(Calendar.DAY_OF_YEAR)

val Calendar.week: Int get() = get(Calendar.WEEK_OF_MONTH)

val Calendar.dayOfMonth: Int get() = this.get(Calendar.DAY_OF_MONTH)
val Calendar.month: Int get() = get(Calendar.MONTH).inc()
val Calendar.year: Int get() = get(Calendar.YEAR)

val Calendar.age: Int
    get() {
        val current = Calendar.getInstance()
        var currentDay = current.dayOfMonth
        var currentMonth = current.month
        var currentYear = current.year

        val birth = this
        var birthDay = birth.dayOfMonth
        var birthMonth = birth.month
        var birthYear = birth.year

        val currentDate = current.time
        val birthDate = time


        if (birthDate.after(currentDate)) {
            return -1
        }
        val months = arrayOf(
            31, 28, 31, 30, 31, 30,
            31, 31, 30, 31, 30, 31
        )

        if (birthDay > currentDay) {
            currentDay = currentDay + months.get(birthMonth.dec())
            currentMonth = currentMonth.dec()
        }

        if (birthMonth > currentMonth) {
            currentYear = currentYear.dec()
            currentMonth = currentMonth + 12
        }

        val day = currentDay - birthDay
        val month = currentMonth - birthMonth
        val year = currentYear - birthYear
        return year
    }

fun Calendar.isLastWeek(week: Int): Boolean {
    val clone = Calendar.getInstance()
    clone.timeInMillis = this.timeInMillis
    clone.set(Calendar.DAY_OF_WEEK_IN_MONTH, week + 1)
    return clone.month > this.month
}

fun Calendar.withinByDate(calendar: Calendar): Boolean {
    if (this.year > calendar.year) return false
    if (this.year < calendar.year) return true
    if (this.month < calendar.month) return true
    return this.date <= calendar.date
}

fun Calendar.withinByDay(calendar: Calendar): Boolean {
    if (this.year > calendar.year) return false
    if (this.year < calendar.year) return true
    return this.dateOfYear <= calendar.dateOfYear
}

fun Calendar.withinByWeek(calendar: Calendar): Boolean {
    if (this.year > calendar.year) return false
    if (this.year < calendar.year) return true
    return this.weekOfYear <= calendar.weekOfYear
}

fun Calendar.withinByMonth(calendar: Calendar): Boolean {
    if (this.year > calendar.year) return false
    if (this.year < calendar.year) return true
    return this.month <= calendar.month
}

fun Calendar.withinByYear(calendar: Calendar): Boolean = this.year <= calendar.year


fun Calendar.update(
    year: Int,
    month: Int = Calendar.JANUARY,
    week: Int = 1,
    dayOfWeek: Int = Calendar.SUNDAY
): Calendar {
    set(Calendar.YEAR, year)
    set(Calendar.MONTH, month)
    set(Calendar.DAY_OF_WEEK_IN_MONTH, week)
    set(Calendar.DAY_OF_WEEK, dayOfWeek)
    return this
}

fun Calendar.update(
    year: Int,
    month: Int = Calendar.JANUARY,
    dayOfMonth: Int = 1
): Calendar {
    set(Calendar.YEAR, year)
    set(Calendar.MONTH, month)
    set(Calendar.DAY_OF_MONTH, dayOfMonth)
    return this
}

fun Calendar.isWeekDay(): Boolean {
    when (dayOfWeek) {
        Calendar.SATURDAY,
        Calendar.SUNDAY -> return false
        else -> return true
    }
}

/*fun Calendar.update(
    year: Int,
    month: Int = Calendar.JANUARY,
    week: Int = 1,
    dayOfWeek: Int = Calendar.SUNDAY
) {
    set(Calendar.HOUR_OF_DAY, 0)
    set(Calendar.MINUTE, 0)
    set(Calendar.SECOND, 0)

    set(Calendar.MONTH, month)
    set(Calendar.YEAR, year)
    //set(Calendar.DAY_OF_WEEK_IN_MONTH, week)
    //set(Calendar.DAY_OF_WEEK, day)
    set(Calendar.DAY_OF_MONTH, 1)

    var dayDiff: Int = dayOfWeek - get(Calendar.DAY_OF_WEEK)
    if (dayDiff < 0) {
        dayDiff += 7
    }
    dayDiff += 7 * (week - 1)

    add(Calendar.DATE, dayDiff)
}*/


fun Calendar.print() {
    Timber.v(
        "%d/%d/%d",
        year,
        month + 1,
        date
    )
}