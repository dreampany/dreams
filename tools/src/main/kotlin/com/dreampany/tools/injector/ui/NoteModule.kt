package com.dreampany.tools.injector.ui
import com.dreampany.framework.misc.FragmentScope
import com.dreampany.tools.ui.fragment.note.FavoriteNotesFragment
import com.dreampany.tools.ui.fragment.note.NoteFragment
import com.dreampany.tools.ui.fragment.note.NoteHomeFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


/**
 * Created by Hawladar Roman on 5/25/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */

@Module
abstract class NoteModule {
    @FragmentScope
    @ContributesAndroidInjector
    abstract fun homeFragment(): NoteHomeFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun noteFragment(): NoteFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun favoritesFragment(): FavoriteNotesFragment
}