package com.dreampany.framework.api.ui.fab

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.annotation.AttrRes
import androidx.annotation.RequiresApi
import androidx.annotation.StyleRes
import androidx.coordinatorlayout.widget.CoordinatorLayout

/**
 * Created by roman on 2019-11-02
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
//@CoordinatorLayout.AttachedBehavior(FabButton.Behavior::class.java)
class FabButton : FrameLayout {

    constructor(context: Context) : this(context, null) {

    }

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0) {

    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        @AttrRes defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        init(context, attrs, defStyleAttr)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context,
        attrs: AttributeSet?,
        @AttrRes defStyleAttr: Int,
        @StyleRes defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context, attrs, defStyleAttr)
    }

    fun init(
        context: Context,
        attrs: AttributeSet?,
        @AttrRes defStyleAttr: Int
    ) {

    }

    class Behavior : CoordinatorLayout.Behavior<FabButton> {

        constructor() {}

        constructor(context: Context, attrs: AttributeSet) {

        }
    }
}