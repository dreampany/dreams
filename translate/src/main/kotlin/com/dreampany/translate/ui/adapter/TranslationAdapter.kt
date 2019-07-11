package com.dreampany.translate.ui.adapter

import com.dreampany.frame.ui.adapter.SmartAdapterKt
import com.dreampany.translate.ui.model.TranslationItem
import com.dreampany.translation.data.model.Translation

/**
 * Created by Roman-372 on 7/11/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class TranslationAdapter(listener: Any?) :
    SmartAdapterKt<TranslationItem<Translation, TranslationItem.ViewHolder, String>>(listener) {
}