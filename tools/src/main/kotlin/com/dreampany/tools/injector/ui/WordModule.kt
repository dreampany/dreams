package com.dreampany.tools.injector.ui
import com.dreampany.frame.misc.FragmentScope
import com.dreampany.tools.ui.fragment.EditNoteFragment
import com.dreampany.tools.ui.fragment.NoteHomeFragment
import com.dreampany.tools.ui.fragment.WordHomeFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


/**
 * Created by Hawladar Roman on 5/25/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */

@Module
abstract class WordModule {
    @FragmentScope
    @ContributesAndroidInjector
    abstract fun wordHomeFragment(): WordHomeFragment
}