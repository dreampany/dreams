package com.dreampany.tools.ui.model

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.dreampany.frame.data.model.Base
import com.dreampany.frame.ui.model.BaseItem
import com.dreampany.frame.util.AndroidUtil
import com.dreampany.frame.util.DataUtilKt
import com.dreampany.tools.R
import com.dreampany.tools.data.enums.ApkType
import com.dreampany.tools.data.model.Apk
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.ui.adapter.ApkAdapter
import com.dreampany.tools.ui.enums.UiAction
import com.github.nikartm.button.FitButton
import de.hdodenhof.circleimageview.CircleImageView
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import jp.shts.android.library.TriangleLabelView
import java.io.Serializable
import java.lang.ref.WeakReference


/**
 * Created by roman on 2019-08-03
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class ApkItem private constructor(
    item: Apk, @LayoutRes layoutId: Int = Constants.Default.INT
) : BaseItem<Apk, ApkItem.ViewHolder, String>(item, layoutId) {

    companion object {
        fun getItem(item: Apk): ApkItem {
            return ApkItem(item, R.layout.item_apk)
        }
    }

    private var icon: WeakReference<Drawable>? = null

    fun getIcon(context: Context): Drawable? {
        if (icon == null || icon!!.get() == null) {
            var drawable = AndroidUtil.getApplicationIcon(context, item.id)
            if (drawable == null) {
                drawable = ContextCompat.getDrawable(context, R.drawable.ic_android_black_24dp)
            }
            icon = WeakReference(drawable!!)
        }
        return icon!!.get()
    }

    override fun createViewHolder(
        view: View,
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>
    ): ViewHolder {
        return ViewHolder(view, adapter)
    }

    override fun filter(constraint: String?): Boolean {
        return false
    }

    class ViewHolder(
        view: View,
        adapter: FlexibleAdapter<*>
    ) : BaseItem.ViewHolder(view, adapter) {

        private val height: Int

        private var adapter: ApkAdapter
        private var imageIcon: CircleImageView
        private var textName: AppCompatTextView
        private var textSize: AppCompatTextView
        private var buttonOpen: FitButton
        private var buttonDetails: FitButton
        private var labelType: TriangleLabelView

        init {
            this.adapter = adapter as ApkAdapter
            height = getSpanHeight(this.adapter.getSpanCount(), this.adapter.getItemOffset())

            imageIcon = view.findViewById(R.id.image_icon)
            textName = view.findViewById(R.id.text_name)
            textSize = view.findViewById(R.id.text_size)
            buttonOpen = view.findViewById(R.id.button_open)
            buttonDetails = view.findViewById(R.id.button_details)
            labelType = view.findViewById(R.id.label_type)

            view.layoutParams.height = height


/*            view.setOnClickListener {
                this.adapter.click?.onClick(this.adapter.getItem(adapterPosition)!!)
            }
            view.setOnLongClickListener {
                this.adapter.click?.onClick(this.adapter.getItem(adapterPosition)!!)
                true
            }*/

            buttonOpen.setOnClickListener {
                this.adapter.click?.onClick(item = this.adapter.getItem(adapterPosition), action = UiAction.OPEN)
            }

            buttonDetails.setOnClickListener {
                this.adapter.click?.onClick(item = this.adapter.getItem(adapterPosition), action = UiAction.DETAILS)
            }
        }

        override fun <VH : BaseItem.ViewHolder, T : Base, S : Serializable, I : BaseItem<T, VH, S>> bind(
            position: Int,
            item: I
        ) {
            val uiItem = item as ApkItem
            val item = uiItem.item
            val drawlable = uiItem.getIcon(getContext())
            if (drawlable != null) {
                imageIcon.setImageDrawable(drawlable)
            } else {
                imageIcon.setImageResource(R.drawable.ic_android_black_24dp)
            }

            textName.text = item.name
            textSize.text = DataUtilKt.formatReadableSize(item.size)
            labelType.primaryText = item.apkType.name

            when (item.apkType) {
                ApkType.SYSTEM -> {
                    labelType.setTriangleBackgroundColorResource(R.color.material_red500)
                }
                ApkType.USER -> {
                    labelType.setTriangleBackgroundColorResource(R.color.material_green500)
                }
            }
        }
    }
}