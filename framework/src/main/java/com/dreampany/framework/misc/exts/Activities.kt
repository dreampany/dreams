package com.dreampany.framework.misc.exts

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.provider.Settings
import android.view.inputmethod.InputMethodManager
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.dreampany.framework.data.model.Task
import com.dreampany.framework.misc.constant.Constant
import com.dreampany.framework.misc.func.Executors
import kotlinx.coroutines.Runnable
import timber.log.Timber
import kotlin.reflect.KClass

/**
 * Created by roman on 17/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
fun Activity?.alive(): Boolean {
    if (this == null) return false
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
        return !(isFinishing() || isDestroyed())
    return !isFinishing()
}

fun Activity?.open(action: String, requestCode: Int) {
    this?.run {
        val intent = Intent(action)
        startActivityForResult(intent, requestCode)
    }
}

fun <T : Any> Activity?.open(target: KClass<T>, finish: Boolean = false) {
    this?.run {
        startActivity(Intent(this, target.java))
        if (finish) {
            finish()
        }
    }
}

fun <T : Any> Activity?.open(
    target: KClass<T>,
    task: Task<*, *, *, *, *>,
    finish: Boolean = false
) {
    this?.run {
        val intent = Intent(this, target.java)
        intent.putExtra(Constant.Keys.TASK, task as Parcelable)
        startActivity(intent)
        if (finish) {
            finish()
        }
    }
}

fun <T : Any> Activity?.open(target: KClass<T>, task: Task<*, *, *, *, *>, requestCode: Int) {
    this?.run {
        val intent = Intent(this, target.java)
        intent.putExtra(Constant.Keys.TASK, task as Parcelable)
        startActivityForResult(intent, requestCode)
    }
}

fun <T : Activity> Activity?.open(target: KClass<T>, flags: Int, finish: Boolean = false) {
    this?.run {
        startActivity(Intent(this, target.java).addFlags(flags))
        if (finish) {
            finish()
        }
    }
}

fun <T : Any> Activity?.open(
    target: KClass<T>,
    finishCurrent: Boolean = false,
    clearTask: Boolean = false
) {
    this?.run {
        if (clearTask) {
            val intent = Intent(this, target.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        } else {
            startActivity(Intent(this, target.java))
            if (finishCurrent) {
                finish()
            }
        }

    }
}

fun Activity?.close(task: Task<*, *, *, *, *>? = null) {
    this?.run {
        val intent = Intent()
        task?.let { intent.putExtra(Constant.Keys.TASK, it as Parcelable) }
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}

val Activity?.clearFlags: Int
    get() = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK

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

val Intent?.bundle: Bundle? get() = this?.extras

val Activity?.bundle: Bundle? get() = this?.intent?.bundle

val Bundle?.task: Task<*, *, *, *, *>?
    get() = this?.getParcelable<Parcelable>(Constant.Keys.TASK) as Task<*, *, *, *, *>?

val Intent?.task: Task<*, *, *, *, *>?
    get() = this.bundle.task

val Activity?.task: Task<*, *, *, *, *>?
    get() = this.bundle?.getParcelable<Parcelable>(Constant.Keys.TASK) as Task<*, *, *, *, *>?

fun Activity?.moreApps(devId: String) {
    if (this == null) return
    try {
        val intent = Intent()
        val uri = Uri.parse("market://search?q=pub:$devId")
        val market = Intent(Intent.ACTION_VIEW, uri)
        this.startActivity(market)
    } catch (error: ActivityNotFoundException) {
        Timber.e(error)
    }
}

fun Activity?.rateUs() {
    if (this == null) return
    try {
        val id: String = this.packageName
        val uri = Uri.parse("market://details?id=$id")
        val market = Intent(Intent.ACTION_VIEW, uri)
        this.startActivity(market)
    } catch (error: ActivityNotFoundException) {
        Timber.e(error)
    }
}

fun Activity?.hideKeyboard() {
    val view = this?.currentFocus ?: return
    Runnable {
        val imm: InputMethodManager = (this.getSystemService(Context.INPUT_METHOD_SERVICE)
            ?: return@Runnable) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }.run()
}

fun Activity?.openAppSettings(finishCurrent: Boolean = false) {
    this?.run {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri: Uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivity(intent)
        if (finishCurrent) {
            finish()
        }
    }
}


