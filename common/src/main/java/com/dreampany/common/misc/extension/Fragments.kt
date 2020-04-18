package com.dreampany.common.misc.extension

import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import com.dreampany.common.data.model.Task
import com.dreampany.common.misc.constant.Constants
import kotlin.reflect.KClass

/**
 * Created by roman on 18/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
fun <T : Fragment> createFragment(clazz: KClass<T>, task: Task<*, *, *, *, *>): T {
    val instance = clazz.java.newInstance()
    if (instance.arguments == null) {
        val bundle = Bundle()
        bundle.putParcelable(Constants.Keys.TASK, task)
        instance.arguments = bundle
    } else {
        instance.arguments?.putParcelable(Constants.Keys.TASK, task)
    }
    return instance
}

val Fragment?.bundle: Bundle?
    get() = this?.arguments

val Fragment?.task: Task<*, *, *, *, *>?
    get() {
        val bundle = this.bundle ?: return null
        return bundle.getParcelable<Parcelable>(Constants.Keys.TASK) as Task<*, *, *, *, *>?
    }