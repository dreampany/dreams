package com.dreampany.framework.ui.model

import com.dreampany.framework.data.enums.BaseAction
import com.dreampany.framework.data.enums.BaseState
import com.dreampany.framework.data.enums.BaseSubtype
import com.dreampany.framework.data.enums.BaseType
import com.dreampany.framework.data.model.BaseParcel
import com.dreampany.framework.data.model.Task
import com.dreampany.framework.misc.constant.Constant
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 21/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
data class UiTask<T : BaseType, S : BaseSubtype, ST : BaseState, A : BaseAction, I : BaseParcel>(
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
) : Task<T, S, ST, A, I>(
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