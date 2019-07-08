package com.dreampany.frame.data.model

/**
 * Created by Roman-372 on 7/4/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class BaseKt : BaseParcelKt() {
    abstract var id: String?
    abstract var time: Long

   /* fun getId(): String? {
        return id
    }

    fun getTime(): Long {
        return time
    }

    fun setId(id: String?) {
        this.id = id
    }

    fun setTime(time: Long) {
        this.time = time
    }*/
}