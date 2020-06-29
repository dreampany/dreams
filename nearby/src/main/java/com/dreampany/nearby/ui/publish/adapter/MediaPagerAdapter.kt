package com.dreampany.nearby.ui.publish.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.dreampany.framework.ui.adapter.BasePagerAdapter
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.nearby.R
import com.dreampany.nearby.data.enums.Action
import com.dreampany.nearby.data.enums.State
import com.dreampany.nearby.data.enums.Subtype
import com.dreampany.nearby.data.enums.Type
import com.dreampany.nearby.data.model.media.Apk
import com.dreampany.nearby.data.model.media.Image
import com.dreampany.nearby.ui.publish.fragment.ApksFragment
import com.dreampany.nearby.ui.publish.fragment.ImagesFragment

/**
 * Created by roman on 28/6/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class MediaPagerAdapter(activity: AppCompatActivity) : BasePagerAdapter<Fragment>(activity) {

    init {
        addItems()
    }

    private fun addItems() {
        val apk = UiTask(Type.APK, Subtype.DEFAULT, State.DEFAULT, Action.DEFAULT, null as Apk?)
        val image =
            UiTask(Type.IMAGE, Subtype.DEFAULT, State.DEFAULT, Action.DEFAULT, null as Image?)
        addItem(
            com.dreampany.framework.misc.exts.createFragment(
                ApksFragment::class,
                apk
            ),
            R.string.title_media_apk,
            true
        )
        addItem(
            com.dreampany.framework.misc.exts.createFragment(
                ImagesFragment::class,
                image
            ),
            R.string.title_media_image,
            true
        )
    }
}