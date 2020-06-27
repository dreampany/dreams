package com.dreampany.nearby.ui.home.model

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dreampany.nearby.R
import com.dreampany.nearby.data.model.User
import com.dreampany.nearby.databinding.UserItemBinding
import com.google.common.base.Objects
import com.mikepenz.fastadapter.binding.ModelAbstractBindingItem

/**
 * Created by roman on 12/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class UserItem(
    val input: User,
    var live : Boolean = false,
    var favorite: Boolean = false
) : ModelAbstractBindingItem<User, UserItemBinding>(input) {

    override fun hashCode(): Int = input.hashCode()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as UserItem
        return Objects.equal(this.input.id, item.input.id)
    }

    override var identifier: Long = hashCode().toLong()

    override val type: Int
        get() = R.id.adapter_user_item_id

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): UserItemBinding =
        UserItemBinding.inflate(inflater, parent, false)

    override fun bindView(bind: UserItemBinding, payloads: List<Any>) {
        //bind.icon.setImageResource(item.iconRes)
        bind.name.text = input.name
    }

    override fun unbindView(binding: UserItemBinding) {

    }
}