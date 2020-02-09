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
import com.dreampany.tools.data.model.Experience
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.ui.adapter.resume.ExperienceAdapter
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
class ExperienceItem private constructor(
    item: Experience,
    @LayoutRes layoutId: Int = Constants.Default.INT
) : BaseItem<ExperienceItem.ViewHolder, Experience, String>(item, layoutId) {

    companion object {
        fun getItem(item: Experience): ExperienceItem {
            return ExperienceItem(item, R.layout.item_resume_experience)
        }
    }

    override fun hashCode(): Int {
        return Objects.hashCode(item.id)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as ExperienceItem
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

        private val adapter: ExperienceAdapter
        private val remove: AppCompatImageButton
        private val company: MaterialTextView
        private val location: MaterialTextView
        private val designation: MaterialTextView
        private val description: MaterialTextView

        init {
            this.adapter = adapter as ExperienceAdapter
            remove = view.findViewById(R.id.button_remove)
            company = view.findViewById(R.id.text_company)
            location = view.findViewById(R.id.text_location)
            designation = view.findViewById(R.id.text_designation)
            description = view.findViewById(R.id.text_description)

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
            val uiItem = item as ExperienceItem
            val item = uiItem.item
            company.text = item.company
            location.text = item.location
            designation.text = item.designation
            description.text = item.description
        }
    }
}