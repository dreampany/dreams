package com.dreampany.lockui.ui.adapter

import androidx.databinding.ViewDataBinding
import com.dreampany.common.misc.extension.gone
import com.dreampany.common.misc.extension.setOnSafeClickListener
import com.dreampany.common.misc.extension.visible
import com.dreampany.common.ui.adapter.BaseAdapter
import com.dreampany.lockui.R
import com.dreampany.lockui.databinding.ItemDeleteBinding
import com.dreampany.lockui.databinding.ItemNumberBinding
import com.dreampany.lockui.ui.model.Delete
import com.dreampany.lockui.ui.model.Item
import com.dreampany.lockui.ui.model.Number

/**
 * Created by roman on 3/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class LockAdapter(listener: Any? = null) : BaseAdapter<Item, LockAdapter.ViewHolder>(listener) {

    private val NUMBER = 0
    private val DELETE = 1

    private var pinLength = 0

    init {
        add(Number(1))
        add(Number(2))
        add(Number(3))

        add(Number(4))
        add(Number(5))
        add(Number(6))

        add(Number(7))
        add(Number(8))
        add(Number(9))

        add(Number(-1))
        add(Number(0))
        add(Delete(12))
    }

/*    override fun getViewType(item: Item): Int {
        return if (item is Number) NUMBER else DELETE
    }*/


    override fun layoutId(viewType: Int): Int {
        return if (viewType == NUMBER) R.layout.item_number else R.layout.item_delete
    }

    override fun createViewHolder(bind: ViewDataBinding, viewType: Int): ViewHolder {
        return if (viewType == NUMBER) NumberViewHolder(
            bind as ItemNumberBinding,
            this
        ) else DeleteViewHolder(
            bind as ItemDeleteBinding, this
        )
    }

    override fun filters(constraint: CharSequence): Boolean {
        return false
    }

    fun setPinLength(pinLength: Int) {

    }

    abstract class ViewHolder(bind: ViewDataBinding) :
        BaseAdapter.ViewHolder<Item, ViewHolder>(bind) {

    }

    class NumberViewHolder(private val bind: ItemNumberBinding, private val adapter: LockAdapter) :
        ViewHolder(bind) {

        init {
            bind.button.setOnSafeClickListener {
                adapter.getItem(adapterPosition)?.run {
                    adapter.listener?.onItemClick(this)
                }
            }
        }

        override fun bindView(item: Item, position: Int) {
            val number = item as Number
            if (number.number == -1) {
                bind.button.gone()
            } else {
                bind.button.text = number.number.toString()
                bind.button.visible()
            }
        }

    }

    class DeleteViewHolder(private val bind: ItemDeleteBinding, private val adapter: LockAdapter) :
        ViewHolder(bind) {

        init {
            bind.button.setOnSafeClickListener {
                adapter.getItem(adapterPosition)?.run {
                    adapter.listener?.onItemClick(this)
                }
            }
        }

        override fun bindView(item: Item, position: Int) {

        }

    }
}