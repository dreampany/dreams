package com.dreampany.tools.injector.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dreampany.framework.misc.ViewModelKey
import com.dreampany.framework.ui.vm.factory.ViewModelFactory
import com.dreampany.tools.ui.vm.*
import com.dreampany.tools.ui.vm.note.NoteViewModel
import com.dreampany.tools.ui.vm.question.QuestionViewModel
import com.dreampany.tools.ui.vm.radio.StationViewModel
import com.dreampany.tools.ui.vm.resume.ResumeViewModel
import com.dreampany.tools.ui.vm.vpn.CountryViewModel
import com.dreampany.tools.ui.vm.vpn.ServerViewModel
import com.dreampany.tools.ui.vm.word.QuizViewModel
import com.dreampany.tools.ui.vm.word.RelatedQuizViewModel
import com.dreampany.tools.ui.vm.word.WordViewModel
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
    @ViewModelKey(DemoViewModel::class)
    abstract fun bindDemoViewModel(vm: DemoViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FeatureViewModel::class)
    abstract fun bindFeatureViewModel(vm: FeatureViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AppViewModel::class)
    abstract fun bindAppViewModel(vm: AppViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NoteViewModel::class)
    abstract fun bindNoteViewModel(vm: NoteViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(WordViewModel::class)
    abstract fun bindWordViewModel(vm: WordViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoaderViewModel::class)
    abstract fun bindLoaderViewModel(vm: LoaderViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(QuizViewModel::class)
    abstract fun bindQuizViewModel(vm: QuizViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RelatedQuizViewModel::class)
    abstract fun bindRelatedQuizViewModel(vm: RelatedQuizViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ServerViewModel::class)
    abstract fun bindServerViewModel(vm: ServerViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CountryViewModel::class)
    abstract fun bindCountryViewModel(vm: CountryViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(StationViewModel::class)
    abstract fun bindStationViewModel(vm: StationViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CoinViewModel::class)
    abstract fun bindCoinViewModel(vm: CoinViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ContactViewModel::class)
    abstract fun bindContactViewModel(vm: ContactViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ResumeViewModel::class)
    abstract fun bindResumeViewModel(vm: ResumeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(QuestionViewModel::class)
    abstract fun bindQuestionViewModel(vm: QuestionViewModel): ViewModel

    @Singleton
    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}