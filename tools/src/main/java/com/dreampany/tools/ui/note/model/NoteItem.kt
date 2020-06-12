package com.dreampany.tools.ui.note.model

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dreampany.framework.misc.exts.format
import com.dreampany.tools.R
import com.dreampany.tools.data.model.note.Note
import com.dreampany.tools.databinding.NoteItemBinding
import com.dreampany.tools.misc.constant.NoteConstants
import com.google.common.base.Objects
import com.mikepenz.fastadapter.binding.ModelAbstractBindingItem

/**
 * Created by roman on 12/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class NoteItem
private constructor(
    val input: Note,
    var favorite: Boolean
) : ModelAbstractBindingItem<Note, NoteItemBinding>(input) {

    companion object {
        fun getItem(
            input: Note,
            favorite: Boolean = false
        ): NoteItem = NoteItem(input, favorite)
    }

    override fun hashCode(): Int = Objects.hashCode(input.id)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as NoteItem
        return Objects.equal(this.input.id, item.input.id)
    }

    override var identifier: Long = hashCode().toLong()

    override val type: Int = R.id.adapter_note_item_id

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): NoteItemBinding =
        NoteItemBinding.inflate(inflater, parent, false)


    override fun bindView(bind: NoteItemBinding, payloads: List<Any>) {
        bind.textTitle.text = input.title
        bind.textDescription.text = input.description
        bind.textDate.text = input.time.format(NoteConstants.Date.FORMAT_MONTH_DAY)
        bind.buttonFavorite.isLiked = favorite
    }

    override fun unbindView(binding: NoteItemBinding) {

    }

    val favoriteRes: Int get() = if (favorite) R.drawable.ic_favorite_black_24dp else R.drawable.ic_favorite_border_black_24dp
}