package com.dreampany.common.misc.extension

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.dreampany.common.misc.func.Executors
import com.dreampany.common.ui.activity.BaseActivity
import com.dreampany.common.ui.fragment.BaseFragment
import kotlinx.coroutines.Runnable
import kotlin.reflect.KClass

/**
 * Created by roman on 17/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
fun Activity?.alive() : Boolean {
    if (this == null) return false
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
        return !(isFinishing() || isDestroyed())
    return !isFinishing()
}

fun <T : Any> Activity?.open(target: KClass<T>, finishCurrent: Boolean = false) {
    this?.run {
        startActivity(Intent(this, target.java))
        if (finishCurrent) {
            finish()
        }
    }
}

fun <T : Activity> Activity?.open(target: KClass<T>, flags: Int, finishCurrent: Boolean = false) {
    this?.run {
        startActivity(Intent(this, target.java).addFlags(flags))
        if (finishCurrent) {
            finish()
        }
    }
}

fun Activity?.clearFlags(
): Int =
    Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK

fun <T : Fragment> AppCompatActivity?.fragment(tag: String?): T? =
    this?.supportFragmentManager?.findFragmentByTag(tag) as T?

fun <T : Fragment> AppCompatActivity?.open(fragment: T?, @IdRes parent: Int, ex: Executors) {
    val runner = Runnable {
        this?.run {
            if (isDestroyed || isFinishing) return@Runnable
            fragment?.let {
                supportFragmentManager
                    .beginTransaction()
                    .replace(parent, it, it.javaClass.simpleName)
                    .addToBackStack(null)
                    .commitAllowingStateLoss()
            }
        }
    }
    ex.postToUi(runner)
}