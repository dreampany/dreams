package com.dreampany.word.injector.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dreampany.frame.misc.ViewModelKey
import com.dreampany.frame.vm.factory.ViewModelFactory
import com.dreampany.word.vm.*
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Singleton


/**
 * Created by Hawladar Roman on 5/31/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MoreViewModel::class)
    abstract fun bindMoreViewModel(vm: MoreViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoaderViewModel::class)
    abstract fun bindLoaderViewModel(vm: LoaderViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(WordViewModel::class)
    abstract fun bindWordViewModel(vm: WordViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RecentViewModel::class)
    abstract fun bindRecentViewModel(vm: RecentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TodayViewModel::class)
    abstract fun bindTodayViewModel(vm: TodayViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RecentsViewModel::class)
    abstract fun bindRecentsViewModel(vm: RecentsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(OcrViewModel::class)
    abstract fun bindOcrViewModel(vm: OcrViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TextOcrViewModel::class)
    abstract fun bindTextOcrViewModel(vm: TextOcrViewModel): ViewModel

    @Singleton
    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}