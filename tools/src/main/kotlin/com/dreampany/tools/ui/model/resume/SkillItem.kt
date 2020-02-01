package com.dreampany.tools.ui.model.resume

import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.RecyclerView
import com.dreampany.framework.data.enums.Action
import com.dreampany.framework.data.model.Base
import com.dreampany.framework.misc.extension.setOnSafeClickListener
import com.dreampany.framework.ui.model.BaseItem
import com.dreampany.tools.R
import com.dreampany.tools.data.model.Skill
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.ui.adapter.resume.ResumeAdapter
import com.dreampany.tools.ui.adapter.resume.SkillAdapter
import com.dreampany.tools.ui.model.ServerItem
import com.google.android.material.textview.MaterialTextView
import com.google.common.base.Objects
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import java.io.Serializable

/**
 * Created by roman on 2020-01-15
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class SkillItem private constructor(
    item: Skill,
    @LayoutRes layoutId: Int = Constants.Default.INT
) : BaseItem<SkillItem.ViewHolder, Skill, String>(item, layoutId) {

    companion object {
        fun getItem(item: Skill): SkillItem {
            return SkillItem(item, R.layout.item_resume_skill)
        }
    }

    override fun hashCode(): Int {
        return Objects.hashCode(item.id)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as SkillItem
        return Objects.equal(this.item.id, item.item.id)
    }

    override fun createViewHolder(
        view: View,
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>
    ): ViewHolder {
        return ViewHolder(
            view,
            adapter
        )
    }

    override fun filter(constraint: String): Boolean {
        return false
    }

    class ViewHolder(view: View, adapter: FlexibleAdapter<*>) :
        BaseItem.ViewHolder(view, adapter) {

        private val adapter: SkillAdapter
        private val skill: MaterialTextView
        private val remove: AppCompatImageButton

        init {
            this.adapter = adapter as SkillAdapter
            skill = view.findViewById(R.id.text_skill)
            remove = view.findViewById(R.id.button_resume_skill_remove)

            remove.setOnSafeClickListener {
                this.adapter.uiItemClickListener?.onUiItemClick(
                    view = view,
                    item = this.adapter.getItem(adapterPosition)!!,
                    action = Action.DELETE
                )
            }
        }

        override fun <VH : BaseItem.ViewHolder, T : Base, S : Serializable, I : BaseItem<VH, T, S>>
                bind(position: Int, item: I) {
            val uiItem = item as SkillItem
            val item = uiItem.item
            skill.text = item.title
        }
    }
}