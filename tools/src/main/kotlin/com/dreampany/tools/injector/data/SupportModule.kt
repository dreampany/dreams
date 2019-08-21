package com.dreampany.tools.injector.data

import com.dreampany.tools.data.model.Demo
import com.dreampany.frame.misc.SmartCache
import com.dreampany.frame.misc.SmartMap
import com.dreampany.tools.data.model.App
import com.dreampany.tools.data.model.Note
import com.dreampany.tools.data.model.Word
import com.dreampany.tools.misc.*
import com.dreampany.tools.ui.model.AppItem
import com.dreampany.tools.ui.model.NoteItem
import com.dreampany.tools.ui.model.WordItem
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


/**
 * Created by Hawladar Roman on 7/11/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Module
class SupportModule {

    @Singleton
    @Provides
    @DemoAnnote
    fun provideDemoSmartMap(): SmartMap<String, Demo> {
        return SmartMap.newMap()
    }

    @Singleton
    @Provides
    @DemoAnnote
    fun provideDemoSmartCache(): SmartCache<String, Demo> {
        return SmartCache.newCache()
    }

    @Singleton
    @Provides
    @AppAnnote
    fun provideAppSmartMap(): SmartMap<String, App> {
        return SmartMap.newMap()
    }

    @Singleton
    @Provides
    @AppAnnote
    fun provideAppSmartCache(): SmartCache<String, App> {
        return SmartCache.newCache()
    }

    @Singleton
    @Provides
    @AppItemAnnote
    fun provideAppItemSmartMap(): SmartMap<String, AppItem> {
        return SmartMap.newMap()
    }

    @Singleton
    @Provides
    @AppItemAnnote
    fun provideAppItemSmartCache(): SmartCache<String, AppItem> {
        return SmartCache.newCache()
    }

    @Singleton
    @Provides
    @NoteAnnote
    fun provideNoteSmartMap(): SmartMap<String, Note> {
        return SmartMap.newMap()
    }

    @Singleton
    @Provides
    @NoteAnnote
    fun provideNoteSmartCache(): SmartCache<String, Note> {
        return SmartCache.newCache()
    }

    @Singleton
    @Provides
    @NoteItemAnnote
    fun provideNoteItemSmartMap(): SmartMap<String, NoteItem> {
        return SmartMap.newMap()
    }

    @Singleton
    @Provides
    @NoteItemAnnote
    fun provideNoteItemSmartCache(): SmartCache<String, NoteItem> {
        return SmartCache.newCache()
    }

    @Singleton
    @Provides
    @WordAnnote
    fun provideWordSmartMap(): SmartMap<String, Word> {
        return SmartMap.newMap()
    }

    @Singleton
    @Provides
    @WordAnnote
    fun provideWordSmartCache(): SmartCache<String, Word> {
        return SmartCache.newCache()
    }

    @Singleton
    @Provides
    @WordItemAnnote
    fun provideWordItemSmartMap(): SmartMap<String, WordItem> {
        return SmartMap.newMap()
    }

    @Singleton
    @Provides
    @WordItemAnnote
    fun provideWordItemSmartCache(): SmartCache<String, WordItem> {
        return SmartCache.newCache()
    }
}