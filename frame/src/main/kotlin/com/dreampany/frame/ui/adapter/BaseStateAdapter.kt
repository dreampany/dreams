package com.dreampany.frame.ui.adapter

import android.os.Parcelable
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import android.util.SparseArray
import android.view.ViewGroup
import com.dreampany.frame.data.model.Task
import com.dreampany.frame.ui.fragment.BaseFragment
import com.dreampany.frame.util.DataUtil
import com.dreampany.frame.util.FragmentUtil

/**
 * Created by Hawladar Roman on 5/24/2018.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
abstract class BaseStateAdapter<T : BaseFragment> internal constructor(internal val manager: androidx.fragment.app.FragmentManager) : FragmentStatePagerAdapter(manager) {
    internal val fragments: SparseArray<T>
    internal val pageTitles: SparseArray<String>
    internal val pageClasses: SparseArray<Class<T>>
    internal val pageTasks: SparseArray<Task<*>>

    val currentFragments: List<T>?
        get() = if (fragments.size() > 0) {
            DataUtil.asList(fragments)

        } else null

    init {
        fragments = SparseArray()
        pageTitles = SparseArray()
        pageClasses = SparseArray()
        pageTasks = SparseArray()
    }

    override fun getCount(): Int {
        val titleSize = pageTitles.size()
        val classSize = pageClasses.size()
        return if (titleSize <= classSize) titleSize else classSize
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return pageTitles.get(position, null)
    }

    override fun getItemPosition(inFragment: Any): Int {
        val position = fragments.indexOfValue(inFragment as T?)
        return if (position >= 0) {
            position
        } else androidx.fragment.app.FragmentStatePagerAdapter.POSITION_NONE
    }

    override fun saveState(): Parcelable? {
        return null
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val fragment = super.instantiateItem(container, position) as T
        setFragment(position, fragment)
        return fragment
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        removePage(position)
        super.destroyItem(container, position, `object`)
    }

    internal fun getClazz(position: Int): Class<T> {
        return pageClasses.get(position, null)
    }

    fun addPage(pageTitle: String, pageClass: Class<T>, pageTask: Task<*>?): BaseStateAdapter<T> {
        if (contains(pageClass)) {
            return this
        }
        val index = count
        setPageTitle(index, pageTitle)
        setPageClass(index, pageClass)
        setPageTask(index, pageTask)
        notifyDataSetChanged()
        return this
    }

    operator fun contains(pageClass: Class<T>): Boolean {
        return pageClasses.indexOfValue(pageClass) >= 0
    }

    operator fun contains(title: String): Boolean {
        return pageTitles.indexOfValue(title) >= 0
    }

    fun removePage(position: Int): BaseStateAdapter<T> {
        return this
    }

    fun removeAll(): BaseStateAdapter<T> {
        if (count > 0) {
            fragments.clear()
            pageTitles.clear()
            pageClasses.clear()
            pageTasks.clear()
            notifyDataSetChanged()
        }
        return this
    }

    fun getFragment(position: Int): T? {
        return fragments.get(position, null)
    }

    private fun setFragment(position: Int, fragment: T): BaseStateAdapter<T> {
        fragments.put(position, fragment)
        return this
    }

    private fun setPageTitle(position: Int, pageTitle: String): BaseStateAdapter<T> {
        pageTitles.put(position, pageTitle)
        return this
    }

    private fun setPageClass(position: Int, pageClass: Class<T>): BaseStateAdapter<T> {
        pageClasses.put(position, pageClass)
        return this
    }

    private fun setPageTask(position: Int, pageTask: Task<*>?): BaseStateAdapter<T> {
        pageTasks.put(position, pageTask)
        return this
    }

    internal fun newFragment(position: Int): T {
        val clazz = pageClasses.get(position)
        val task = pageTasks.get(position)
        val fragment = FragmentUtil.newFragment(clazz, task)
        if (fragment != null) {
            fragment.retainInstance = true
        }
        return fragment
    }
}
