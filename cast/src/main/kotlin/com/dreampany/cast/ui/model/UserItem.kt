package com.dreampany.cast.ui.model

import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.dreampany.cast.R
import com.dreampany.cast.data.model.User
import com.dreampany.frame.ui.model.BaseItem
import com.google.common.base.Objects
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import kotlinx.android.synthetic.main.item_user.view.*
import java.io.Serializable

/**
 * Created by Roman-372 on 6/27/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class UserItem : BaseItem<User, UserItem.ViewHolder> {

    constructor(item: User, @LayoutRes layoutId: Int) : super(item, layoutId) {

    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as UserItem
        return Objects.equal(item.getItem(), getItem())
    }

    override fun createViewHolder(
        view: View,
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>
    ): ViewHolder {
        return UserViewHolder(view, adapter)
    }

    override fun bindViewHolder(
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>,
        holder: ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {

    }

    override fun filter(constraint: Serializable): Boolean {
        return item.name.toLowerCase().startsWith((constraint as String).toLowerCase())
    }

    companion object {
        fun getUser(item: User): UserItem {
            return UserItem(item, R.layout.item_user)
        }
    }

    abstract class ViewHolder : BaseItem.ViewHolder {

        constructor(
            view: View,
            adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>
        ) : super(view, adapter) {

        }
    }

    class UserViewHolder : ViewHolder {

        lateinit var name : TextView

        constructor(
            view: View,
            adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>
        ) : super(view, adapter) {
            name = view.text_name
        }
    }
}