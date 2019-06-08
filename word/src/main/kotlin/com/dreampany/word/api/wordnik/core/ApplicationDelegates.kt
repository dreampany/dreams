package com.dreampany.word.api.wordnik.core

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Created by roman on 2019-06-08
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
object ApplicationDelegates {

    fun <T> setOnce(defaultValue: T? = null): ReadWriteProperty<Any?, T> = SetOnce(defaultValue)

    private class SetOnce<T>(defaultValue: T? = null) : ReadWriteProperty<Any?, T> {
        private var isSet = false
        private var value: T? = defaultValue

        override fun getValue(thisRef: Any?, property: KProperty<*>): T {
            return value ?: throw IllegalStateException("${property.name} not initialized")
        }

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) = synchronized(this) {
            if (!isSet) {
                this.value = value
                isSet = true
            }
        }
    }
}