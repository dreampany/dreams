package com.dreampany.common.ui.model

import com.dreampany.common.data.enums.BaseEnum
import com.dreampany.common.data.model.BaseParcel
import com.dreampany.common.data.model.Task
import com.dreampany.common.misc.constant.Constant
import kotlinx.parcelize.Parcelize

/**
 * Created by roman on 7/10/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
@Parcelize
data class UiTask<T : BaseEnum, ST : BaseEnum, S : BaseEnum, A : BaseEnum, I : BaseParcel>(
    override var type: T,
    override var subtype: S,
    override var state: ST,
    override var action: A,
    override var input: I? = Constant.Default.NULL
) : Task<T, ST, S, A, I>(type, subtype, state, action, input) {
}