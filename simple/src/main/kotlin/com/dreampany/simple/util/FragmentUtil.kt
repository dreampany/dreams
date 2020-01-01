package com.dreampany.simple.util

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.dreampany.simple.data.model.Task
import com.dreampany.simple.misc.AppExecutor
import com.dreampany.simple.misc.Constants
import com.dreampany.simple.ui.activity.BaseActivity

/**
 * Created by roman on 2020-01-01
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class FragmentUtil {

    companion object {
        fun <T : Fragment> commitFragment(
            ex: AppExecutor,
            activity: BaseActivity,
            fragment: T?,
            parentId: Int
        ): T? {

            val commitRunnable = Runnable {
                if (activity.isDestroyed || activity.isFinishing) {
                    return@Runnable
                }
                fragment?.run {
                    activity.supportFragmentManager
                        .beginTransaction()
                        //.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                        .replace(parentId, this, javaClass.simpleName)
                        .addToBackStack(null)
                        .commitAllowingStateLoss()
                }

            }

            ex.postToUi(commitRunnable)
            return fragment
        }

        fun <T : Fragment> getFragment(activity: AppCompatActivity, fragmentClass: Class<T>): T? {

            var fragment: T? = getFragmentByTag(activity, fragmentClass.name)

            if (fragment == null /*&& task != null*/) {
                fragment = newFragment(fragmentClass)
                if (fragment != null) {
                    val bundle = Bundle()
                    //bundle.putSerializable(Task.class.getName(), task);
                    fragment.arguments = bundle
                }
            } /*else if (task != null) {
            if (task instanceof Parcelable) {
                fragment.getArguments().putParcelable(Task.class.getName(), (Parcelable) task);
            } else if (task instanceof Serializable) {
                fragment.getArguments().putSerializable(Task.class.getName(), task);
            }
        }*/

            return fragment
        }

        fun <F : Fragment> getSupportFragment(
            parent: FragmentManager,
            fragmentClass: Class<F>,
            tag: String,
            task: Task<*>?
        ): F? {

            var fragment: F? = getFragmentByTag(parent, tag)

            if (fragment == null) {
                fragment = newFragment(fragmentClass)
                if (fragment != null && task != null) {
                    val bundle = Bundle()
                    bundle.putSerializable(Constants.Task.TASK, task)
                    fragment.arguments = bundle
                }
            } else if (task != null) {
                fragment.arguments!!.putParcelable(Constants.Task.TASK, task)
            }

            return fragment
        }

        fun <T : Fragment> getFragmentByTag(activity: AppCompatActivity, fragmentTag: String): T? {
            return activity.supportFragmentManager.findFragmentByTag(fragmentTag) as T?
        }

        fun <T : Fragment> getFragmentByTag(manager: FragmentManager, fragmentTag: String): T? {
            return manager.findFragmentByTag(fragmentTag) as T?
        }

        fun <T : Fragment> getFragment(parent: Fragment, @IdRes fragmentId: Int): T? {
            return parent.childFragmentManager.findFragmentById(fragmentId) as T?
        }

        fun <T : Fragment> newFragment(fragmentClass: Class<T>, task: Task<*>?): T? {
            val fragment = newFragment(fragmentClass)
            if (fragment != null && task != null) {
                val bundle = Bundle()
                bundle.putSerializable(Constants.Task.TASK, task)
                fragment.arguments = bundle
            }
            return fragment
        }

        fun <T : Fragment> newFragment(fragmentClass: Class<T>): T? {
            try {
                return fragmentClass.newInstance()
            } catch (ignored: Exception) {
                return null
            }

        }
    }
}