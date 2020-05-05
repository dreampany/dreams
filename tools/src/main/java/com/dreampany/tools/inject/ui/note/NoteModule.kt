package com.dreampany.tools.inject.ui.note

import com.dreampany.tools.ui.note.activity.NoteActivity
import com.dreampany.tools.ui.note.activity.NotesActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by roman on 13/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module
abstract class NoteModule {
    @ContributesAndroidInjector
    abstract fun notes(): NotesActivity

    @ContributesAndroidInjector
    abstract fun note(): NoteActivity
}