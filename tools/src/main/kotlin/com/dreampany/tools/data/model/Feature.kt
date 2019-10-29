package com.dreampany.tools.data.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.dreampany.framework.data.enums.State
import com.dreampany.framework.data.enums.Subtype
import com.dreampany.framework.data.enums.Type
import com.dreampany.framework.data.model.Base
import com.dreampany.framework.util.TimeUtilKt
import com.dreampany.tools.misc.Constants
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 2019-08-03
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
@Entity(
    indices = [Index(
        value = [Constants.Feature.ID],
        unique = true
    )],
    primaryKeys = [Constants.Feature.ID]
)
data class Feature(
    override var time: Long = Constants.Default.LONG,
    override var id: String = Constants.Default.STRING, // package
    var type: Type = Type.DEFAULT,
    var subtype: Subtype = Subtype.DEFAULT,
    var state: State = State.DEFAULT,
    var title: String? = Constants.Default.NULL,
    var subtitle: String? = Constants.Default.NULL
) : Base() {

    @Ignore
    constructor() : this(time = TimeUtilKt.currentMillis()) {

    }

    constructor(id: String) : this(time = TimeUtilKt.currentMillis(), id = id) {

    }

    constructor(id: String, type: Type) : this(
        time = TimeUtilKt.currentMillis(),
        id = id,
        type = type
    ) {

    }

    constructor(
        type: Type = Type.DEFAULT,
        subtype: Subtype = Subtype.DEFAULT,
        state: State = State.DEFAULT,
        title: String? = Constants.Default.NULL
    ) : this(
        id = type.name + subtype.name + state.name,
        type = type,
        subtype = subtype,
        state = state,
        title = title
    ) {

    }


}