package com.dreampany.framework.injector.annote

import androidx.lifecycle.ViewModel
import dagger.MapKey
import kotlin.reflect.KClass


/**
 * Created by roman on 2019-07-09
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@MapKey
@MustBeDocumented
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention(AnnotationRetention.RUNTIME)
annotation class ViewModelKey(val value: KClass<out ViewModel>)
