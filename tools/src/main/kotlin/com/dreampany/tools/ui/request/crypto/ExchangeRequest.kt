package com.dreampany.tools.ui.request.crypto

import com.dreampany.framework.data.enums.*
import com.dreampany.framework.data.model.Request
import com.dreampany.tools.data.model.crypto.Coin
import com.dreampany.tools.misc.Constants

/**
 * Created by roman on 2019-11-15
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class ExchangeRequest(
    type: Type = Type.DEFAULT,
    subtype: Subtype = Subtype.DEFAULT,
    state: State = State.DEFAULT,
    action: Action = Action.DEFAULT,
    source: Source = Source.DEFAULT,
    single: Boolean = Constants.Default.BOOLEAN,
    important: Boolean = Constants.Default.BOOLEAN,
    progress: Boolean = Constants.Default.BOOLEAN,
    start: Long = Constants.Default.LONG,
    limit: Long = Constants.Default.LONG,
    id: String = Constants.Default.STRING,
    ids: List<String>? = Constants.Default.NULL,
    input: Coin? = Constants.Default.NULL,
    inputs: List<Coin>? = Constants.Default.NULL,
    var extraParams: String = Constants.Default.STRING,
    var fromSymbol: String = Constants.Default.STRING,
    var toSymbol: String = Constants.Default.STRING
) : Request<Coin>(
    type = type,
    subtype = subtype,
    state = state,
    action = action,
    source = source,
    single = single,
    important = important,
    progress = progress,
    start = start,
    limit = limit,
    id = id,
    ids = ids,
    input = input,
    inputs = inputs
) {

}