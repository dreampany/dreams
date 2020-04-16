package com.dreampany.common.ui.model

import com.dreampany.common.data.enums.BaseAction
import com.dreampany.common.data.enums.BaseState
import com.dreampany.common.data.enums.BaseSubtype
import com.dreampany.common.data.enums.BaseType
import com.dreampany.common.data.model.BaseParcel
import com.dreampany.common.data.model.Task
import com.dreampany.common.misc.constant.Constants
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 21/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
data class UiTask<
        T : BaseType,
        S : BaseSubtype,
        ST : BaseState,
        A : BaseAction,
        I : BaseParcel>(
    override var type: T,
    override var subtype: S,
    override var state: ST,
    override var action: A,
    override var input: I? = Constants.Default.NULL,
    override var inputs: List<I>? = Constants.Default.NULL,
    override var id: String? = Constants.Default.NULL,
    override var ids: List<String>? = Constants.Default.NULL,
    override var extra: String? = Constants.Default.NULL,
    override var extras: List<String>? = Constants.Default.NULL,
    override var notify: Boolean = Constants.Default.BOOLEAN,
    override var fullscreen: Boolean = Constants.Default.BOOLEAN,
    override var collapseToolbar: Boolean = Constants.Default.BOOLEAN
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
    notify,
    fullscreen,
    collapseToolbar
)