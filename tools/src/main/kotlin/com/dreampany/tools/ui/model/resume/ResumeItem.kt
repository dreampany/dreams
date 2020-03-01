package com.dreampany.tools.ui.model.resume

import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.dreampany.framework.data.enums.Action
import com.dreampany.framework.data.model.Base
import com.dreampany.framework.data.model.Color
import com.dreampany.framework.misc.extension.setOnSafeClickListener
import com.dreampany.framework.misc.extension.toColor
import com.dreampany.framework.ui.model.BaseItem
import com.dreampany.framework.util.ColorUtil
import com.dreampany.framework.util.TimeUtilKt
import com.dreampany.tools.R
import com.dreampany.tools.data.model.resume.Resume
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.ui.adapter.resume.ResumeAdapter
import com.google.common.base.Objects
import com.like.LikeButton
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import java.io.Serializable

/**
 * Created by roman on 2020-01-11
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class ResumeItem
private constructor(
    item: Resume,
    @LayoutRes layoutId: Int = Constants.Default.INT
) : BaseItem<ResumeItem.ViewHolder, Resume, String>(item, layoutId) {


    val color: Color

    init {
        color = ColorUtil.createShadowWhiteColor()
    }

    val skills: ArrayList<SkillItem> = arrayListOf()
    val experiences: ArrayList<ExperienceItem> = arrayListOf()
    val projects: ArrayList<ProjectItem> = arrayListOf()
    val schools: ArrayList<SchoolItem> = arrayListOf()

    companion object {
        fun getItem(item: Resume): ResumeItem {
            return ResumeItem(
                item,
                R.layout.item_resume
            )
        }
    }

    override fun hashCode(): Int {
        return Objects.hashCode(item.id)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as ResumeItem
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

        private val adapter: ResumeAdapter
        private val layoutRoot: CardView
        private val textName: AppCompatTextView
        private val textDesignation: AppCompatTextView
        private val textDate: AppCompatTextView
        private val buttonEdit: AppCompatImageButton
        private val buttonFavorite: LikeButton

        init {
            this.adapter = adapter as ResumeAdapter

            layoutRoot = view.findViewById(R.id.layout_root)
            textName = view.findViewById(R.id.text_market)
            textDesignation = view.findViewById(R.id.text_designation)
            textDate = view.findViewById(R.id.text_date)
            buttonEdit = view.findViewById(R.id.button_edit)
            buttonFavorite = view.findViewById(R.id.button_favorite)

            view.setOnSafeClickListener {
                this.adapter.uiItemClickListener?.onUiItemClick(
                    view = view,
                    item = this.adapter.getItem(adapterPosition)!!,
                    action = Action.PREVIEW
                )
            }
            buttonEdit.setOnSafeClickListener {
                this.adapter.uiItemClickListener?.onUiItemClick(
                    view = view,
                    item = this.adapter.getItem(adapterPosition)!!,
                    action = Action.EDIT
                )
            }
            buttonFavorite.setOnSafeClickListener {
                this.adapter.uiItemClickListener?.onUiItemClick(
                    view = view,
                    item = this.adapter.getItem(adapterPosition)!!,
                    action = Action.FAVORITE
                )
            }
        }

        override fun <VH : BaseItem.ViewHolder, T : Base, S : Serializable, I : BaseItem<VH,T,  S>>
                bind(position: Int, item: I) {
            val uiItem = item as ResumeItem
            val item = uiItem.item

            textName.text = item.profile?.name
            textDesignation.text = item.profile?.designation
            textDate.text = TimeUtilKt.getDate(item.time, Constants.Date.FORMAT_MONTH_DAY)
            buttonFavorite.isLiked = uiItem.favorite

            layoutRoot.setCardBackgroundColor(uiItem.color.primaryId.toColor(context))

        }
    }
}