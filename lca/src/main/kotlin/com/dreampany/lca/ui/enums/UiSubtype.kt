package com.dreampany.lca.ui.enums

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Roman-372 on 2/13/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
enum class UiSubtype : Parcelable {
    EDIT, VIEW, FAVORITES, ALERT, SETTINGS, LICENSE, ABOUT
}