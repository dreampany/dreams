package com.dreampany.framework.misc.exts

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import com.dreampany.framework.data.model.Task
import com.dreampany.framework.misc.constant.Constant
import kotlin.reflect.KClass

/**
 * Created by roman on 18/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */

fun <T : Any> Fragment?.open(target: KClass<T>, finishCurrent: Boolean = false) {
    this?.run {
        startActivity(Intent(activity, target.java))
        if (finishCurrent) {
            activity?.finish()
        }
    }
}

fun <T : Any> Fragment?.open(
    target: KClass<T>,
    task: Task<*, *, *, *, *>,
    finishCurrent: Boolean = false
) {
    this?.run {
        val intent = Intent(activity, target.java)
        intent.putExtra(Constant.Keys.TASK, task as Parcelable)
        startActivity(intent)
        if (finishCurrent) {
            activity?.finish()
        }
    }
}

fun <T : Any> Fragment?.open(
    target: KClass<T>,
    task: Task<*, *, *, *, *>,
    requestCode: Int,
    finishCurrent: Boolean = false
) {
    this?.run {
        val intent = Intent(activity, target.java)
        intent.putExtra(Constant.Keys.TASK, task as Parcelable)
        startActivityForResult(intent, requestCode)
        if (finishCurrent) {
            activity?.finish()
        }
    }
}

fun <T : Fragment> createFragment(clazz: KClass<T>, task: Task<*, *, *, *, *>): T {
    val instance = clazz.java.newInstance()
    if (instance.arguments == null) {
        val bundle = Bundle()
        bundle.putParcelable(Constant.Keys.TASK, task)
        instance.arguments = bundle
    } else {
        instance.arguments?.putParcelable(Constant.Keys.TASK, task)
    }
    return instance
}

val Fragment?.bundle: Bundle?
    get() = this?.arguments

val Fragment?.task: Task<*, *, *, *, *>?
    get() {
        val bundle = this.bundle ?: return null
        return bundle.getParcelable<Parcelable>(Constant.Keys.TASK) as Task<*, *, *, *, *>?
    }

fun <T : View> Fragment?.findViewById(@IdRes id: Int): T? = this?.view?.findViewById(id)

@ColorInt
fun Fragment?.color(@ColorRes resId: Int): Int =
    this?.requireContext().color(resId)