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
    override var input: I? = Constant.Default.NULL,
    override var inputs: List<I>? = Constant.Default.NULL,
    override var id: String? = Constant.Default.NULL,
    override var ids: List<String>? = Constant.Default.NULL,
    override var extra: String? = Constant.Default.NULL,
    override var extras: List<String>? = Constant.Default.NULL,
    override var url: String? = Constant.Default.NULL,
    override var notify: Boolean = Constant.Default.BOOLEAN,
    override var fullscreen: Boolean = Constant.Default.BOOLEAN,
    override var collapseToolbar: Boolean = Constant.Default.BOOLEAN
) : Task<T, ST, S, A, I>(
    type,
    subtype,
    state,
    action,
    input,
    inputs,
    id,
    ids,
    extra,
    extras,
    url,
    notify,
    fullscreen,
    collapseToolbar
)