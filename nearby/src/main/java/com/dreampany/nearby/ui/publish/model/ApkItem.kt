package com.dreampany.nearby.ui.publish.model

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dreampany.nearby.R
import com.dreampany.nearby.data.model.media.Apk
import com.dreampany.nearby.databinding.ApkItemBinding
import com.google.common.base.Objects
import com.mikepenz.fastadapter.binding.ModelAbstractBindingItem

/**
 * Created by roman on 12/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class ApkItem(
    val input: Apk,
    var favorite: Boolean = false
) : ModelAbstractBindingItem<Apk, ApkItemBinding>(input) {

    override fun hashCode(): Int = input.hashCode()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as ApkItem
        return Objects.equal(this.input.id, item.input.id)
    }

    override var identifier: Long = hashCode().toLong()

    override val type: Int
        get() = R.id.adapter_apk_item_id

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): ApkItemBinding =
        ApkItemBinding.inflate(inflater, parent, false)

    override fun bindView(bind: ApkItemBinding, payloads: List<Any>) {
        /*bind.name.text = input.name

        val statusRes = if (live) R.drawable.ic_status_live_24 else R.drawable.ic_status_dead_24
        bind.status.setRes(statusRes)*/
    }

    override fun unbindView(binding: ApkItemBinding) {

    }
}