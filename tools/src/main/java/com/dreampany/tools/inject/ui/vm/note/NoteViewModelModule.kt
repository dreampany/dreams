package com.dreampany.tools.inject.ui.vm.note

import androidx.lifecycle.ViewModel
import com.dreampany.framework.inject.annote.ViewModelKey
import com.dreampany.tools.ui.crypto.vm.CoinViewModel
import com.dreampany.tools.ui.crypto.vm.ExchangeViewModel
import com.dreampany.tools.ui.crypto.vm.TradeViewModel
import com.dreampany.tools.ui.note.vm.NoteViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by roman on 18/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module
abstract class NoteViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(NoteViewModel::class)
    abstract fun bindNote(vm: NoteViewModel): ViewModel
}