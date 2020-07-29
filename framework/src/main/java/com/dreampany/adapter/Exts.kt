package com.dreampany.adapter

/**
 * Created by roman on 6/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
fun CharSequence?.equals(text: CharSequence?): Boolean {
    if (this == text) return true
    if (this.toString().equals(text.toString(), true)) return true
    return false
}

/*
val Boolean?.value: Boolean get() = this ?: false

val Int?.value: Int get() = this ?: 0*/
